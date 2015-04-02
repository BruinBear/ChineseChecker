import java.util.*;

/**
 * Created by JingyuLiu on 3/31/2015.
 */
public class Board {
    private int [][] m_grid;
    private int m_grid_size;
    private int m_num_stone;
    private IntPair[] m_player2_pieces;

    Board(int grid_size, int stone_size){
        if(grid_size/2 < stone_size) {
                throw new IllegalArgumentException("Bad input game size");
        }
        m_grid_size = grid_size;
        m_num_stone = stone_size;

        //initialize grid
        m_grid  = new int[m_grid_size][m_grid_size];
        m_player2_pieces = new IntPair[m_num_stone*m_num_stone];

        //initialize pieces
        for(int i=0; i<m_num_stone; i++) {
            for(int j=0; j<m_num_stone; j++) {
                m_grid[i][j] = 1;
                m_grid[4-i][4-j] = 2;
                m_player2_pieces[i+j] = new IntPair(4-i, 4-j);
            }
        }
        System.out.println("board initialized");
    }

    /*
    for testing
     */
    protected void setBoard(int [][] arr) {
        m_grid_size = arr.length;
        m_grid = arr;
    }

    public List<IntPair> pieceOneStepCanReach(IntPair pair) {
        ArrayList<IntPair> res = new ArrayList<IntPair>();
        // Single move
        // down
        if(pair.x != 0 && m_grid[pair.x-1][pair.y] == 0) {
            res.add(new IntPair(pair.x-1, pair.y));
        }
        // up
        if(pair.x != m_grid_size-1 && m_grid[pair.x+1][pair.y] == 0) {
            res.add(new IntPair(pair.x+1, pair.y));
        }
        // left
        if(pair.y != 0 && m_grid[pair.x][pair.y-1] == 0) {
            res.add(new IntPair(pair.x, pair.y-1));
        }
        // right
        if(pair.y != m_grid_size-1 && m_grid[pair.x][pair.y+1] == 0) {
            res.add(new IntPair(pair.x, pair.y+1));
        }
        // side right
        if(pair.x != m_grid_size-1 && pair.y != 0 && m_grid[pair.x+1][pair.y-1] == 0) {
            res.add(new IntPair(pair.x+1, pair.y-1));
        }
        // side left
        if(pair.x != 0 && pair.y != m_grid_size-1 && m_grid[pair.x-1][pair.y+1] == 0) {
            res.add(new IntPair(pair.x-1, pair.y+1));
        }
        return res;
    }

    /*
     Given a IntPair where a piece is return a list of places reacheable through jumps
     searched through BFS because depth is limited by grid size
      */
    public List<IntPair> pieceJumpCanReach(IntPair root) {
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
            if (x > 1 && m_grid[x - 1][y] != 0 && m_grid[x - 2][y] == 0
                    && !visited.contains(new IntPair(x - 2, y))) {
                frontier.add(new IntPair(x - 2, y));
            }
            if (x < m_grid_size - 2 && m_grid[x + 1][y] != 0 && m_grid[x + 2][y] == 0
                    && !visited.contains(new IntPair(x + 2, y))) {
                frontier.add(new IntPair(x + 2, y));
            }
            if (y > 1 && m_grid[x][y - 1] != 0 && m_grid[x][y - 2] == 0
                    && !visited.contains(new IntPair(x, y - 2))) {
                frontier.add(new IntPair(x, y - 2));
            }
            if (y < m_grid_size - 2 && m_grid[x][y + 1] != 0 && m_grid[x][y + 2] == 0
                    && !visited.contains(new IntPair(x, y + 2))) {
                frontier.add(new IntPair(x, y + 2));
            }
            if (x < m_grid_size -2 && y > 1 && m_grid[x + 1][y - 1] != 0 && m_grid[x + 2][y - 2] == 0
                    && !visited.contains(new IntPair(x + 2, y - 2))) {
                frontier.add(new IntPair(x + 2, y - 2));
            }
            if (y < m_grid_size -2 && x > 1 && m_grid[x - 1][y + 1] != 0 && m_grid[x - 2][y + 2] == 0
                    && !visited.contains(new IntPair(x - 2, y + 2))) {
                frontier.add(new IntPair(x - 2, y + 2));
            }
        }
        return new ArrayList<IntPair>(visited);
    }


    public void printBoard() {
        System.out.println("board configuration");
        int[] oneDimension = new int[m_grid_size*m_grid_size];
        for(int i = 0; i < m_grid_size*m_grid_size; i++) {
            oneDimension[i] = m_grid[i%m_grid_size][i/m_grid_size];
        }
        int count = 0;
        // print diagonally
        for(int i = 1; i <= m_grid_size; i++) { //i is the size to print
            // align
            for(int j = m_grid_size-i; j >=0; j--) {
                System.out.print(" ");
            }
            count = printArray(oneDimension, count, i);
            System.out.println();
        }

        for(int i = m_grid_size-1; i >=1 ; i--) { // i is the size to print
            // align
            for(int j = m_grid_size-i; j >=0; j--) {
                System.out.print(" ");
            }
            count = printArray(oneDimension, count, i);
            System.out.println();
        }
    }

    private int printArray(int[] arr, int start, int size) {
        if(start+size <= arr.length) {
            for (int i = start; i < start + size; i++){
                System.out.print(arr[i]);
                System.out.print(" ");
            }
        }
        return start+size;
    }
    void printComputerPieces(){

    }

}

