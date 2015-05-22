import java.util.concurrent.*;

/**
 * Created by JingyuLiu on 4/23/2015.
 */
public abstract class SearchAlgorithm {
    protected int nodes_generated = 0;
    protected static Evaluation eval_func = new Evaluation();
    protected Move bestMove;
    protected int current_depth;
    protected int max_depth;
    protected int current_num_nodes = 0;
    protected int max_nodes_per_iteration;
    protected boolean nodes_limiting = false;

    public abstract Move nextMove(CheckerState state);

    public abstract void execute_iteratively(CheckerState s);

    /**
     * This get the best move so far and then reset best move
     * @return
     */
    public Move getTimedBestMove() {
        Move bm = this.bestMove;
        this.bestMove = null;
        return bm;
    }

    public abstract Move nextNodeLimitedMove(CheckerState s, int node_limit);


    /**
     * For iteratively executing Maxn and Paranoid search
     * @param s
     * @param milliseconds
     * @return
     */
    public Move nextMoveTimed(CheckerState s, int milliseconds) {
        Move bm;
        // increase time required to compute at least one best move.
        int i = 1;
        do {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Integer> future = executor.submit(new TimedNextBestMove(this, new CheckerState(s)));
            try {
                TimeUnit.MILLISECONDS.sleep(i*milliseconds);
            } catch (InterruptedException e) {

            }
            try {
                System.out.println(String.format("%s started with Max Depth %d in %f seconds", this.getClass().getName(), max_depth,i * (double)milliseconds / 1000));
                // Force return the best move so far
                System.out.println(future.get(i * milliseconds, TimeUnit.MILLISECONDS));
                System.out.println("Finished at depth " + this.current_depth);
            } catch (TimeoutException e) {
                System.out.println("Terminated at depth " + this.current_depth);
            } catch (InterruptedException e) {
//            e.printStackTrace();
            } catch (ExecutionException e) {
//            e.printStackTrace();
            }
            executor.shutdownNow();
            // exponential time increase
            i = i * 2;
        } while((bm=this.getTimedBestMove())==null);
        return bm;
    }


    public int numNodesGenertaed(){
        return nodes_generated;
    };


    public void reset() {
        nodes_generated = 0;
    }

    protected double[] getTuple(CheckerState s) {
        double[] n_tuple = new double[s.m_num_players];
        for(int i=0; i<s.m_num_players; i++) {
            n_tuple[i] = eval_func.eval_distance_and_goal(i, s);
        }
        return n_tuple;
    }


    /**
     * Compute the transformed utility value from Social Orientation Matrix
     * @param tuple
     * @return
     */
    protected double[] multiplySo(double[] tuple, double[][] social_orientation) {
        double[] transformed = new double[tuple.length];
        for(int i = 0; i<social_orientation.length; i++) {
            for(int j = 0; j<social_orientation[0].length; j++) {
                transformed[i] += social_orientation[i][j] * tuple[j];
            }
        }
        return transformed;
    }
}
