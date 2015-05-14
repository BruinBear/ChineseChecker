import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/23/2015.
 */
public class Maxn extends SearchAlgorithm {
    protected static Evaluation eval_func = new Evaluation();
    protected int max_depth;

    Maxn(int depth) {
        max_depth = depth;
    }

    public Move nextMove(CheckerState state) {
        return maxn(state, this.max_depth).getValue();
    }


    // For timed task Iteratively increament level. if timer expires, early termination is possible
    public void execute_iteratively(CheckerState s) {
        CheckerState tmp = new CheckerState(s);
        current_depth = 1;
        while(current_depth<max_depth) {
            this.bestMove = maxn(s, this.current_depth).getValue();
            current_depth++;
        }
    }


    protected double[] getTuple(CheckerState s) {
        double[] n_tuple = new double[s.m_num_players];
        for(int i=0; i<s.m_num_players; i++) {
            n_tuple[i] = eval_func.eval_distance_and_goal(i, s);
        }
        return n_tuple;
    }


    public Pair<double[],Move> maxn(CheckerState node, int depth) {
        int max_player_id = node.m_turn;
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
        bestValue = Double.NEGATIVE_INFINITY;
        for(Move move : nextMoves) {
            node.applyMove(move);
            // commit one move and do further evaluation
            nodes_generated++;
            tuple = maxn(node, depth - 1).getKey();
            //System.out.printf("New value found in max node: %d\n", val);
            if(tuple[max_player_id] > bestValue){
                bestValue = tuple[max_player_id];
                bestTuple = tuple;
                localBestMove = move;
            }
            node.reverseMove();

            // early termination when a winning move is found
            if(bestValue > 10000) {
                break;
            }
        }

        return new Pair(bestTuple, localBestMove);
    }
}
