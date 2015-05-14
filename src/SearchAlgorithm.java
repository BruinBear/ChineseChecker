import java.util.concurrent.*;

/**
 * Created by JingyuLiu on 4/23/2015.
 */
public abstract class SearchAlgorithm {
    protected int nodes_generated = 0;
    protected static Evaluation eval_func = new Evaluation();
    protected Move bestMove;
    protected int current_depth;

    public abstract Move nextMove(CheckerState state);

    public abstract void execute_iteratively(CheckerState s);

    public Move getTimedBestMove() {
        return bestMove;
    }


    public Move nextMoveTimed(CheckerState s, int miliseconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new TimedNextBestMove(this, new CheckerState(s)));
        try {
            System.out.println("Started..");
            // Force return the best move so far
            System.out.println(future.get(miliseconds, TimeUnit.MILLISECONDS));
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            System.out.println("Terminated!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
        return this.getTimedBestMove();
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
