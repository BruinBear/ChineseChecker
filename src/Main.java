
public class Main{

    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!\n");
        CheckerState checker = new CheckerState();
        Algorithm alg = new Algorithm("ALPHABETA_STATE");
        CheckerGameJFrame checker_gui = new CheckerGameJFrame(checker, alg);
    }


}
