
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/30/2015.
 */
public class SOS extends SearchAlgorithm{
    protected int max_depth;
    protected double[][] social_orientation;

    SOS(int depth , double[][] so) {
        max_depth = depth;
        social_orientation = so;
    }

    public Move nextMove(CheckerState state) {
        return sos(state, this.max_depth).getValue();
    }

    /**
     * Compute the transformed utility value from Social Orientation Matrix
     * @param tuple
     * @return
     */
    private double[] multiplySo(double[] tuple) {
        double[] transformed = new double[tuple.length];
        for(int i = 0; i<social_orientation.length; i++) {
            for(int j = 0; j<social_orientation[0].length; j++) {
                transformed[i] += social_orientation[i][j] * tuple[j];
            }
        }
        return transformed;
    }


    public Pair<double[],Move> sos(CheckerState node, int depth) {
        int max_player_id = node.m_turn;
        ArrayList<Move> nextMoves = node.nextOrderedMoves(true);
        if(depth == 0 || node.gameOver()!=0 || nextMoves.isEmpty()) {
            double[] perceived_util = multiplySo(getTuple(node));
            return new Pair(perceived_util, null);
        }
        double[] tuple;
        // there is at least one move
        // if no move can lead to win we need a move to play so game can end
        Move localBestMove = nextMoves.get(0);
        double bestValue;
        double[] bestTuple = getTuple(node);
        bestValue = Double.NEGATIVE_INFINITY;
        for(Move move : nextMoves) {
            node.applyMove(move);
            // commit one move and do further evaluation
            nodes_generated++;
            tuple = sos(node, depth - 1).getKey();
            //System.out.printf("New value found in max node: %d\n", val);
            if(tuple[max_player_id] > bestValue){
                bestValue = tuple[max_player_id];
                bestTuple = tuple;
                localBestMove = move;
            }
            node.reverseMove();

            // early termination when a winning move is found
            if(bestValue >= 10000) {
                break;
            }
        }

        return new Pair(bestTuple, localBestMove);
    }

}
