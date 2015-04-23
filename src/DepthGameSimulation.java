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
        ArrayList<Minimax> algorithms = new ArrayList<Minimax>();
        algorithms.add(new Minimax("MINIMAX",4));
        algorithms.add(new Minimax("alphabeta", 4));
        while(m_state.gameOver() == 0) {
            System.out.printf("Turn %d, Player %d move\n", turn, m_state.m_turn+1);
            int player_id = turn%2;
            Minimax alg_to_use = algorithms.get(player_id);
            int current_num_nodes_processed = alg_to_use.node_expanded;
            alg_to_use.execute_once(m_state);
            System.out.printf("%d more nodes generated\n", algorithms.get(turn%2).node_expanded - current_num_nodes_processed);
            // printing move info
            m_state.printBoard();
            // increment turn
            turn++;
            System.out.printf("Player one eval to %f!\n", algorithms.get(0).eval_func.eval_distance_and_goal(0, m_state));
            System.out.printf("Player two eval to %f!\n", algorithms.get(1).eval_func.eval_distance_and_goal(1, m_state));
//            System.out.print("Press enter key to continue\n");
//            sc.nextLine();
        }
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(0).node_expanded);
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(1).node_expanded);
        System.out.printf("Winner is player %d\n", m_state.gameOver());

    }
}
