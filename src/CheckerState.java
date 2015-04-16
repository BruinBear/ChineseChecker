import java.util.*;

/**
 * Created by JingyuLiu on 4/1/2015.
 */
public class CheckerState {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    protected char[][] m_grid;
    private int m_grid_size;

    // next m_turn info
    int m_turn = 0;
    // total turns info
    int m_turn_played = 0;
    protected CheckerState bestnext;
    /**
     * grid should be square
     * assume two players
     * space is wall, 0 is empty,
     * 1 is player one's pieces
     * 2 is player two's pieces
     */
    CheckerState() {
        // clone a copy of 2 player game
        m_grid_size = grid_2_s.length;
        m_grid = new char[m_grid_size][m_grid_size];
        for(int i = 0; i< m_grid_size; i++) {
            m_grid[i] = this.grid_2_s[i].clone();
        }
        // add player pieces to pool and the goal states to pool
        m_num_players = 2;
        this.setupGoals(1);
        this.scanForPlayerPiecesGoals();
    }


    /* copy constructor */
    CheckerState(CheckerState b) {
        m_grid_size = b.getSize();
        m_turn = b.m_turn;
        m_turn_played = b.m_turn_played;
        m_grid = new char[m_grid_size][m_grid_size];
        for (int i = 0; i < m_grid_size; i++) {
            m_grid[i] = b.getGrid()[i].clone();
        }
        m_num_players = 2;
        m_players_goals = b.m_players_goals;
        m_players_goal_center = b.m_players_goal_center;
    }

