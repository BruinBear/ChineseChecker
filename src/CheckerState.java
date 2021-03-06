import java.awt.*;
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
    private BoardConfiguration board_config = new BoardConfiguration();
    // next player to play, e.g. 0 means player 1 plays next
    int m_turn = 0;
    // total turns info
    int m_turn_played = 0;

    // move stack, most recent move is on top, importatn for game history
    // saves memory and skip clone action of arrays
    Stack<Move> m_move_stack = new Stack<Move>();


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
        m_grid_size = board_config.grid_2_s.length;
        m_grid = new char[m_grid_size][m_grid_size];
        for(int i = 0; i< m_grid_size; i++) {
            m_grid[i] = board_config.grid_2_s[i].clone();
        }
        // add player pieces to pool and the goal states to pool
        m_num_players = 2;
        this.setupGoals(1);
        this.scanForPlayerPiecesGoals();
    }

    CheckerState(int number_of_player) {
        // clone a copy of 2 player game
        m_grid_size = board_config.grid_3_s.length;
        m_grid = new char[m_grid_size][m_grid_size];
        for(int i = 0; i< m_grid_size; i++) {
            m_grid[i] = board_config.grid_3_s[i].clone();
        }
        // add player pieces to pool and the goal states to pool
        m_num_players = 3;
        this.setupGoals(2);
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
        m_num_players = 3;
        m_players_goals = b.m_players_goals;
        m_players_far_goal = b.m_players_far_goal;
        m_players_pieces = new ArrayList<ArrayList<IntPair>>();
        m_move_stack = new Stack<Move>();
        m_move_stack.addAll(b.m_move_stack);
        rescanPieces();
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
//            0    1    2    3    4    5    6    7    8    9    10   11   12   13   14
/*0*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*1*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' '},
/*2*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' '},
/*3*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' '},
/*4*/       {' ', ' ', ' ', ' ', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', ' '},
/*5*/       {' ', ' ', ' ', ' ', '1', '1', '0', '0', '0', '0', '0', '0', '0', ' ', ' '},
/*6*/       {' ', ' ', ' ', ' ', '1', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' '},
/*7*/       {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
/*8*/       {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '2', ' ', ' ', ' ', ' '},
/*9*/       {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '2', '2', ' ', ' ', ' ', ' '},
/*10*/      {' ', '0', '0', '0', '0', '0', '0', '0', '2', '2', '2', ' ', ' ', ' ', ' '},
/*11*/      {' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*12*/      {' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*13*/      {' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*14*/      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    public static char[][] grid_3_s = new char[][]{
//            0    1    2    3    4    5    6    7    8    9    10   11   12   13   14
/*0*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*1*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' '},
/*2*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' '},
/*3*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' '},
/*4*/       {' ', ' ', ' ', ' ', '1', '1', '1', '0', '0', '0', '0', '2', '2', '2', ' '},
/*5*/       {' ', ' ', ' ', ' ', '1', '1', '0', '0', '0', '0', '0', '2', '2', ' ', ' '},
/*6*/       {' ', ' ', ' ', ' ', '1', '0', '0', '0', '0', '0', '0', '2', ' ', ' ', ' '},
/*7*/       {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
/*8*/       {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
/*9*/       {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
/*10*/      {' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
/*11*/      {' ', ' ', ' ', ' ', '3', '3', '3', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*12*/      {' ', ' ', ' ', ' ', '3', '3', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*13*/      {' ', ' ', ' ', ' ', '3', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
/*14*/      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
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

    public static ArrayList<IntPair> m_players_far_goal;

    private void setupGoals (int board_num) {
        m_players_far_goal = new ArrayList<IntPair>();
        switch(board_num) {
            case 1:
                // goal for player 1
                m_players_far_goal.add(new IntPair(10, 10));
                // goal for player 2
                m_players_far_goal.add(new IntPair(4, 4));
                break;
            case 2:
                // goal for player 1
                m_players_far_goal.add(new IntPair(10, 10));
                // goal for player 2
                m_players_far_goal.add(new IntPair(10, 1));
                // goal for player 3
                m_players_far_goal.add(new IntPair(1, 10));
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
        int player_index = 0;
        while(player_index < m_num_players) {
            boolean finished = false;
            for (IntPair piece : m_players_goals.get(player_index)) {
                if (m_grid[piece.x][piece.y] == ('1' + player_index)) {
                    finished = true;
                }
                if (m_grid[piece.x][piece.y] == '0') {
                    finished = false;
                    break;
                }
            }
            if (finished)
                return player_index + 1;
            else
                player_index++;
        }
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
                System.out.print(ANSI_RED + c + " " + ANSI_RESET);
                break;
            case '2':
                System.out.print(ANSI_GREEN + c + " " + ANSI_RESET);
                break;
            case '3':
                System.out.print(ANSI_YELLOW + c + " " + ANSI_RESET);
                break;
            case '4':
                System.out.print(ANSI_BLUE + c + " " + ANSI_RESET);
                break;
            case '5':
                System.out.print(ANSI_PURPLE + c + " " + ANSI_RESET);
                break;
            case '6':
                System.out.print(ANSI_CYAN + c + " " + ANSI_RESET);
                break;
            default:
                System.out.print(ANSI_WHITE + c + " " + ANSI_RESET);
                break;
        }
        return;
    }

    private void printPieceOnGraphics(char c, Graphics g, int gx, int gy, int radius, int x, int y) {
        switch(c) {
            case '0':
                g.setColor(Color.white);
                break;
            case '1':
                g.setColor(Color.red);
                break;
            case '2':
                g.setColor(Color.green);
                break;
            case '3':
                g.setColor(Color.yellow);
                break;
            case '4':
                g.setColor(Color.blue);
                break;
            case '5':
                g.setColor(Color.pink);
                break;
            case '6':
                g.setColor(Color.cyan);
                break;
            default:
                return;
        }
        g.fillOval(gx, gy, radius, radius);
        g.setColor(Color.black);
        g.drawString(String.format("%d,%d", x, y), gx, gy);
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
                int x = i-1-k;
                int y = k;
                printPiece(m_grid[x][y]);
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
                printPiece(m_grid[m_grid_size - 1 - k][k + m_grid_size - i]);
            }
            System.out.println();
        }
    }


    public void printBoardWithPosition() {
//        // top half
//        for(int i = 1; i <= m_grid_size; i++) {
//            // print align spaces
//            for(int j = m_grid_size-i; j >=0; j--) {
//                System.out.print("     ");
//            }
//            // print chars total i chars to print
//            for(int k = 0; k < i; k++) {
//                // x, y sums to k
//                int x,y;
//                x = i-1-k;
//                y = k;
//                if(m_grid[x][y]== ' ')
//                    System.out.print("      ");
//                else
//                    System.out.printf(new IntPair(x, y).toString());
//                printPiece(m_grid[x][y]);
//            }
//            System.out.println();
//            System.out.println();
//        }
//        // lower half
//        for(int i = m_grid_size-1; i > 0; i--) {
//            // print align spaces
//            for(int j = m_grid_size-i; j >=0; j--) {
//                System.out.print("     ");
//            }
//            // print chars total i chars to print
//            for(int k = 0; k < i; k++) {
//                // x, y sums to k
//                int x,y;
//                x = m_grid_size-1-k;
//                y = k+m_grid_size-i;
//                if(m_grid[x][y]== ' ')
//                    System.out.print("     ");
//                else
//                    System.out.print(new IntPair(x, y).toString());
//                printPiece(m_grid[x][y]);
//            }
//            System.out.println();
//            System.out.println();
//        }
    }


    public HashMap<IntPair, PieceShape> getGraphicsConfiguration() {
        HashMap<IntPair, PieceShape> board_conf = new HashMap<IntPair, PieceShape>();
        // top half
        int x, y;
        int gxStep = 30;
        int gxSpace = 15;
        int gyStep = 30;
        int oval_size = 18;
        int gy = 40;
        for(int i = 8; i <= m_grid_size; i++) {
            // print align spaces
            int gx = 40;
            for(int j = m_grid_size-i; j>0; j--) {
                gx+=gxSpace;
            }
            // print chars total i chars to print
            for(int k = 0; k < i; k++) {
                // x, y sums to k
                x = i-1-k;
                y = k;
                if(m_grid[x][y] != ' ')
                    board_conf.put(new IntPair(x,y) , new PieceShape(gx,gy,oval_size,oval_size,m_grid[x][y]));
                gx += gxStep;
            }
            gy += gyStep;
        }
        // lower half
        for(int i = m_grid_size-1; i > 7; i--) {
            int gx = 40;
            // print align spaces
            for(int j = m_grid_size-i; j>0; j--) {
                gx += gxSpace;
            }
            // print chars total i chars to print
            for(int k = 0; k < i; k++) {
                x = m_grid_size - 1 - k;
                y = k + m_grid_size - i;
                if(m_grid[x][y] != ' ')
                    board_conf.put(new IntPair(x, y), new PieceShape(gx, gy, oval_size, oval_size, m_grid[x][y]));
                gx += gxStep;
            }
            gy += gyStep;
        }
        return board_conf;
    }

    public void printBoardOnGraphics(Graphics g) {
        // top half
        int x, y;
        int gxStep = 30;
        int gxSpace = 15;
        int gyStep = 30;
        int oval_size = 18;
        int gy = 0;
        for(int i = 8; i <= m_grid_size; i++) {
            // print align spaces
            int gx = 0;
            for(int j = m_grid_size-i; j>0; j--) {
                gx+=gxSpace;
            }
            // print chars total i chars to print
            for(int k = 0; k < i; k++) {
                // x, y sums to k
                x = i-1-k;
                y = k;
                printPieceOnGraphics(m_grid[x][y], g, gx, gy, oval_size, x, y);
                gx += gxStep;
//                printPiece(m_grid[x][y]);
            }
            gy += gyStep;
        }
        // lower half
        for(int i = m_grid_size-1; i > 7; i--) {
            int gx = 0;
            // print align spaces
            for(int j = m_grid_size-i; j>0; j--) {
                gx += gxSpace;
            }
            // print chars total i chars to print
            for(int k = 0; k < i; k++) {
                x = m_grid_size - 1 - k;
                y = k + m_grid_size - i;
                printPieceOnGraphics(m_grid[x][y], g, gx, gy, oval_size, x, y);
                gx += gxStep;
            }
            gy += gyStep;
        }
    }


    public ArrayList<IntPair> pieceOneStepCanReach(IntPair pair) {
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
        IntPair tmp = new IntPair(root);
        frontier.add(root);
        visited.add(root);
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
        visited.remove(root);
        return new ArrayList<IntPair>(visited);
    }


    /**
     * Give a merged result of next move for a piece
     */
    public ArrayList<IntPair> pieceCanMove(IntPair pair) {
        ArrayList<IntPair> res = new ArrayList<IntPair>(this.pieceOneStepCanReach(pair));
        res.addAll(this.pieceJumpCanReach(pair));
        return res;
    }


    /**
     * Generate all possible moves
     * we can potentially sort the moves based on some score
     * and we will
     */
    public ArrayList<Move> nextUnorderedMoves() {
        ArrayList<Move> nextMoves = new ArrayList<Move> ();
        if(gameOver() != 0) {return nextMoves;}
        for (IntPair piece : m_players_pieces.get(m_turn)) {
            for (IntPair dest : pieceCanMove(piece)) {
                nextMoves.add(new Move(piece,dest));
            }
        }
        Collections.shuffle(nextMoves);
        return nextMoves;
    }


    public ArrayList<Move> nextOrderedMoves(boolean nonnegative) {
        ArrayList<Move> nextMoves = new ArrayList<Move> ();
        if(gameOver() != 0) {
            return nextMoves;
        }
        for (IntPair piece : m_players_pieces.get(m_turn)) {
            for (IntPair dest : pieceCanMove(piece)) {
                Move new_move = new Move(new Move(piece,dest), m_players_far_goal.get(m_turn));
                if(!nonnegative || new_move.improvement>=0) {
                    nextMoves.add(new_move);
                }
            }
        }
        Collections.shuffle(nextMoves);
        Collections.sort(nextMoves, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                return Integer.valueOf(o2.improvement).compareTo(o1.improvement);
            }
        });
        return nextMoves;
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
                newState.m_turn = (newState.m_turn + 1) % m_num_players;
                newState.m_turn_played++;
                nextStates.add(newState);
            }
        }
        return nextStates;
    }


    /**
     *    Commit a move that increases turn number
     *    Also important make inverse possible, to pop a move action off stack
     */
    public boolean applyMove(Move mv) {
        IntPair piece = mv.piece;
        IntPair dest = mv.dest;
        if ('0' < m_grid[piece.x][piece.y] &&
                m_grid[piece.x][piece.y] <= '6' &&
                m_grid[dest.x][dest.y] == '0') {
            // swaping a piece to new blank, no tmp needed because dest is 0
            m_grid[dest.x][dest.y] = m_grid[piece.x][piece.y];
            m_grid[piece.x][piece.y] = '0';
            m_move_stack.push(new Move(piece, dest));
            // next player play next turn
            m_turn = (m_turn+1)%m_num_players;
            m_turn_played++;
            this.rescanPieces();
            return true;
        }
        else
            return false;
    }


    public Move getRandomMove() {
        ArrayList<Move> moves = nextOrderedMoves(true);
        if(moves.size() == 0)
            return null;
        int random = (int) (Math.random()*moves.size());
        return moves.get(random);
    }

    /**
     * epsilon greedy move selection
     * 95% farthest move, 5% random moep  */
    public Move epsilonMove() {
        ArrayList<Move> moves = nextOrderedMoves(true);
        if(moves.size() == 0)
            return null;
        double rand = Math.random();
        if(rand<=0.95) {
            return moves.remove(0);
        } else {
            int index = (int) Math.floor(Math.random()*moves.size());
            return moves.get(index);
        }
    }

    
    private double[] mapImprovementToProbability(ArrayList<Move> moves) {
        double total_weight = 0;
        for(Move m : moves) {
            total_weight += m.improvement+1;
        }
        double[] prob = new double[moves.size()];
        double last = 1.0;
        for(int i=0; i<moves.size(); i++) {
            prob[i] = (moves.get(i).improvement+1) / total_weight;
            last = 1 - prob[i];
        }
        prob[moves.size()-1] = last;
        return prob;
    }




    /**
     * Create a copy of the current state and apply move
     * @param mv
     * @return
     */
    public CheckerState newAndApply(Move mv) {
        CheckerState newState = new CheckerState(this);
        newState.applyMove(mv);
        return newState;
    }


    /**
     *    Commit a move that increases turn number
     *    Also important make inverse possible, to pop a move action off stack
     */
    public boolean reverseMove() {
        // if the state doesn't have previous move
        if(m_move_stack.isEmpty()) {
            return false;
        }
        Move mv = m_move_stack.pop();
        IntPair piece = mv.piece;
        IntPair dest = mv.dest;
        // we can assume a move is valid, so checking is not necessary
        // piece char
        m_grid[piece.x][piece.y] = m_grid[dest.x][dest.y];
        m_grid[dest.x][dest.y] = '0';
        // next player play next turn
        m_turn = (m_turn-1+m_num_players)%m_num_players;
        m_turn_played--;
        this.rescanPieces();
        return true;
    }


    public Move getLastMove() {
        return m_move_stack.peek();
    }

    public double[] getReward() {
        double[] re = new double[m_num_players];
        if(gameOver()!= 0)
            re[gameOver()-1] = 1.0;
        return re;
    }


    private void advanceTurn() {
        m_turn = (m_turn+1)/m_num_players;
        m_turn_played++;
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


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof CheckerState)) {
            return false;
        }

        CheckerState c = (CheckerState) o;

        return c.toString() == this.toString() && c.m_turn == this.m_turn;
    }

}
