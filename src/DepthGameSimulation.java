import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/11/2015.
 */
public class DepthGameSimulation {
    private int m_max_depth;
    private CheckerState m_state;

    DepthGameSimulation(CheckerState startState, int max_depth) {
        m_max_depth = max_depth;
        m_state = startState;
    }

    public void startGame() {
        // turn number 0,1,2,3...
        int turn = 0;
        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
        algorithms.add(new Algorithm("MINIMAX"));
        algorithms.add(new Algorithm("ALPHABETA"));
        while(m_state.gameOver() == 0) {
            System.out.printf("Turn %d, Player %d move\n", turn, m_state.m_turn+1);
            int player_id = turn%2;
            Algorithm alg_to_use = algorithms.get(player_id);
            int current_num_nodes_processed = alg_to_use.node_generated;
            alg_to_use.execute_once(m_state, m_max_depth, player_id, (player_id + 1) % 2);
            System.out.printf("%d more nodes generated\n", algorithms.get(turn%2).node_generated - current_num_nodes_processed);

            // commit move
            m_state = m_state.bestnext;
            // printing move info
            m_state.printBoard();
            // increment turn
            turn++;
            System.out.printf("Player one eval to %f!\n", algorithms.get(0).eval_func.eval_distance_and_goal(0, m_state));
            System.out.printf("Player one eval to %f!\n", algorithms.get(0).eval_func.eval_distance_and_goal(1, m_state));
//            System.out.print("Press enter key to continue\n");
//            sc.nextLine();
        }
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(0).node_generated);
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(1).node_generated);
        System.out.printf("Winner is player %d\n", m_state.gameOver());

    }
}
