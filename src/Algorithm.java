import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/8/2015.
 */
public class Algorithm {
    protected int node_generated = 0;
    public enum ALGORITHM_NAME {
        MINIMAX,
        ALPHABETA
    }

    protected ALGORITHM_NAME m_name;
    Algorithm(String name) {
        m_name = ALGORITHM_NAME.valueOf(name.toUpperCase());
    }

    public void execute(CheckerState node, int depth, int max_player_id, int min_player_id) {
        switch(m_name) {
            case MINIMAX:
                minimax(node, 3, true, max_player_id, min_player_id);
                break;
            case ALPHABETA:
                alphabeta(node,3,true, max_player_id, min_player_id, Integer.MIN_VALUE, Integer.MAX_VALUE);
                 break;
        }
        return;
    }

    public int minimax(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id) {
        if(depth == 0 || node.gameOver() != 0) {
            return node.evaluation_goal_distance(max_player_id)-node.evaluation_goal_distance(min_player_id);
        }
        CheckerState bestNextState = null;
        ArrayList<CheckerState> checkerNextStates = node.nextStates();
        node_generated += checkerNextStates.size();
        if(maximizing) {
            Integer bestValue = Integer.MIN_VALUE;
            for(CheckerState child : checkerNextStates) {
                int val = minimax(child, depth - 1, false, max_player_id, min_player_id);
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
            Integer bestValue = Integer.MAX_VALUE;
            for(CheckerState child : checkerNextStates) {
                int val = minimax(child, depth - 1, true, max_player_id, min_player_id);
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


    public int alphabeta(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id, int alpha, int beta) {
        if(depth == 0 || node.gameOver() != 0) {
            return node.evaluation_goal_distance(max_player_id)-node.evaluation_goal_distance(min_player_id);
        }
        CheckerState bestNextState = null;
        ArrayList<CheckerState> checkerNextStates = node.nextStates();
        if(maximizing) {
            Integer bestValue = Integer.MIN_VALUE;
            for(CheckerState child : checkerNextStates) {
                int val = alphabeta(child, depth - 1, false, max_player_id, min_player_id, alpha, beta);
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
            Integer bestValue = Integer.MAX_VALUE;
            for(CheckerState child : checkerNextStates) {
                int val = alphabeta(child, depth - 1, true, max_player_id, min_player_id, alpha, beta);
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
