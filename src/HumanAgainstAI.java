import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/16/2015.
 */
import java.util.Scanner;

public class HumanAgainstAI {
    private int m_max_depth;
    private CheckerState m_state;

    HumanAgainstAI(CheckerState startState, int max_depth) {
        m_max_depth = max_depth;
        m_state = startState;
    }

    public void startGame() {
        // For human choice input
        Scanner in = new Scanner(System.in);

        // turn number 0,1,2,3...
        int turn = 0;
        // Let human play first
        Algorithm alg = new Algorithm("MINIMAX_STATE");
        m_state.printBoardWithPosition();
        while(m_state.gameOver() == 0) {
            System.out.printf("Turn %d, Player %d move\n", turn, m_state.m_turn+1);
            Move mv;
            int player_id = turn%2;
            if(player_id == 0) { // human turn
//                ArrayList<Move> human_moves = m_state.nextMoves();
//                for(int move_id = 0; move_id < human_moves.size(); move_id++) {
//                    System.out.printf("%d. %s", move_id,human_moves.get(move_id).toString());
//                }
                while(true) {
                    int x, y, mx, my;
                    x = in.nextInt();
                    y = in.nextInt();
                    mx = in.nextInt();
                    my = in.nextInt();
                    IntPair p = new IntPair(x, y);
                    IntPair mp = new IntPair(mx, my);
                    mv = new Move(p, mp);
                    ArrayList<IntPair> canReach = m_state.pieceCanMove(p);
                    if(canReach.contains(mp)){
                        System.out.printf("Move Registered: %s\n", mv.toString());
                        break;
                    } else {
                        System.out.println("Move is invalid, please retry.");
                    }
                }
            } else { // computer turn
                int current_num_nodes_processed = alg.node_generated;
                alg.execute_once(m_state, m_max_depth, player_id, (player_id + 1) % 2);
                System.out.printf("%d more nodes generated\n", alg.node_generated - current_num_nodes_processed);
                mv = alg.bestMove;
            }
            m_state.movePieceTo(mv);
            // printing move info
            m_state.printBoardWithPosition();
            // increment turn
            turn++;
            System.out.printf("Player two eval to %f!\n", alg.eval_func.eval_distance_and_goal(1, m_state));
//            System.out.print("Press enter key to continue\n");
//            sc.nextLine();
        }
        System.out.printf("Number of nodes looked up: %d\n", alg.node_generated);
        System.out.printf("Winner is player %d\n", m_state.gameOver());
    }

}
