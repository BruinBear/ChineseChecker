/**
 * Created by JingyuLiu on 5/7/2015.
 * UCT_PARANOID has different best child policy for MIN players
 * Essentially 1-xi for win rate since MIN players try to minimize MAX player's win rate
 */
public class MCTS_UCT_PARANOID extends MCTS_UCT {
    // this is the player we maximize win rate
    private int max;
    MCTS_UCT_PARANOID(double c, int nodesPerIteration, int max_player){
        super(c, nodesPerIteration);
        max = max_player;
    }

    @Override
    public TreeSearchNode bestChild(TreeSearchNode v, double const_explore) {
        // we want to maximize the player's win rate
        TreeSearchNode best_child = null;
        double explore_numerator = 2*Math.log(v.visit_times);
        double best_uct = Double.NEGATIVE_INFINITY;
        for(TreeSearchNode child : v.children) {
            if(best_child == null) {
                best_child = child;
            }
            double Xj = child.util_arr[max] / child.visit_times;
            if(!v.isMaxNode(max)) {
                Xj = 1;
            }
            double uct = Xj + Cp*Math.sqrt(2*explore_numerator/child.visit_times);

            if(best_uct < uct){
                best_uct = uct;
                best_child = child;
            }
        }
        if(best_child == null) {
            int a =1;
        }
        return best_child;
    }


}
