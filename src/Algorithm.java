import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/8/2015.
 */
public class Algorithm {
    public enum ALGORITHM_NAME {
        MINIMAX,
        ALPHABETA
    }

    protected int node_generated = 0;
    protected static Evaluation eval_func = new Evaluation();
    protected String m_name;
    int current_depth;
    int max_depth;

    Algorithm(String name) {
        m_name = name;
    }

    public void execute_iteratively(CheckerState node, int depth, int max_player_id, int min_player_id) {
        System.out.printf("Using algorithm: %s\n", m_name);
        max_depth = depth;
        current_depth = 1;
        while(current_depth<max_depth) {
            switch (ALGORITHM_NAME.valueOf(m_name)) {
                case MINIMAX:
                    minimax(node, current_depth, true, max_player_id, min_player_id);
                    break;
                case ALPHABETA:
                    alphabeta(node, current_depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    break;
            }
            current_depth++;
        }
        return;
    }

    public void execute_once(CheckerState node, int depth, int max_player_id, int min_player_id) {
        System.out.printf("Using algorithm: %s\n", m_name);
        switch (ALGORITHM_NAME.valueOf(m_name)) {
            case MINIMAX:
                minimax(node, depth, true, max_player_id, min_player_id);
                break;
            case ALPHABETA:
                alphabeta(node, depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                break;
        }
        current_depth++;
        return;
    }

    public double minimax(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id) {
        if(depth == 0 || node.gameOver()!=0) {
            return eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
        }
        ArrayList<CheckerState> checkerNextStates = node.nextStates();
        CheckerState bestNextState = checkerNextStates.get(0);
        node_generated += checkerNextStates.size();
        Double val;
        if(maximizing) {
            double bestValue = Double.NEGATIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = minimax(child, depth - 1, false, max_player_id, min_player_id);
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    bestValue = val;
                    bestNextState = child;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }else{
            double bestValue = Double.POSITIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = minimax(child, depth - 1, true, max_player_id, min_player_id);
                //System.out.printf("New value found in min node: %d\n", val);
                if(val < bestValue){
                    bestValue = val;
                    bestNextState = child;
                    //System.out.printf("New best found in min node: %d\n", bestValue);
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }
    }


    public double alphabeta(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id, double alpha, double beta) {
        ArrayList<CheckerState> checkerNextStates = node.nextStates();
        if(depth == 0 || node.gameOver() != 0 || checkerNextStates.size() == 0) {
            return eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
        }
        double val;
        CheckerState bestNextState = checkerNextStates.get(0);
        if(maximizing) {
            double bestValue = Double.NEGATIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = alphabeta(child, depth - 1, false, max_player_id, min_player_id, alpha, beta);
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    bestNextState = child;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
                bestValue = Math.max(bestValue, val);
                alpha = Math.max(alpha, bestValue);
                node_generated++;
                if(beta <= alpha) {
                    break;
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }else{
            double bestValue = Double.POSITIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = alphabeta(child, depth - 1, true, max_player_id, min_player_id, alpha, beta);
                //System.out.printf("New value found in max node: %d\n", val);
                // update next best move
                if(val < bestValue){
                    bestNextState = child;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
                bestValue = Math.min(bestValue, val);
                beta = Math.min(beta, bestValue);
                node_generated++;
                if(beta <= alpha) {
                    break;
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }
    }
}
