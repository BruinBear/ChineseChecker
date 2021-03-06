/**
 * Created by JingyuLiu on 4/19/2015.
 */
public class MCTS_UCT extends SearchAlgorithm implements MCTS{
    protected TreeSearchNode v0;

    protected double Cp;
    protected int nodesPerIteration;
    MCTS_UCT(double c, int nodesPerIteration) {
        Cp = c;
        this.nodesPerIteration = nodesPerIteration;
    }


    public Move nextMove(CheckerState state) {
        return uctSearch(state);
    }


    public Move nextNodeLimitedMove(CheckerState s, int node_limit) {
        nodesPerIteration = node_limit;
        return uctSearch(s);
    }


    // For timed task Iteratively increament level. if timer expires, early termination is possible
    public void execute_iteratively(CheckerState s) {
        v0 = new TreeSearchNode(s, null);
        while(true) {
            TreeSearchNode vl = treePolicy(v0);
            double[] delta = defaultPlayoutPolicy(new CheckerState(vl.state));
            backUp(vl, delta);
            nodes_generated++;
            current_num_nodes++;
        }
    }


    @Override
    public Move getTimedBestMove() {
        Move bm;
        if(v0 == null) {
            return null;
        } else {
            try {
                bm = bestChild(v0, 0).state.getLastMove();
                v0 = null;
            }catch (Exception e) {
                e.printStackTrace();
                v0 = null;
                return null;
            }
        }
        return bm;
    }


    /**
     * Given a root node, select the next node for expansion based on UCT
     * @return the move to reach the best child selected by UCT
     */
    public Move uctSearch(CheckerState s0){
        v0 = new TreeSearchNode(s0, null);
        int i = 0;
        while(i++<this.nodesPerIteration) {
            TreeSearchNode vl = treePolicy(v0);
            double[] delta = defaultPlayoutPolicy(new CheckerState(vl.state));
            backUp(vl, delta);
            nodes_generated++;
            current_num_nodes++;
        }
//        printChildrenStats(v0);
        return bestChildEnd(v0).state.getLastMove();
    }


    public void printChildrenStats(TreeSearchNode v) {
        System.out.printf("Parent Total Visit %d\n", v.visit_times);
        for(TreeSearchNode child : v.children){
            System.out.printf("Child Visit %d\n", child.visit_times);
            System.out.printf("1 won %f, 2 won %f\n",child.util_arr[0] ,child.util_arr[1]);
        }
    }


    /**
     * Done
     * @param v which is node we would like to expand
     * @return  the expanded child
     *          OR best child of v if v is fully expanded
     *          OR v itself if v is not expandable
     */
    public TreeSearchNode treePolicy(TreeSearchNode v){
        while(!v.isTerminal()) {
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
     * find the best child which yields maximum UCT
     * UCT = Xj + c * sqrt (2* ln(parent visit counts) / child visit counts)
     * @param v
     * @return TreeSearchNode best_child obtained with UCT
     */
    public TreeSearchNode bestChild(TreeSearchNode v, double const_explore){
        // we want to maximize the player's win rate
        int player_id_to_max = v.state.m_turn;
        TreeSearchNode best_child = null;
        double explore_numerator = 2*Math.log(v.visit_times);
        double best_uct = Double.NEGATIVE_INFINITY;
        for(TreeSearchNode child : v.children) {
            if(best_child == null) {
                best_child = child;
            }
            double Xj = child.util_arr[player_id_to_max] / child.visit_times;
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

    /**
     * find the best child which yields maximum UCT
     * UCT = Xj + c * sqrt (2* ln(parent visit counts) / child visit counts)
     * @param v
     * @return TreeSearchNode best_child obtained with UCT
     */
    public TreeSearchNode bestChildEnd(TreeSearchNode v){
        // we want to maximize the player's win rate
        int player_id_to_max = v.state.m_turn;
        TreeSearchNode best_child = null;
        double best_uct = Double.NEGATIVE_INFINITY;
        for(TreeSearchNode child : v.children) {
            if(best_child == null) {
                best_child = child;
            }
            double Xj = child.util_arr[player_id_to_max] / child.visit_times;
            double uct = Xj;

            if(best_uct < uct){
                best_uct = uct;
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
    public double[] defaultPlayoutPolicy(CheckerState s){
        Move mv;
        while ((mv = s.epsilonMove()) != null) {
            s.applyMove(mv);
//            s.printBoard();
        }
        if(0==s.gameOver()) {
            s.printBoard();
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
            v.visit_times = v.visit_times+1;
            v.addUtility(delta);
            v = v.parent;
        }
    }

}
