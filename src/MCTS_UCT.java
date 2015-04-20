import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/19/2015.
 */
public class MCTS_UCT implements MCTS{

    private double Cp;


    MCTS_UCT(double c) {
        Cp = c;
    }


    /**
     * Given a root node, select the next node for expansion based on UCT
     * @return
     */
    public Move uctSearch(CheckerState s0){
        
    }


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


    public TreeSearchNode expand(TreeSearchNode v){
        if(!v.expandable()) {   // All expansion made
            return null;
        }
        // Pick a random move in the pool
        int random = (int )(Math.random() * v.expandAction.size());
        // add one child to expansion
        CheckerState c_state = new CheckerState(v.state.newAndApply(v.expandAction.remove(random)));
        TreeSearchNode child = new TreeSearchNode(c_state, v);
        v.children.add(child);
        return child;
    }


    /**
     * find the best child which yields maximum UCT
     * @param v
     * @param exploration_constant
     * @return
     */
    public TreeSearchNode bestChild(TreeSearchNode v, double exploration_constant){

        int parent_n = v.visit_times;
        for(TreeSearchNode child : v.children) {

        }
        return;
    }


    public double[] defaultPolicy(CheckerState s){
        CheckerState to_play = new CheckerState(s);
        Move mv;
        while ((mv = s.getRandomMove()) != null) {
            s.movePieceTo(mv);
        }
        return s.getReward();
    }


    public void backUp(TreeSearchNode v, double[] delta){
        while(v!=null) {
            v.visit_times += 1;
            v.addUtility(delta);
            v = v.parent;
        }
    }


}
