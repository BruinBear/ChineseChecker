import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main{

    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!\n");

        CheckerState b = new CheckerState();
        b.printBoard();
        System.out.printf("Player one eval to %d!\n", b.evaluation_goal_distance(0));
        b.printBoard();
        Scanner sc = new Scanner(System.in);

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
                System.out.println(future.get(6, TimeUnit.SECONDS));
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
//            System.out.print("Press enter key to continue\n");
//            sc.nextLine();
        }
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(0).node_generated);
        System.out.printf("Number of nodes looked up: %d\n", algorithms.get(1).node_generated);
        System.out.printf("Winner is player %d\n", b.gameOver());
    }


}
