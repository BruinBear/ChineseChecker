/**
 * Created by JingyuLiu on 5/7/2015.
 */
public class MCTS_UCT_SOS extends MCTS_UCT {
    private double[][] social_orientation = null;

    MCTS_UCT_SOS(double c, int nodesPerIteration, double[][] so) {
        super(c, nodesPerIteration);
        this.social_orientation = so;
    }

    // Assume there is no altruism in a game
    double[][] computeSocialOrientation(double[] utils) {
        double sum = 0;
        for(int i = 0; i<utils.length; i++) {
            sum += utils[i];
        }

        double[][] so = new double[utils.length][utils.length];
        for(int i = 0; i<utils.length; i++) {
            for(int j = 0; j<utils.length; j++) {
                if(i == j) {
                    so[i][j] = 1;
                } else {
                    so[i][j] = -utils[j]/sum;
                }
            }
        }
        return so;
    }

    @Override
    public TreeSearchNode bestChild(TreeSearchNode v, double const_explore) {
        // we want to maximize the player's win rate
        int player_id_to_max = v.state.m_turn;
        TreeSearchNode best_child = null;
        double explore_numerator = 2*Math.log(v.visit_times);
        double best_uct = Double.NEGATIVE_INFINITY;
        for(TreeSearchNode child : v.children) {
            if(best_child == null) {
                best_child = child;
            }
            double[][] social;
            if(social_orientation == null) {
                social = computeSocialOrientation(child.util_arr);
            } else {
                social = social_orientation;
            }
            double Xj = this.multiplySo(child.util_arr, social_orientation)[player_id_to_max] / child.visit_times;
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