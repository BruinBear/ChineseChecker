import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class CanvasDemo extends JFrame{

    private CheckerState checkerboard;
    private CheckerState checkerboard_buf;

    public CanvasDemo() {
        checkerboard = new CheckerState();
        checkerboard_buf = new CheckerState();
        setLayout(new BorderLayout());
        setSize(600, 600);
        add("Center", canvas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chinese Checker");
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private class GameCanvas extends JPanel {
        @Override
        public void paint(Graphics g) {
//            Image board = new ImageIcon("checkerboard.gif").getImage();
//            g.drawImage(board, 10,10,435,500,null);
            checkerboard_buf.printBoardOnGraphics(g);
        }

    }
    private GameCanvas canvas = new GameCanvas();

    public void startGame() {
        // For human choice input
        Scanner in = new Scanner(System.in);
        // turn number 0,1,2,3...
        int turn = 0;
        // Let human play first
        Algorithm alg = new Algorithm("ALPHABETA_STATE");
        while(checkerboard.gameOver() == 0) {
            System.out.printf("Turn %d, Player %d move\n", turn, checkerboard.m_turn+1);
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
                    ArrayList<IntPair> canReach = checkerboard.pieceCanMove(p);
                    if(canReach.contains(mp)){
                        System.out.printf("Move Registered: %s\n", mv.toString());
                        break;
                    } else {
                        System.out.println("Move is invalid, please retry.");
                    }
                }
            } else { // computer turn
                int current_num_nodes_processed = alg.node_generated;
                alg.execute_once(checkerboard, 6, player_id, (player_id + 1) % 2);
                System.out.printf("%d more nodes generated\n", alg.node_generated - current_num_nodes_processed);
                mv = alg.bestMove;
            }
            checkerboard.movePieceTo(mv);
            checkerboard_buf = new CheckerState(checkerboard);
            // printing move info
            repaint();
            // increment turn
            turn++;
            System.out.printf("Player two eval to %f!\n", alg.eval_func.eval_distance_and_goal(1, checkerboard));
//            System.out.print("Press enter key to continue\n");
//            sc.nextLine();
        }
        System.out.printf("Number of nodes looked up: %d\n", alg.node_generated);
        System.out.printf("Winner is player %d\n", checkerboard.gameOver());
    }


    public static void main(String[] args) {
        CanvasDemo fr = new CanvasDemo();
        fr.startGame();
    }


}