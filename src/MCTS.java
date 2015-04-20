import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/19/2015.
 */
public interface MCTS {
    /**
     * Given a root node, select the next node for expansion based on UCT
     * @return
     */
    public Move uctSearch(CheckerState s0);


    public TreeSearchNode treePolicy(TreeSearchNode v);


    public TreeSearchNode expand(TreeSearchNode v);


    public TreeSearchNode bestChild(TreeSearchNode v, double exploration_constant);


    public double[] defaultPolicy(CheckerState s);


    public void backUp(TreeSearchNode v, double[] delta);


}
