import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
            FileHandler fh = new FileHandler("test/20-40-80k.txt");

            logger = Logger.getLogger("Chinese Checker AI");

            lm.addLogger(logger);
            logger.setLevel(Level.INFO);
            fh.setFormatter(new XMLFormatter());

            logger.addHandler(fh);
            ArrayList<Integer> millis = new ArrayList<Integer>();
//            millis.add(2000);
//            millis.add(5000);
//            millis.add(10000);
//            millis.add(20000);
//            millis.add(40000);
            millis.add(160000);

            int games_to_play = 100;
            for(Integer n: millis) {
                HashMap<String, Integer> res = new HashMap<String, Integer>();
                res.put("Maxn", 0);
                res.put("Paranoid", 0);
                res.put("MCTS_UCT_SOS", 0);
                logger.log(Level.INFO, String.format("Current Node Limit: %d.", n));
                for(int i = 0; i<games_to_play; i++) {
                    logger.log(Level.INFO, String.format("Game %d starts.", i+1));
                    CheckerState b = new CheckerState(3);
                    String winner = threePlayers(b, logger, n);
                    res.put(winner, res.get(winner)+1) ;

                    logger.log(Level.INFO, String.format("player(%s) %d won.", winner, b.gameOver()));
                    logger.log(Level.INFO, b.toString());
                    logger.log(Level.INFO, "Turns played: "+b.m_turn_played);
                }
                for(String key: res.keySet()){
                    logger.log(Level.INFO, String.format("%s wins %d games(%f).",key, res.get(key), (double)res.get(key)/ games_to_play));
                }
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


    public static String threePlayers(CheckerState b, Logger logger, int limit) {
        ArrayList<SearchAlgorithm> pool= new ArrayList<SearchAlgorithm>(3);

        pool.add(new Maxn(9));
        pool.add(new MCTS_UCT_SOS(0.2, 100000000, null));
        pool.add(new Paranoid(9));
        Collections.shuffle(pool);
        while(b.gameOver()==0) {
            SearchAlgorithm alg = pool.get(b.m_turn);
            Move nextTimedMove = alg.nextNodeLimitedMove(b,limit);
            System.out.println(alg.getClass().getName()+"Finished at depth " + alg.current_depth);

//            b.applyMove(nextTimedMove);
//            Move nextNodeLimitedMove = alg.nextNodeLimitedMove(b, limit);
            b.applyMove(nextTimedMove);
//            b.printBoard();
        }


        for(SearchAlgorithm g : pool) {
            logger.log(Level.INFO, "<"+g.getClass().getName()+"> generated "+g.nodes_generated+" nodes");
        }
        return pool.get(b.gameOver()-1).getClass().getName();
    }


    public static void getAB(double b) {
        for(double i = 1; i <11 ;i++)
        System.out.printf("%f depth: %f leaves\n", i, Math.pow(b,Math.ceil(i / 2)) + Math.pow(b,Math.floor(i/2))-1);
    }


    public static  int maxrec(int b, int d){
        if(d == 1) return b;
        if(d == 0) return 1;
        else return maxrec(b,d-1) + (b-1)*maxrec(b,d-2);
    }
}
