import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main{

    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!\n");

        CheckerState b = new CheckerState();
//        b.printBoard();
//        System.out.printf("Player one eval to %d!\n", b.eval_distance_and_goal(0));
//        System.out.printf("Player two eval to %d!\n", b.eval_distance_and_goal(1));
        Scanner sc = new Scanner(System.in);
//
        TimedGameSimulation simulation1 = new TimedGameSimulation(b, 5000, 4);
        simulation1.startGame();
//        DepthGameSimulation simulation2 = new DepthGameSimulation(b, 3);
//        simulation2.startGame();

//        b.m_grid = new char[][]{
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '2', '2', '0', '0', '0', '0', '0', '0', '0', ' '},
//                {' ', ' ', ' ', ' ', '2', '0', '2', '2', '0', '0', '0', '0', '0', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '0', '0', '2', '0', '0', '0', '0', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '1', '0', ' ', ' ', ' ', ' '},
//                {' ', ' ', '0', '0', '0', '0', '0', '0', '0', '1', '1', ' ', ' ', ' ', ' '},
//                {' ', '0', '0', '0', '0', '0', '0', '0', '2', '1', '1', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
//        };
//        b.printBoard();
//        b.rescanPieces();
//        Evaluation e = new Evaluation();
//
//        for(CheckerState cc : b.nextStates()) {
//            cc.printBoard();
//            System.out.printf("eval:%f\n" , e.eval_distance_and_goal(0,cc));
//        }
//        System.out.printf("Player one eval to %d!\n", b.eval_distance_and_goal(0));
//        Algorithm a = new Algorithm("MINIMAX");
//        a.minimax(b, 3,true, 0, 1);
//        b.bestnext.printBoard();
//        System.out.printf("Player one eval to %d!\n", b.bestnext.eval_distance_and_goal(0));
        // time allowed for each turn
        return;
    }


}
