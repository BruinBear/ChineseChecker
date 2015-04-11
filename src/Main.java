import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main{

    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!\n");

        CheckerState b = new CheckerState();
//        b.printBoard();
//        System.out.printf("Player one eval to %d!\n", b.eval_distance_and_goal(0));
//        System.out.printf("Player two eval to %d!\n", b.eval_distance_and_goal(1));
        Scanner sc = new Scanner(System.in);
//
//        b.m_grid = new char[][]{
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '2', '2', '0', '2', '0', '0', '0', '0', '0', '0', ' '},
//                {' ', ' ', ' ', ' ', '2', '2', '0', '0', '0', '0', '0', '0', '0', ' ', ' '},
//                {' ', ' ', ' ', ' ', '2', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '1', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '1', '1', ' ', ' ', ' ', ' '},
//                {' ', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
//        };
//        b.printBoard();
//        b.rescanPieces();
//        System.out.printf("Player one eval to %d!\n", b.eval_distance_and_goal(0));
//        Algorithm a = new Algorithm("MINIMAX");
//        a.minimax(b, 3,true, 0, 1);
//        b.bestnext.printBoard();
//        System.out.printf("Player one eval to %d!\n", b.bestnext.eval_distance_and_goal(0));
        // time allowed for each turn
        int timePerTurn = 500;
        // turn number 0,1,2,3...
        int turn = 0;
        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
        algorithms.add(new Algorithm("MINIMAX"));
        algorithms.add(new Algorithm("ALPHABETA"));
        while(b.gameOver() == 0) {
            System.out.printf("Turn %d, Player %d move\n", turn, b.m_turn+1);
            int current_num_nodes_processed = algorithms.get(turn%2).node_generated;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Integer> future = executor.submit(new GetBestNextMoveTask(algorithms.get(turn%2),b,8, turn%2, (turn+1)%2));
            try {
                System.out.println("Started..");
                // Force return the best move so far
                System.out.println(future.get(timePerTurn, TimeUnit.MILLISECONDS));
                System.out.println("Finished!");
            } catch (TimeoutException e) {
                System.out.println("Terminated!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.printf("%d out of %d levels searched.\n", algorithms.get(turn%2).current_depth,algorithms.get(turn%2).max_depth);
            System.out.printf("%d more nodes generated\n", algorithms.get(turn%2).node_generated - current_num_nodes_processed);
            executor.shutdownNow();

            // commit move
            b = b.bestnext;
            // printing move info
            b.printBoard();
            // increment turn
            turn++;
            System.out.printf("Player one eval to %d!\n", b.eval_distance_and_goal(0));
            System.out.printf("Player two eval to %d!\n", b.eval_distance_and_goal(1));
//            System.out.print("Press enter key to continue\n");
//            sc.nextLine();
        }
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(0).node_generated);
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(1).node_generated);
        System.out.printf("Winner is player %d\n", b.gameOver());
    }


}
