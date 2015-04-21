import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/19/2015.
 */
public class TreeSearchNode {
    CheckerState state;
    ArrayList<Move> expandAction;
    ArrayList<TreeSearchNode> children;
    TreeSearchNode parent;
    int visit_times; // searches that passed this node
    double[] util_arr;
    boolean is_terminal = false;

    TreeSearchNode(CheckerState s, TreeSearchNode p) {
        state = s;
        expandAction = s.nextMoves();
        children = null;
        visit_times = 0;
        // total utility for each player
        util_arr = new double[s.m_num_players];
        // expandable if there are children
        parent = p;
        if(s.gameOver() != 0 && expandable()) {
            is_terminal = true;
        }
    }

    public void appendChild(TreeSearchNode child) {
        if(children == null) {
            children = new ArrayList<TreeSearchNode>();
        }
        children.add(child);
    }

    public boolean expandable() {
        return expandAction.size() != 0;
    }
    /**
     * !important, this is only two player version
     * Given opponents advanced the game state to definiteChild
     * we can hack the siblings and their trees
     * @param definiteChild
     * @return
     */
    public TreeSearchNode hackSiblings(CheckerState definiteChild) {
        for(TreeSearchNode child: this.children) {
            if(child.state == definiteChild) {
                return child;
            }
        }
        return null;
    }


    public void addUtility(double[] utils) {
        for(int i = 0; i < util_arr.length; i++) {
            util_arr[i] += utils[i];
        }
    }



}