    public static char[][] empty_grid = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    public static char[][] grid_2 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', '2', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', '2', '2', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };
    public static char[][] grid_2_s = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', ' '},
            {' ', ' ', ' ', ' ', '1', '1', '0', '0', '0', '0', '0', '0', '0', ' ', ' '},
            {' ', ' ', ' ', ' ', '1', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '2', ' ', ' ', ' ', ' '},
            {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '2', '2', ' ', ' ', ' ', ' '},
            {' ', '0', '0', '0', '0', '0', '0', '0', '2', '2', '2', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    public static char[][] grid_2_s_goal = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '2', '2', '2', '0', '0', '0', '0', '0', '0', '0', ' '},
            {' ', ' ', ' ', ' ', '2', '2', '0', '0', '0', '0', '0', '0', '0', ' ', ' '},
            {' ', ' ', ' ', ' ', '2', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '1', ' ', ' ', ' ', ' '},
            {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '1', '1', ' ', ' ', ' ', ' '},
            {' ', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    public static char[][] grid_3 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '2', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '2', '2', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '2', '2', '2', '2', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '1', '1', '1', '0', '0', '0', '0', '0', '3', '3', '3', '3', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '1', '1', '0', '0', '0', '0', '0', '0', '3', '3', '3', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '1', '0', '0', '0', '0', '0', '0', '0', '3', '3', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '1', '0', '0', '0', '0', '0', '0', '0', '0', '3', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    protected int m_num_players;
    protected ArrayList<ArrayList<IntPair>> m_players_goals = new ArrayList<ArrayList<IntPair>>();
    protected ArrayList<ArrayList<IntPair>> m_players_pieces = new ArrayList<ArrayList<IntPair>>();

    public static ArrayList<IntPair> m_players_goal_center;

    private void setupGoals (int board_num) {
        m_players_goal_center = new ArrayList<IntPair>();
        switch(board_num) {
            case 1:
                // goal for player 1
                m_players_goal_center.add(new IntPair(10,10));
                // goal for player 2
                m_players_goal_center.add(new IntPair(4,4));
                break;
        }
    }



    // Scan goals, only done once
    private void scanForPlayerPiecesGoals() {
        for(int i=0;i<m_num_players;i++) {
            m_players_goals.add(new ArrayList<IntPair>());
            m_players_pieces.add(new ArrayList<IntPair>());
        }
        for(int i=0; i<m_grid_size; i++) {
            for(int j=0; j<m_grid_size; j++) {
                if( '0' < m_grid[i][j] && m_grid[i][j] <='6') {
                    m_players_goals.get(m_grid[i][j] - '1').add(new IntPair(m_grid_size-1 - i, m_grid_size-1 - j));
                    m_players_pieces.get(m_grid[i][j] - '1').add(new IntPair(i, j));
                }
            }
        }
        int num_pieces = m_players_goals.get(0).size();
        for(int i = 0; i < m_players_goals.size(); i++) {
            int x_sum = 0;
            int y_sum = 0;
            for(IntPair goal: m_players_goals.get(i)){
                x_sum += goal.x;
                y_sum += goal.y;
            }
        }
    }


    public void rescanPieces() {
        m_players_pieces = new ArrayList<ArrayList<IntPair>>();
        for(int i=0;i<m_num_players;i++) {
            m_players_pieces.add(new ArrayList<IntPair>());
        }
        for(int i=0; i<m_grid_size; i++) {
            for(int j=0; j<m_grid_size; j++) {
                if( '0' < m_grid[i][j] && m_grid[i][j] <='6') {
                    m_players_pieces.get(m_grid[i][j] - '1').add(new IntPair(i, j));
                }
            }
        }
    }


    // returns who wins, 0 if none wins
    public int gameOver() {
        // we only need to check if player the m_turn before has won
        int player_index = (m_turn +m_num_players-1)%m_num_players;
        boolean finished_one_piece = false;
        for(IntPair piece : m_players_goals.get(player_index)) {
            if(m_grid[piece.x][piece.y] == ('1'+ player_index)) {
                finished_one_piece = true;
            }
            if(m_grid[piece.x][piece.y] == '0') {
                return 0;
            }
        }
        if (finished_one_piece)
            return player_index+1;
        else
            return 0;
    }



    public char[][] getGrid () {
        return m_grid;
    }

    public int getSize() {
        return m_grid_size;
    }


    private void printPiece(char c) {
        switch(c) {
            case '1':
                System.out.print(ANSI_RED+c+" "+ANSI_RESET);
                break;
            case '2':
                System.out.print(ANSI_GREEN+c+" "+ANSI_RESET);
                break;
            case '3':
                System.out.print(ANSI_YELLOW+c+" "+ANSI_RESET);
                break;
            case '4':
                System.out.print(ANSI_BLUE+c+" "+ANSI_RESET);
                break;
            case '5':
                System.out.print(ANSI_PURPLE+c+" "+ANSI_RESET);
                break;
            case '6':
                System.out.print(ANSI_CYAN+c+" "+ANSI_RESET);
                break;
            default:
                System.out.print(ANSI_WHITE+c+" "+ANSI_RESET);
                break;
        }
        return;
    }

    public void printBoard() {
        // top half
        for(int i = 1; i <= m_grid_size; i++) {
            // print align spaces
            for(int j = m_grid_size-i; j >=0; j--) {
                System.out.print(" ");
            }
            // print chars total i chars to print
            for(int k = 0; k < i; k++) {
                // x, y sums to k
                printPiece(m_grid[i - 1 - k][k]);
            }
            System.out.println();
        }
        // lower half
        for(int i = m_grid_size-1; i > 0; i--) {
            // print align spaces
            for(int j = m_grid_size-i; j >=0; j--) {
                System.out.print(" ");
            }
            // print chars total i chars to print
            for(int k = 0; k < i; k++) {
                // x, y sums to k
                printPiece(m_grid[m_grid_size-1-k][k+m_grid_size-i]);
            }
            System.out.println();
        }
    }


    public List<IntPair> pieceOneStepCanReach(IntPair pair) {
        ArrayList<IntPair> res = new ArrayList<IntPair>();
        // Single move
        // up right
        if(m_grid[pair.x-1][pair.y] == '0') {
            res.add(new IntPair(pair.x-1, pair.y));
        }
        // down left
        if(m_grid[pair.x+1][pair.y] == '0') {
            res.add(new IntPair(pair.x+1, pair.y));
        }
        // up left
        if(m_grid[pair.x][pair.y-1] == '0') {
            res.add(new IntPair(pair.x, pair.y-1));
        }
        // down right
        if(m_grid[pair.x][pair.y+1] == '0') {
            res.add(new IntPair(pair.x, pair.y+1));
        }
        // left
        if(m_grid[pair.x+1][pair.y-1] == '0') {
            res.add(new IntPair(pair.x+1, pair.y-1));
        }
        // right
        if(m_grid[pair.x-1][pair.y+1] == '0') {
            res.add(new IntPair(pair.x-1, pair.y+1));
        }
        return res;
    }


    /*
       Given a IntPair where a piece is return a list of places reacheable through jumps
       searched through BFS because depth is limited by grid size
    */
    public ArrayList<IntPair> pieceJumpCanReach(IntPair root) {
        Set<IntPair> visited = new HashSet<IntPair>();
        Queue<IntPair> frontier = new LinkedList<IntPair>();
        frontier.add(root);
        while (!frontier.isEmpty()) {
            IntPair toExpand = frontier.remove();
            if (toExpand != root) {
                visited.add(toExpand);
            }
            // append next level jump
            int x = toExpand.x;
            int y = toExpand.y;
            if ('0' < m_grid[x - 1][y] && m_grid[x - 1][y] <= '6' //a piece in the middle
                    && m_grid[x - 2][y] == '0' //a empty space over the piece
                    && !visited.contains(new IntPair(x - 2, y))) {
                frontier.add(new IntPair(x - 2, y));
            }
            if ('0' < m_grid[x + 1][y] && m_grid[x + 1][y] <= '6'
                    && m_grid[x + 2][y] == '0'
                    && !visited.contains(new IntPair(x + 2, y))) {
                frontier.add(new IntPair(x + 2, y));
            }
            if ('0' < m_grid[x][y - 1] && m_grid[x][y - 1] <= '6'
                    && m_grid[x][y - 2] == '0'
                    && !visited.contains(new IntPair(x, y - 2))) {
                frontier.add(new IntPair(x, y - 2));
            }
            if ('0' < m_grid[x][y + 1] && m_grid[x][y + 1] <= '6'
                    && m_grid[x][y + 2] == '0'
                    && !visited.contains(new IntPair(x, y + 2))) {
                frontier.add(new IntPair(x, y + 2));
            }
            if ('0' < m_grid[x + 1][y - 1] && m_grid[x + 1][y - 1] <= '6'
                    && m_grid[x + 2][y - 2] == '0'
                    && !visited.contains(new IntPair(x + 2, y - 2))) {
                frontier.add(new IntPair(x + 2, y - 2));
            }
            if ('0' < m_grid[x - 1][y + 1] && m_grid[x - 1][y + 1] <= '6'
                    && m_grid[x - 2][y + 2] == '0'
                    && !visited.contains(new IntPair(x - 2, y + 2))) {
                frontier.add(new IntPair(x - 2, y + 2));
            }
        }
        return new ArrayList<IntPair>(visited);
    }


    /*
    Give a merged result of next move for a piece
     */
    public ArrayList<IntPair> pieceCanMove(IntPair pair) {
        ArrayList<IntPair> res = new ArrayList<IntPair>(this.pieceOneStepCanReach(pair));
        res.addAll(this.pieceJumpCanReach(pair));
        return res;
    }


    public ArrayList<CheckerState> nextStates() {
        ArrayList<CheckerState> nextStates = new ArrayList<CheckerState>();
        // no more expansion
        if(this.gameOver() != 0) {
            return nextStates;
        }

        for (IntPair piece : m_players_pieces.get(m_turn)) {
            for (IntPair dest : pieceCanMove(piece)) {
                // A new state
                CheckerState newState = new CheckerState(this);
                newState.m_grid[dest.x][dest.y] = newState.m_grid[piece.x][piece.y];
                newState.m_grid[piece.x][piece.y] = '0';
                newState.rescanPieces();
                newState.m_turn = (newState.m_turn+1) % m_num_players;
                newState.m_turn_played++;
                nextStates.add(newState);
            }
        }
        return nextStates;
    }

    public boolean movePieceTo(IntPair piece, IntPair destination) {
        if ('0' < m_grid[piece.x][piece.y] &&
                m_grid[piece.x][piece.y] <= '6' &&
                m_grid[destination.x][destination.y] == '0') {
            char tmp = m_grid[piece.x][piece.y];
            m_grid[piece.x][piece.y] = '0';
            m_grid[destination.x][destination.y] = tmp;
            return true;
        }
        else
            return false;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < m_grid_size; i++) {
            for (int j = 0; j < m_grid_size; j++) {
                b.append(m_grid[i][j]);
            }
            b.append("\n");
        }
        return b.toString();
    }
}
