import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/23/2015.
 */
public class Maxn extends SearchAlgorithm {
    protected int node_expanded = 0;
    protected static Evaluation eval_func = new Evaluation();
    protected int max_depth;

    Maxn(int depth) {
        max_depth = depth;
    }

    public Move nextMove(CheckerState state) {
        return maxn(state, this.max_depth).getValue();
    }

    private Double[] getTuple(CheckerState s) {
        Double[] n_tuple = new Double[s.m_num_players];
        for(int i=0; i<s.m_num_players; i++) {
            n_tuple[i] = eval_func.eval_distance_and_goal(i, s);
        }
        if(n_tuple[0] == null){
            int k = 0;
        }
        return n_tuple;
    }


    public Pair<Double[],Move> maxn(CheckerState node, int depth) {
        int max_player_id = node.m_turn;
        ArrayList<Move> nextMoves = node.nextUnorderedMoves();
        if(depth == 0 || node.gameOver()!=0 || nextMoves.isEmpty()) {
            return new Pair(getTuple(node), null);
        }
        Double[] tuple;
        // there is at least one move
        // if no move can lead to win we need a move to play so game can end
        Move localBestMove = nextMoves.get(0);
        double bestValue;
        Double[] bestTuple = getTuple(node);
        bestValue = Double.NEGATIVE_INFINITY;
        for(Move move : nextMoves) {
            node.applyMove(move);
            // commit one move and do further evaluation
            node_expanded++;
            tuple = maxn(node, depth - 1).getKey();
            //System.out.printf("New value found in max node: %d\n", val);
            if(tuple[max_player_id] > bestValue){
                bestValue = tuple[max_player_id];
                bestTuple = tuple;
                localBestMove = move;
            }
            node.reverseMove();

            // early termination when a winning move is found
            if(bestValue == Double.POSITIVE_INFINITY) {
                break;
            }
        }

        return new Pair(bestTuple, localBestMove);
    }
}
