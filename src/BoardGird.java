import java.util.*;

/**
 * Created by JingyuLiu on 4/1/2015.
 */
public class BoardGird {
    private char[][] m_grid;
    private int m_grid_size;
    /*
    grid should be square
    assume two players
    space is wall, 0 is empty,
    1 is player one's pieces
    2 is player two's pieces
    */
    BoardGird(char[][] g) {
        m_grid = g ;
        m_grid_size = g.length;
    }

    public void printBoard() {
        System.out.println("Printing board configuration");
        // top half
        for(int i = 1; i <= m_grid_size; i++) {
            // print align spaces
            for(int j = m_grid_size-i; j >=0; j--) {
                System.out.print(" ");
            }
            // print chars total i chars to print
            for(int k = 0; k < i; k++) {
                // x, y sums to k
                System.out.printf("%c ", m_grid[i-1-k][k]);
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
                System.out.printf("%c ", m_grid[m_grid_size-1-k][k+m_grid_size-i]);
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
    
}
