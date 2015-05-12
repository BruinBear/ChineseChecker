import java.util.ArrayList;

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

        getAB(3);
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


    public static void threePlayers() {
        CheckerState b = new CheckerState(3);
        ArrayList<SearchAlgorithm> pool= new ArrayList<SearchAlgorithm>(3);
        double[][] so = new double[][]{
                {1,     -1,     -1},
                {-1,     1,      1},
                {-1,     1,      1}};

        pool.add(new MCTS_UCT(0.2, 5000));
        pool.add(new MCTS_UCT_SOS(0.2, 5000, so));
        pool.add(new MCTS_UCT_SOS(0.2, 5000, so));


//
//        b.m_grid =  new char[][]{
////            0    1    2    3    4    5    6    7    8    9    10   11   12   13   14
///*0*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
///*1*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' '},
///*2*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' '},
///*3*/       {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' '},
///*4*/       {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '3', '3', '3', ' '},
///*5*/       {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '3', ' ', ' '},
///*6*/       {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '0', '3', ' ', ' ', ' '},
///*7*/       {' ', ' ', ' ', ' ', '0', '0', '0', '0', '0', '0', '3', ' ', ' ', ' ', ' '},
///*8*/       {' ', ' ', ' ', '2', '2', '0', '0', '0', '0', '0', '1', ' ', ' ', ' ', ' '},
///*9*/       {' ', ' ', '2', '0', '0', '0', '0', '0', '0', '1', '1', ' ', ' ', ' ', ' '},
///*10*/      {' ', '2', '2', '2', '0', '0', '1', '0', '0', '1', '1', ' ', ' ', ' ', ' '},
///*11*/      {' ', ' ', ' ', ' ', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
///*12*/      {' ', ' ', ' ', ' ', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
///*13*/      {' ', ' ', ' ', ' ', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
///*14*/      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
//        };
//        b.rescanPieces();
//        b.m_turn = 1;
        b.printBoard();
        while(b.gameOver()==0) {
            System.out.printf("player %d turn.", b.m_turn+1);
            SearchAlgorithm alg = pool.get(b.m_turn);
            b.applyMove(alg.nextMove(b));
            b.printBoard();
        }
        System.out.printf("player %d won.", b.gameOver());
        return;
    }


    public static void getAB(double b) {
        for(double i = 1; i <11 ;i++)
        System.out.printf("%f depth: %f leaves\n", i, Math.pow(b,Math.ceil(i / 2)) + Math.pow(b,Math.floor(i/2))-1);
    }
}