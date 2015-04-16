import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/8/2015.
 */
public class Algorithm {
    public enum ALGORITHM_NAME {
        MINIMAX,
        ALPHABETA,
        MINIMAX_STATE,
        ALPHABETA_STATE
    }

    protected int node_generated = 0;
    protected static Evaluation eval_func = new Evaluation();
    protected String m_name;

    // will be set once we start expansion
    protected Move bestMove = null;

    int current_depth;
    int max_depth;

    Algorithm(String name) {
        m_name = name.toUpperCase();
    }

    // For timed task Iteratively increament level. if timer expires, early termination is possible
    public void execute_iteratively(CheckerState node, int depth, int max_player_id, int min_player_id) {
        System.out.printf("Using algorithm: %s\n", m_name);
        max_depth = depth;
        current_depth = 1;
        while(current_depth<max_depth) {
            switch (ALGORITHM_NAME.valueOf(m_name)) {
                case MINIMAX:
                    minimax(node, current_depth, true, max_player_id, min_player_id);
                    break;
                case ALPHABETA:
                    alphabeta(node, current_depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    break;
                case MINIMAX_STATE:
                    minimax_state(node, current_depth, true, max_player_id, min_player_id);
                    break;
                case ALPHABETA_STATE:
                    alphabeta_state(node, current_depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                    break;
            }
            current_depth++;
        }
        return;
    }

    // Regular calls limited by max depth, guaranteed completion
    public void execute_once(CheckerState node, int depth, int max_player_id, int min_player_id) {
        System.out.printf("Using algorithm: %s\n", m_name);
        switch (ALGORITHM_NAME.valueOf(m_name)) {
            case MINIMAX:
                minimax(node, depth, true, max_player_id, min_player_id);
                break;
            case ALPHABETA:
                alphabeta(node, depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                break;
            case MINIMAX_STATE:
                minimax_state(node, depth, true, max_player_id, min_player_id);
                break;
            case ALPHABETA_STATE:
                alphabeta_state(node, depth, true, max_player_id, min_player_id, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                break;
        }
        current_depth++;
        return;
    }


    public double minimax(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id) {
        if(depth == 0 || node.gameOver()!=0) {
            return eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
        }
        ArrayList<CheckerState> checkerNextStates = node.nextStates();
        CheckerState bestNextState = checkerNextStates.get(0);
        node_generated += checkerNextStates.size();
        Double val;
        if(maximizing) {
            double bestValue = Double.NEGATIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = minimax(child, depth - 1, false, max_player_id, min_player_id);
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    bestValue = val;
                    bestNextState = child;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }else{
            double bestValue = Double.POSITIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = minimax(child, depth - 1, true, max_player_id, min_player_id);
                //System.out.printf("New value found in min node: %d\n", val);
                if(val < bestValue){
                    bestValue = val;
                    bestNextState = child;
                    //System.out.printf("New best found in min node: %d\n", bestValue);
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }
    }


    public double alphabeta(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id, double alpha, double beta) {
        ArrayList<CheckerState> checkerNextStates = node.nextStates();
        if(depth == 0 || node.gameOver() != 0 || checkerNextStates.size() == 0) {
            return eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
        }
        double val;
        CheckerState bestNextState = checkerNextStates.get(0);
        if(maximizing) {
            double bestValue = Double.NEGATIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = alphabeta(child, depth - 1, false, max_player_id, min_player_id, alpha, beta);
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    bestNextState = child;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
                bestValue = Math.max(bestValue, val);
                alpha = Math.max(alpha, bestValue);
                node_generated++;
                if(beta <= alpha) {
                    break;
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }else{
            double bestValue = Double.POSITIVE_INFINITY;
            for(CheckerState child : checkerNextStates) {
                val = alphabeta(child, depth - 1, true, max_player_id, min_player_id, alpha, beta);
                //System.out.printf("New value found in max node: %d\n", val);
                // update next best move
                if(val < bestValue){
                    bestNextState = child;
                    //System.out.printf("New best found in max node: %d\n", bestValue);
                }
                bestValue = Math.min(bestValue, val);
                beta = Math.min(beta, bestValue);
                node_generated++;
                if(beta <= alpha) {
                    break;
                }
            }
            node.bestnext = bestNextState;
            return bestValue;
        }
    }


    public Pair<Double,Move> minimax_state(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id) {
        ArrayList<Move> nextMoves = node.nextMoves();
        if(depth == 0 || node.gameOver()!=0 || nextMoves.isEmpty()) {
            Double val = eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
            return new Pair(val, null);
        }
        Double val;
        // there is at least one move
        // if no move can lead to win we need a move to play so game can end
        Move bestMove = nextMoves.get(0);
        double bestValue;
        if(maximizing) {
            bestValue = Double.NEGATIVE_INFINITY;
            for(Move move : nextMoves) {
                node.movePieceTo(move);
                // commit one move and do further evaluation
                node_generated++;
                val = minimax_state(node, depth - 1, false, max_player_id, min_player_id).getKey();
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    bestValue = val;
                    bestMove = move;
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
                node.movePieceTo(move);
                // commit one move and do further evaluation
                node_generated++;
                val = minimax_state(node, depth - 1, true, max_player_id, min_player_id).getKey();
                //System.out.printf("New value found in min node: %d\n", val);
                if(val < bestValue){
                    bestValue = val;
                    bestMove = move;
                }
                node.reverseMove();
                // early termination when a winning move is found
                if(bestValue == Double.NEGATIVE_INFINITY) {
                    break;
                }
            }
        }
        this.bestMove = bestMove;
        return new Pair(bestValue, bestMove);
    }


    public Pair<Double,Move> alphabeta_state(CheckerState node, int depth, boolean maximizing, int max_player_id, int min_player_id, double alpha, double beta) {
        ArrayList<Move> nextMoves = node.nextMoves();

        if(depth == 0 || node.gameOver() != 0 || nextMoves.size() == 0) {
            double val =  eval_func.eval_distance_and_goal(max_player_id, node)
                    -eval_func.eval_distance_and_goal(min_player_id, node);
            return new Pair(val, null);
        }
        Double val;
        double bestValue;
        Move bestMove = nextMoves.get(0);

        if(maximizing) {
            bestValue = Double.NEGATIVE_INFINITY;
            for(Move move : nextMoves) {
                node.movePieceTo(move);
                if(node_generated == 432){
                    System.out.printf("haha");
                }
                node_generated++;
                val = alphabeta_state(node, depth - 1, false, max_player_id, min_player_id, alpha, beta).getKey();
                //System.out.printf("New value found in max node: %d\n", val);
                if(val > bestValue){
                    bestMove = move;
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
                node.movePieceTo(move);
                if(node_generated == 432){
                    System.out.printf("haha");
                }
                node_generated++;
                val = alphabeta_state(node, depth - 1, true, max_player_id, min_player_id, alpha, beta).getKey();
                //System.out.printf("New value found in max node: %d\n", val);
                // update next best move
                if(val < bestValue){
                    bestMove = move;
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
        this.bestMove = bestMove;
        return new Pair(bestValue, bestMove);
    }



}
