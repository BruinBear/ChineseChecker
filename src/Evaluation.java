import java.util.LinkedList;

/**
 * Created by JingyuLiu on 4/13/2015.
 */
public class Evaluation {
    /**
     *     Three strategies are implied by this evaluation function
     *     1. pair wise pieces not at goal to the closest goal distance
     *     2. farthest piece distance to goal center
     *     4. game won reward and game lose penalty
     */
    public double eval_distance_and_goal(int player_id, CheckerState b) {

        // Static goal distance evaluation
        IntPair far_goal = b.m_players_far_goal.get(player_id);

        int max_to_goal = 0;
        int piece_dist = 0;
        int completion = 0;
        for (IntPair p : b.m_players_pieces.get(player_id)) {
            int dis = far_goal.minPathDistance(p);
            piece_dist += dis;
            max_to_goal = Math.max(max_to_goal, dis);
        }

        for (IntPair g : b.m_players_goals.get(player_id)) {
            if(b.m_grid[g.x][g.y] == '1'+player_id) {
                completion += 3;
            }
        }

//        for(IntPair p : b.m_players_pieces.get(player_id))
//        {
//            if(b.grid_2_s_goal[p.x][p.y] == '1'+player_id) {
//                continue;
//            }
//
//            // farthest piece distance to goal center
//            int min_to_goal = Integer.MAX_VALUE;
//            int goal_to_remove = 0;
//            int i = 0;
//            for(IntPair g: goal_queue) {
//                if(g.minPathDistance(p)<min_to_goal){
//                    min_to_goal = g.minPathDistance(p);
//                    goal_to_remove = i;
//                }
//                i++;
//            }
//            piece_dist += min_to_goal;
//            max_to_goal = Math.max(max_to_goal, min_to_goal);
//        }

        double evaluation;
        // Winning reward
        if(b.gameOver() == player_id+1) {
            evaluation = +100000;
        } else if(b.gameOver() != 0) {
            evaluation = -100000;
        } else{
            evaluation = - piece_dist - 0.5 * max_to_goal + completion;
        }
//        System.out.printf("One Step Sum: %d, Max Distance: %d\n", oneStepSum, farthest_piece_dist);
//        System.out.printf("Goal bonus: %d\n", goalBonus);
//        System.out.printf("Evaluation: %d\n", evaluation);

        return evaluation;
    }

}
