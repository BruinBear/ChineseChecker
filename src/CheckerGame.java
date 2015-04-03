import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by JingyuLiu on 3/31/2015.
 */
public class CheckerGame {
    // to ensure no same game is generated twice.
    public static HashSet<String> m_hash = new HashSet<String>();
    protected CheckerBoard m_board;
    // indicate next player to move
    protected int m_turn = 0;
    protected int m_num_players;
    // players' pieces
    private ArrayList<ArrayList<IntPair>> m_player_pieces = new ArrayList<ArrayList<IntPair>>();


    CheckerGame(CheckerGame game) {
        this(new CheckerBoard(game.m_board), game.m_num_players);
        this.m_turn = game.m_turn;
    }

    CheckerGame(CheckerBoard board, int num_players) {
        m_board = board;
        m_num_players = num_players;
        char[][] grid = m_board.getGrid();
        for(int i=0; i<num_players; i++) {
            m_player_pieces.add(new ArrayList<IntPair>());
        }
        for(int i=0; i<m_board.getSize(); i++) {
            for(int j=0; j<m_board.getSize(); j++) {
                if( '0' <grid[i][j] && grid[i][j] <='6')
                    m_player_pieces.get(grid[i][j]-'1').add(new IntPair(i,j));
            }
        }
    }


    ArrayList<Move> singlePieceNextStates(IntPair pair) {
        ArrayList<IntPair> nextPos = m_board.pieceCanMove(pair);
        ArrayList<Move> moves = new ArrayList<Move>();
        for(int i = 0; i < nextPos.size(); i++) {
            moves.add(new Move(pair, nextPos.get(i)));
        }
        return moves;
    }

    public ArrayList<Move> singlePlayerNextStates() {
        ArrayList<Move> res = new ArrayList<Move>();
        for(IntPair piece : m_player_pieces.get(m_turn)) {
            res.addAll(singlePieceNextStates(piece));
        }
        if(res.size()==0)
            throw new IllegalStateException("No more moves can be made by player "+ (m_turn+1));
        return res;
    }

    public ArrayList<CheckerGame> nextStates() {
        ArrayList<CheckerGame> nextStates = new ArrayList<CheckerGame>();
        ArrayList<Move> moves = this.singlePlayerNextStates();
        for (Move mv : moves) {
            CheckerGame newState = new CheckerGame(this);
            char tmp = newState.m_board.m_grid[mv.piece.x][mv.piece.y];
            newState.m_board.m_grid[mv.piece.x][mv.piece.y] = '0';
            newState.m_board.m_grid[mv.move.x][mv.move.y] = tmp;
            newState.m_turn =  (newState.m_turn+1) % newState.m_num_players;
            if(!m_hash.contains(newState.toString())) {
                nextStates.add(newState);
                m_hash.add(newState.toString());
            }
        }
        return nextStates;
    }

    public boolean gameOver() {
        if(m_board.m_grid[4][4] == '1') {
            System.out.println("Player 1 wins");
            return true;
        }
        else if(m_board.m_grid[2][2] == '2') {
            System.out.println("Player 2 wins");
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return String.format("%s%d%d", m_board.toString(), m_turn, m_num_players);
    }





}
