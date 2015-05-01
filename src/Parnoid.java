import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/23/2015.
 */
public class Parnoid extends SearchAlgorithm {
    protected int max_depth;

    Parnoid(int depth) {
        max_depth = depth;
    }

    public Move nextMove(CheckerState state) {
        return paranoid(state, this.max_depth, state.m_turn).getValue();
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
