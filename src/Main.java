import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main{

    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!\n");

        CheckerState b = new CheckerState();
        TimedGameSimulation simulation1 = new TimedGameSimulation(b, 5000, 4);
        simulation1.startGame();
        return;
    }


}