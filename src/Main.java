import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!");
        char[][] testBoard = new char[][]{
                {' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', '0', '2', '0', ' '},
                {' ', ' ', '1', '1', '1', '3', ' '},
                {' ', '0', '1', '1', '1', '0', ' '},
                {' ', '6', '1', '1', '4', '1', ' '},
                {' ', '0', '5', '0', ' ', '0', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' '},

        };
        BoardGird b = new BoardGird(testBoard);
        List level1 = b.pieceOneStepCanReach(new IntPair(3,3));
        b.printBoard();
        List<IntPair> a = b.pieceJumpCanReach(new IntPair(3, 3));
    }
}
