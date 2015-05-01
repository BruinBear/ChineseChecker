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


    protected double[] getTuple(CheckerState s) {
        double[] n_tuple = new double[s.m_num_players];
        for(int i=0; i<s.m_num_players; i++) {
            n_tuple[i] = eval_func.eval_distance_and_goal(i, s);
        }
        return n_tuple;
    }
}
