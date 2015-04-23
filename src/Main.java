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
        threePlayers();
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
        MCTS_UCT mcts = new MCTS_UCT(0.5);
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
        ArrayList<SearchAlgorithm> alg_pool= new ArrayList<SearchAlgorithm>(2);
        alg_pool.add(new Maxn(3));
        alg_pool.add(new Maxn(3));
        alg_pool.add(new Maxn(3));
        b.printBoard();
        while(b.gameOver()==0) {
            System.out.printf("player %d turn.", b.m_turn+1);
            SearchAlgorithm alg = alg_pool.get(b.m_turn);
            b.applyMove(alg.nextMove(b));
            b.printBoard();
        }
        System.out.printf("player %d won.", b.gameOver());
        return;
    }
}