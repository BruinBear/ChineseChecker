import java.util.ArrayList;
import java.util.logging.*;

public class Main{

    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!\n");
//
//        IntPair a = new IntPair(10,10);
//        IntPair b = new IntPair(13,4);
//        System.out.printf("Distance %d\n", a.minPathDistance(b));

//        CheckerState board = new CheckerState();
//
//        MCTS_UCT mcts = new MCTS_UCT(0.5);
//        Move mv = mcts.uctSearch(board);
//        threePlayers();

        try {
            LogManager lm = LogManager.getLogManager();
            Logger logger;
            FileHandler fh = new FileHandler("log_test.txt");

            logger = Logger.getLogger("MCTS");

            lm.addLogger(logger);
            logger.setLevel(Level.INFO);
            fh.setFormatter(new XMLFormatter());

            logger.addHandler(fh);

            for(int i = 0; i<5; i++) {
                CheckerState b = new CheckerState(3);
                threePlayers(b, logger);
            }
            fh.close();
        } catch (Exception e) {
            System.out.println("Exception thrown: " + e);
            e.printStackTrace();
        }

    }

    public static void alphaVSminimax() {
        CheckerState b = new CheckerState();
        ArrayList<SearchAlgorithm> alg_pool= new ArrayList<SearchAlgorithm>(2);
        Minimax alphabeta = new Minimax("ALPHABETA", 5);
        Minimax minimax = new Minimax("MINIMAX", 4);
        alg_pool.add(alphabeta);
        alg_pool.add(minimax);
        b.printBoard();
        while(b.gameOver()==0) {
            SearchAlgorithm alg = alg_pool.get(b.m_turn);
            b.applyMove(alg.nextMove(b));
            b.printBoard();
        }
        System.out.printf("player %d won.", b.gameOver());
        return;
    }


    public static void alphaVSmcts() {
        CheckerState b = new CheckerState();
        ArrayList<SearchAlgorithm> alg_pool= new ArrayList<SearchAlgorithm>(2);
        Minimax alphabeta = new Minimax("ALPHABETA", 5);
        MCTS_UCT mcts = new MCTS_UCT(0.5, 20000);
        alg_pool.add(alphabeta);
        alg_pool.add(mcts);
        b.printBoard();
        while(b.gameOver()==0) {
            SearchAlgorithm alg = alg_pool.get(b.m_turn);
            b.applyMove(alg.nextMove(b));
            b.printBoard();
        }
        System.out.printf("player %d won.", b.gameOver());
        return;
    }


    public static void threePlayers(CheckerState b, Logger logger) {
        ArrayList<SearchAlgorithm> pool= new ArrayList<SearchAlgorithm>(3);
        double[][] so = new double[][]{
                {1,     -1,     -1},
                {-1,     1,      1},
                {-1,     1,      1}};

        pool.add(new Maxn(10));
        pool.add(new MCTS_UCT_SOS(0.2, 5000, null));
        pool.add(new MCTS_UCT_PARANOID(0.2, 5000, 2));

        while(b.gameOver()==0) {
            SearchAlgorithm alg = pool.get(b.m_turn);
            Move nextTimedMove = alg.nextMoveTimed(b,3000);
            b.applyMove(nextTimedMove);
            b.printBoard();
        }
        logger.log(Level.INFO, "player "+b.gameOver()+" won.\n"+"board:\n "+b.toString());
    }


    public static void getAB(double b) {
        for(double i = 1; i <11 ;i++)
        System.out.printf("%f depth: %f leaves\n", i, Math.pow(b,Math.ceil(i / 2)) + Math.pow(b,Math.floor(i/2))-1);
    }
}
