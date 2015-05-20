import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/23/2015.
 */
public class Paranoid extends SearchAlgorithm {
    protected int max_depth;

    Paranoid(int depth) {
        max_depth = depth;
    }

    public Move nextMove(CheckerState state) {
        return paranoid(state, this.max_depth, state.m_turn).getValue();
    }

    public Move nextNodeLimitedMove(CheckerState s, int node_limit) {
        max_nodes_per_iteration = node_limit;
        current_num_nodes = 0;
        CheckerState tmp = new CheckerState(s);
        current_depth = 1;
        while (current_num_nodes < node_limit) {
            Pair<double[],Move> m = paranoidNodesLimited(s, this.current_depth, s.m_turn);
            if(m != null) {
                this.bestMove = m.getValue();
            }
            current_depth++;
        }
        current_num_nodes = 0;
        return bestMove;
    }


    // For timed task Iteratively increament level. if timer expires, early termination is possible
    public void execute_iteratively(CheckerState s) {
        CheckerState tmp = new CheckerState(s);
        current_depth = 1;
        while(current_depth<max_depth) {
            this.bestMove = paranoid(s, this.current_depth, s.m_turn).getValue();
            current_depth++;
        }
    }


    public Pair<double[],Move> paranoidNodesLimited(CheckerState node, int depth, int max_player) {
        if(current_num_nodes>max_nodes_per_iteration) {
            return null;
        }
        if(depth == 0) {
            current_num_nodes++;
            return new Pair(getTuple(node), null);
        }

        ArrayList<Move> nextMoves = node.nextOrderedMoves(false);
//        if(nextMoves.isEmpty()) {
//            current_num_nodes=Integer.MAX_VALUE;
//        }
        if(node.gameOver()!=0 || nextMoves.isEmpty()) {
            current_num_nodes++;
            return new Pair(getTuple(node), null);
        }
        double[] tuple;
        // there is at least one move
        // if no move can lead to win we need a move to play so game can end
        Move localBestMove = nextMoves.get(0);
        double bestValue;
        double[] bestTuple = getTuple(node);
        if(node.m_turn == max_player) {
            bestValue = Double.NEGATIVE_INFINITY;
            for (Move move : nextMoves) {
                node.applyMove(move);
                // commit one move and do further evaluation
                nodes_generated++;
                current_num_nodes++;
                Pair<double[], Move> pp = paranoidNodesLimited(node, depth - 1, max_player);
                if(pp!=null) {
                    tuple = pp.getKey();
                }else{
                    return null;
                }
                if (tuple[max_player] > bestValue) {
                    bestValue = tuple[max_player];
                    bestTuple = tuple;
                    localBestMove = move;
                }
                node.reverseMove();

                // early termination when a winning move is found
                if (bestValue > 10000) {
                    current_num_nodes=10000000;
                    break;
                }
            }
        } else { //working to pick the lowest for max_player
             bestValue = Double.POSITIVE_INFINITY;
            for (Move move : nextMoves) {
                node.applyMove(move);
                // commit one move and do further evaluation
                nodes_generated++;
                current_num_nodes++;
                Pair<double[], Move> pp = paranoidNodesLimited(node, depth - 1, max_player);
                if(pp!=null) {
                    tuple = pp.getKey();
                }else{
                    return null;
                }                //System.out.printf("New value found in max node: %d\n", val);
                if (tuple[max_player] < bestValue) {
                    bestValue = tuple[max_player];
                    bestTuple = tuple;
                    localBestMove = move;
                }
                node.reverseMove();

                // early termination when a winning move is found
                if (bestValue < -10000) {
                    current_num_nodes=10000000;
                    break;
                }
            }
        }
        return new Pair(bestTuple, localBestMove);
    }

    public Pair<double[],Move> paranoid(CheckerState node, int depth, int max_player) {
        ArrayList<Move> nextMoves = node.nextOrderedMoves(true);
        if(depth == 0 || node.gameOver()!=0 || nextMoves.isEmpty()) {
            return new Pair(getTuple(node), null);
        }
        double[] tuple;
        // there is at least one move
        // if no move can lead to win we need a move to play so game can end
        Move localBestMove = nextMoves.get(0);
        double bestValue;
        double[] bestTuple = getTuple(node);
        if(node.m_turn == max_player) {
            bestValue = Double.NEGATIVE_INFINITY;
            for (Move move : nextMoves) {
                node.applyMove(move);
                // commit one move and do further evaluation
                nodes_generated++;
                tuple = paranoid(node, depth - 1, max_player).getKey();
                if (tuple[max_player] > bestValue) {
                    bestValue = tuple[max_player];
                    bestTuple = tuple;
                    localBestMove = move;
                }
                node.reverseMove();

                // early termination when a winning move is found
                if (bestValue > 10000) {
                    break;
                }
            }
        } else { //working to pick the lowest for max_player
            bestValue = Double.POSITIVE_INFINITY;
            for (Move move : nextMoves) {
                node.applyMove(move);
                // commit one move and do further evaluation
                nodes_generated++;
                tuple = paranoid(node, depth - 1, max_player).getKey();
                //System.out.printf("New value found in max node: %d\n", val);
                if (tuple[max_player] < bestValue) {
                    bestValue = tuple[max_player];
                    bestTuple = tuple;
                    localBestMove = move;
                }
                node.reverseMove();

                // early termination when a winning move is found
                if (bestValue < -10000) {
                    break;
                }
            }
        }
        return new Pair(bestTuple, localBestMove);
    }
}
