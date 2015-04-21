/**
 * Created by JingyuLiu on 4/19/2015.
 */
public class MCTS_UCT implements MCTS{

    private double Cp;


    public MCTS_UCT(double c) {
        Cp = c;
    }


    /**
     * Given a root node, select the next node for expansion based on UCT
     * @return the move to reach the best child selected by UCT
     */
    public Move uctSearch(CheckerState s0){
        TreeSearchNode v0 = new TreeSearchNode(s0, null);
        int i = 0;
        while(i++<2) {
            TreeSearchNode vl = treePolicy(v0);
            double[] delta = defaultPolicy(vl.state);
            backUp(vl, delta);
        }
        return bestChild(v0, 0).state.getLastMove();
    }



    /**
     * Done
     * @param v which is node we would like to expand
     * @return  the expanded child
     *          OR best child of v if v is fully expanded
     *          OR v itself if v is not expandable
     */
    public TreeSearchNode treePolicy(TreeSearchNode v){
        while(!v.is_terminal) {
            if(v.expandable()) {
                return expand(v);
            } else {
                v = bestChild(v, Cp);
            }
        }
        return v;
    }


    /**
     * Done
     * @param v
     * @return expand a node v if possible
     * an move is removed from that node because it used for expansion
     * append the result child to v
     * return the child created
     */
    public TreeSearchNode expand(TreeSearchNode v){
        if(!v.expandable()) {   // All expansion made
            return null;
        }
        // Pick a random move in the pool
        int random = (int )(Math.random() * v.expandAction.size());
        // add one child to expansion, state is a deep copy
        CheckerState c_state = v.state.newAndApply(v.expandAction.remove(random));
        TreeSearchNode child = new TreeSearchNode(c_state, v);
        v.appendChild(child);
        return child;
    }


    /**
     * TODO
     * find the best child which yields maximum UCT
     * UCT = Xj + c * sqrt (2* ln(parent visit counts) / child visit counts)
     * @param v
     * @return TreeSearchNode best_child obtained with UCT
     */
    public TreeSearchNode bestChild(TreeSearchNode v, double const_explore){
        // we want to maximize the player's win rate
        int player_id_to_max = v.state.m_turn;
        TreeSearchNode best_child = null;
        double[] best_util = null;
        double explore_numerator = 2*Math.log(v.visit_times);

        for(TreeSearchNode child : v.children) {
            if(best_child == null) {
                best_util = child.util_arr;
                best_child = child;
            }
            double Xj = child.util_arr[player_id_to_max] / child.visit_times;
            double uct = Xj + Cp*Math.sqrt(2*explore_numerator/child.visit_times);

            if(best_util[player_id_to_max] < uct){
                best_util = child.util_arr;
                best_child = child;
            }
        }
        return best_child;
    }


    /**
     * @param s a game state
     * @return the final reward at end game in a tuple of size number of players
     * (1,0,0...) player 0 won
     */
    public double[] defaultPolicy(CheckerState s){
        Move mv;
        while ((mv = s.getRandomMove()) != null) {
            s.movePieceTo(mv);
            if(s.m_turn_played % 1000 == 0) {
                System.out.printf("Simulated %d steps\n", s.m_turn_played);
                s.printBoard();
            }
        }
        return s.getReward();
    }


    /**
     * Back up a reward to parent
     * @param v node to back up through
     * @param delta utilities
     */
    public void backUp(TreeSearchNode v, double[] delta){
        while(v!=null) {
            v.visit_times += 1;
            v.addUtility(delta);
            v = v.parent;
        }
    }

}
