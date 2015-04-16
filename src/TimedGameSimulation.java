import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by JingyuLiu on 4/11/2015.
 */
public class TimedGameSimulation {
    private int m_turntime;
    private int m_max_depth;
    private CheckerState m_state;

    TimedGameSimulation(CheckerState startState, int turntime, int max_depth) {
        m_max_depth = max_depth;
        m_turntime = turntime;
        m_state = startState;
    }

    public void startGame() {
        // turn number 0,1,2,3...
        int turn = 0;
        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
        algorithms.add(new Algorithm("ALPHABETA"));
        algorithms.add(new Algorithm("MINIMAX"));
        while(m_state.gameOver() == 0) {
            System.out.printf("Turn %d, Player %d move\n", turn, m_state.m_turn+1);
            int current_num_nodes_processed = algorithms.get(turn%2).node_generated;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Integer> future = executor.submit(new GetBestNextMoveTask(algorithms.get(turn%2), m_state, m_max_depth, turn%2, (turn+1)%2));
            try {
                System.out.println("Started..");
                // Force return the best move so far
                System.out.println(future.get(m_turntime, TimeUnit.MILLISECONDS));
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
