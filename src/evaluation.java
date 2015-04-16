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
        LinkedList<IntPair> goal_queue = new LinkedList<IntPair>();
        for(IntPair p : b.m_players_goals.get(player_id)) {
            if (b.m_grid[p.x][p.y] != '1' + player_id) {
                goal_queue.add(p);
            }
        }

        IntPair goal_c = b.m_players_goal_center.get(player_id);
        int farthest_piece_dist = 0;
        int piece_dist = 0;
        for(IntPair p : b.m_players_pieces.get(player_id))
        {
            if(b.grid_2_s_goal[p.x][p.y] == '1'+player_id) {
                continue;
            }

            // farthest piece distance to goal center
            farthest_piece_dist = Math.max(farthest_piece_dist,  Math.abs(p.x - goal_c.x) + Math.abs(p.y - goal_c.y));
            int min_to_goal = Integer.MAX_VALUE;
            for(IntPair g: goal_queue) {
                min_to_goal = Math.min(min_to_goal, Math.abs(p.x - g.x) + Math.abs(p.y - g.y));
            }
            piece_dist += min_to_goal;
        }

        double evaluation;
        // Winning reward
        if(b.gameOver() == player_id+1) {
            evaluation = Double.POSITIVE_INFINITY;
        } else if(b.gameOver() != 0) {
            evaluation = Double.NEGATIVE_INFINITY;
        } else{
            evaluation = - piece_dist - farthest_piece_dist;
        }
//        System.out.printf("One Step Sum: %d, Max Distance: %d\n", oneStepSum, farthest_piece_dist);
//        System.out.printf("Goal bonus: %d\n", goalBonus);
//        System.out.printf("Evaluation: %d\n", evaluation);

        return evaluation;
    }

}
