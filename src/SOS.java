
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


    public Move nextNodeLimitedMove(CheckerState s, int node_limit) {
        current_num_nodes = 0;
        CheckerState tmp = new CheckerState(s);
        current_depth = 1;
        while (current_num_nodes < node_limit) {
            this.bestMove = sos(s, this.current_depth).getValue();
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
            this.bestMove = sos(s, current_depth).getValue();
            current_depth++;
        }
    }

    public Pair<double[],Move> sos(CheckerState node, int depth) {
        int max_player_id = node.m_turn;
        ArrayList<Move> nextMoves = node.nextOrderedMoves(true);
        if(depth == 0 || node.gameOver()!=0 || nextMoves.isEmpty()) {
            double[] perceived_util = multiplySo(getTuple(node), social_orientation);
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
            current_num_nodes++;
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
