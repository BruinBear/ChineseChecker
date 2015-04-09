import javafx.util.Pair;

/**
 * Created by JingyuLiu on 4/8/2015.
 */
public class Algorithm {
    public int minimax(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id) {
        if(depth == 0 || node.gameOver() != 0) {
            return node.evaluation_goal_distance(max_player_id)-node.evaluation_goal_distance(min_player_id);
        }
        CheckerState bestNextState = null;
        if(maximizing) {
            Integer bestValue = Integer.MIN_VALUE;
            for(CheckerState child : node.nextStates()) {
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
            for(CheckerState child : node.nextStates()) {
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
}
