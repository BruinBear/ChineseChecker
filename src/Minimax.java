import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by JingyuLiu on 4/8/2015.
 */
public class Minimax extends SearchAlgorithm{
    public enum ALGORITHM_NAME {
        MINIMAX,
        ALPHABETA
    }

    protected static Evaluation eval_func = new Evaluation();
    protected String m_name;

    int current_depth;
    int max_depth;

    int max_player_id;
    int min_player_id;

    Minimax(String name, int depth) {
        this.max_depth = depth;
        m_name = name.toUpperCase();
    }

    public Move nextMove(CheckerState s) {
        max_player_id = s.m_turn;
        min_player_id = (s.m_turn+1)%2;
        int current_num_nodes_processed = this.nodes_generated;
        execute_once(s);
        System.out.printf("%d more nodes generated\n", this.nodes_generated - current_num_nodes_processed);
        return bestMove;
    }

    public Move nextNodeLimitedMove(CheckerState s, int node_limit) {
        // guard
        current_num_nodes = 0;
        CheckerState tmp = new CheckerState(s);
        System.out.printf("Using algorithm: %s\n", m_name);
        current_depth = 1;
        while(current_num_nodes < node_limit) {
            switch (ALGORITHM_NAME.valueOf(m_name)) {
                case MINIMAX:
                    this.bestMove = minimax(tmp, current_depth, true, max_player_id, min_player_id).getValue();
                    break;
                case ALPHABETA:
                    this.bestMove = alphabeta(tmp, current_depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getValue();
                    break;
            }
            current_depth++;
        }
        // guard
        current_num_nodes = 0;
        return bestMove;
    }


    public Move nextMoveTimed(CheckerState s, int milliseconds) {
        max_player_id = s.m_turn;
        min_player_id = (s.m_turn+1)%2;
        int current_num_nodes_processed = this.nodes_generated;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new TimedNextBestMove(this, s));
        try {
            System.out.println("Started..");
            // Force return the best move so far
            System.out.println(future.get(milliseconds, TimeUnit.MILLISECONDS));
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            System.out.println("Terminated!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.printf("%d out of %d levels searched.\n", this.current_depth, this.max_depth);
        System.out.printf("%d more nodes generated\n", this.nodes_generated - current_num_nodes_processed);
        executor.shutdownNow();
        return this.bestMove;
    }


    // For timed task Iteratively increament level. if timer expires, early termination is possible
    public void execute_iteratively(CheckerState s) {
        CheckerState tmp = new CheckerState(s);
        System.out.printf("Using algorithm: %s\n", m_name);
        current_depth = 1;
        while(current_depth<max_depth) {
            switch (ALGORITHM_NAME.valueOf(m_name)) {
                case MINIMAX:
                    this.bestMove = minimax(tmp, current_depth, true, max_player_id, min_player_id).getValue();
                    break;
                case ALPHABETA:
                    this.bestMove = alphabeta(tmp, current_depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getValue();
                    break;
            }
            current_depth++;
        }
        return;
    }

    // Regular calls limited by max depth, guaranteed completion
    public void execute_once(CheckerState s) {
        System.out.printf("Using algorithm: %s\n", m_name);
        switch (ALGORITHM_NAME.valueOf(m_name)) {
            case MINIMAX:
                this.bestMove = minimax(s, max_depth, true, max_player_id, min_player_id).getValue();
                break;
            case ALPHABETA:
                this.bestMove = alphabeta(s, max_depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getValue();
                break;
        }
        current_depth++;
        return;
    }


    public Pair<Double,Move> minimax(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id) {
        ArrayList<Move> nextMoves = node.nextUnorderedMoves();
        if(depth == 0 || node.gameOver()!=0 || nextMoves.isEmpty()) {
            Double val = eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
            return new Pair(val, null);
        }
        Double val;
        // there is at least one move
        // if no move can lead to win we need a move to play so game can end
        Move localBestMove = nextMoves.get(0);
        double bestValue;
        if(maximizing) {
            bestValue = Double.NEGATIVE_INFINITY;
            for(Move move : nextMoves) {
                node.applyMove(move);
                // commit one move and do further evaluation
                nodes_generated++;
                current_num_nodes++;
                val = minimax(node, depth - 1, false, max_player_id, min_player_id).getKey();
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    bestValue = val;
                    localBestMove = move;
                }
                node.reverseMove();

                // early termination when a winning move is found
                if(bestValue == Double.POSITIVE_INFINITY) {
                    break;
                }
            }
        }else{
            bestValue = Double.POSITIVE_INFINITY;
            for(Move move : nextMoves) {
                node.applyMove(move);
                // commit one move and do further evaluation
                nodes_generated++;
                current_num_nodes++;
                val = minimax(node, depth - 1, true, max_player_id, min_player_id).getKey();
                //System.out.printf("New value found in min node: %d\n", val);
                if(val < bestValue){
                    bestValue = val;
                    localBestMove = move;
                }
                node.reverseMove();
                // early termination when a winning move is found
                if(bestValue == Double.NEGATIVE_INFINITY) {
                    break;
                }
            }
        }
        return new Pair(bestValue, localBestMove);
    }


    public Pair<Double,Move> alphabeta(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id, double alpha, double beta) {
        ArrayList<Move> nextMoves = node.nextOrderedMoves(false);

        if(depth == 0 || node.gameOver() != 0 || nextMoves.size() == 0) {
            double val =  eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
            return new Pair(val, null);
        }
        Double val;
        double bestValue;
        Move localBestMove = nextMoves.get(0);

        if(maximizing) {
            bestValue = Double.NEGATIVE_INFINITY;
            for(Move move : nextMoves) {
                node.applyMove(move);
                nodes_generated++;
                current_num_nodes++;
                val = alphabeta(node, depth - 1, false, max_player_id, min_player_id, alpha, beta).getKey();
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    localBestMove = move;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
                bestValue = Math.max(bestValue, val);
                alpha = Math.max(alpha, bestValue);
                node.reverseMove();
                // before it is possible to break out of loop we recover the move
                if(beta <= alpha) {
                    break;
                }
            }
        }else{
            bestValue = Double.POSITIVE_INFINITY;
            for(Move move : nextMoves) {
                node.applyMove(move);
                nodes_generated++;
                current_num_nodes++;
                val = alphabeta(node, depth - 1, true, max_player_id, min_player_id, alpha, beta).getKey();
                //System.out.printf("New value found in max node: %d\n", val);
                // update next best move
                if(val < bestValue){
                    localBestMove = move;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
                bestValue = Math.min(bestValue, val);
                beta = Math.min(beta, bestValue);
                node.reverseMove();
                // before it is possible to break out of loop we recover the move
                if(beta <= alpha) {
                    break;
                }
            }
        }
        return new Pair(bestValue, localBestMove);
    }

}
