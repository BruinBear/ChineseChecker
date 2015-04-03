import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class Main {



    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!");
        char[][] testBoard = new char[][]{
                {' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', '0', ' ', ' '},
                {' ', ' ', '1', '0', '0', '0', ' '},
                {' ', ' ', '0', '0', '0', ' ', ' '},
                {' ', '0', '0', '0', '2', ' ', ' '},
                {' ', ' ', '0', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' '},

        };
        CheckerBoard b = new CheckerBoard(testBoard);
        b.printBoard();

        CheckerGame g = new CheckerGame(b, 2);
        Stack<CheckerGame> gameStack = new Stack<CheckerGame>();
//        HashSet<String> hash = new HashSet<String>();

        gameStack.add(g);

        if (winnerOne(g))
            System.out.println("Player one has winning strategy");
        else
            System.out.println("Player one doesn't have winning strategy");

//
//        while(!gameStack.isEmpty()) {
//            CheckerGame toplay = gameStack.pop();
////            hash.add(toplay.toString());
//            toplay.m_board.printBoard();
//            System.out.printf("Player %d's turn\n", toplay.m_turn+1);
//            if (toplay.gameOver())
//                break;
//            else {
//                ArrayList<CheckerGame> checkerGames = toplay.nextStates();
//                for (CheckerGame newState : checkerGames) {
////                    if(!hash.contains(newState.toString()))
//                        gameStack.add(newState);
//                }
//            }
//        }

    }
    public static boolean winnerOne(CheckerGame g) {
        if(g.m_board.m_grid[4][4] == '1' && g.m_turn == 1) {
            g.m_board.printBoard();
            return true;
        } else {
            if(g.m_turn == 0) {
                for (CheckerGame checkerGame : g.nextStates()) {
                    if(winnerOne(checkerGame)) {
                        return true;
                    }
                }
                return false;
            }
            // assume two turns
            else {
                for (CheckerGame checkerGame : g.nextStates()) {
                    if(!winnerOne(checkerGame)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }


    public static boolean buildWinningTreeOone(CheckerGame g, GameTree root) {
        if(g.m_board.m_grid[4][4] == '1' && g.m_turn == 1) {
            g.m_board.printBoard();
            return true;
        } else {
            if(g.m_turn == 0) {
                for (CheckerGame checkerGame : g.nextStates()) {
                    if(buildWinningTreeOone(checkerGame, root)) {
                        root.addChild(new GameTree(checkerGame));
                        return true;
                    }
                }
                return false;
            }
            // assume two turns
            else {
                for (CheckerGame checkerGame : g.nextStates()) {
                    if(!winnerOne(checkerGame)) {
                        return false;
                    } else {

                    }
                }
                return true;
            }
        }
    }

}
