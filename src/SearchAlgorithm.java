/**
 * Created by JingyuLiu on 4/23/2015.
 */
public abstract class SearchAlgorithm {
    protected int nodes_generated = 0;
    protected static Evaluation eval_func = new Evaluation();

    public abstract Move nextMove(CheckerState state);


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
