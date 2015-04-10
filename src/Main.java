import javafx.util.Pair;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        System.out.println("Welcome to Chinese Checker!\n");

        CheckerState b = new CheckerState();
        b.printBoard();
        System.out.printf("Player one eval to %d!\n", b.evaluation_goal_distance(0));
        b.printBoard();
        Scanner sc = new Scanner(System.in);
//        while(b.gameOver() == 0) {
//            b = b.nextStates().remove(0);
//            b.printBoard();
//            System.out.printf("Player one eval to %f!\n\n" +
//                    "", b.evaluation_goal_distance(0));
//            System.out.print("Pressenter key to continue");
//            sc.nextLine();
//        }

        Algorithm alg = new Algorithm();
//        b.evaluation_goal_distance(0);
////
//        Pair<Integer, CheckerState> res_minimax = alg.minimax(b, 2, true, 0, 1);
//        b = res_minimax.getValue().state_chain.get(1);
//        b.printBoard();
//
//        res_minimax = alg.minimax(b, 2, true, 1, 0);
//        b = res_minimax.getValue().state_chain.get(2);
//        b.printBoard();
//
//        res_minimax = alg.minimax(b, 2, true, 0, 1);
//        b = res_minimax.getValue().state_chain.get(3);
//        b.printBoard();
//
//        res_minimax = alg.minimax(b, 2, true, 1, 0);
//        b = res_minimax.getValue().state_chain.get(4);
//        b.printBoard();

        int turn = 0;
        while(b.gameOver() == 0) {
            turn++;
            alg.alphabeta(b,2,true, (turn+1)%2, turn%2, Integer.MIN_VALUE, Integer.MAX_VALUE);
//            alg.minimax(b, 2, true, (turn+1)%2, turn%2);
            b = b.bestnext;
            b.printBoard();
            System.out.printf("Evaluation for player %d: %d\n", b.m_turn+1, turn);
            System.out.print("Press enter key to continue");
            sc.nextLine();
        }
        System.out.printf("Number of nodes looked up: %d\n", alg.node_generated);

//        for (CheckerState ch : b.nextStates()) {
//            ch.printBoard();
//        }
    }
}

//
//        CheckerBoard b = new CheckerBoard(testBoard);
//        b.printBoard();
//
//        CheckerGame g = new CheckerGame(b, 2);
//        Stack<CheckerGame> gameStack = new Stack<CheckerGame>();
////        HashSet<String> hash = new HashSet<String>();
//
//        gameStack.add(g);
//        GameTreeNode WinningTree = isWinningNode(g, null);
//        if (WinningTree != null)
//            System.out.println("Player one has winning strategy");
//        else
//            System.out.println("Player one doesn't have winning strategy");
////
////        LinkedList<GameTreeNode> treeQ = new LinkedList<GameTreeNode>();
////        treeQ.add(WinningTree);
////        while(!treeQ.isEmpty()){
////            GameTreeNode first = treeQ.removeFirst();
////            first.printNode();
////            treeQ.addAll(first.children);
////        }
//
////
////        // for printing
////        Stack<GameTreeNode> treeStack = new Stack<GameTreeNode>();
////
////        System.out.println("Winning Strategy:");
//////        treeStack.push(winningTree);
//
////
////        while(!gameStack.isEmpty()) {
////            CheckerGame toplay = gameStack.pop();
//////            hash.add(toplay.toString());
////            toplay.m_board.printBoard();
////            System.out.printf("Player %d's m_turn\n", toplay.m_turn+1);
////            if (toplay.gameOver())
////                break;
////            else {
////                ArrayList<CheckerGame> checkerGames = toplay.nextStates();
////                for (CheckerGame newState : checkerGames) {
//////                    if(!hash.contains(newState.toString()))
////                        gameStack.add(newState);
////                }
////            }
////        }
//
//    }
//
////
//    public static void printLeafToRoot(GameTreeNode leaf) {
//        int i = 0;
//        System.out.printf("Level 0\n");
//        leaf.game.printGame();
//        while(leaf.parent!=null) {
//            leaf = leaf.parent;
//            i++;
//            System.out.printf("Level %d\n",i);
//            leaf.game.printGame();
//        }
//    }
//
//    public static GameTreeNode isWinningNode(CheckerGame g, GameTreeNode parent) {
//        GameTreeNode root = new GameTreeNode(g);
//        root.parent = parent;
//        if (g.m_board.m_grid[4][4] == '1' && g.m_turn == 1) {
//            System.out.printf("Start printing one winning route\n");
//            printLeafToRoot(root);
//            System.out.printf("Finshed one winning route\n");
//            return root; //game won by player 1
//        } else if (g.m_board.m_grid[2][2] == '2' && g.m_turn == 0) {
//            return null; // game lost by player 1
//        } else {
//            if (g.m_turn == 0) // player one to play  OR NODE
//            {
//                for (CheckerGame checkerGame : g.nextStates()) {
//                    GameTreeNode orNode = isWinningNode(checkerGame, root);
//                    if (orNode != null) {
//                        orNode.level = root.level+1;
//                        orNode.parentHash = root.game.m_hash.get(root.game.toString());
//                        root.addChild(orNode);
//                        // wins
//                        return root;
//                    }
//                }
//                // if all or nodes fail
//                return null;
//            } else {    // player two to play  AND NODE
//                for (CheckerGame checkerGame : g.nextStates()) {
//                    GameTreeNode andNode = isWinningNode(checkerGame, parent);
//                    if (null == andNode) {
//                        return null;
//                    } else { // append child to root
//                        andNode.level = root.level+1;
//                        andNode.parentHash = root.game.m_hash.get(root.game.toString());
//                        root.addChild(andNode);
//                    }
//                    // if all wins
//                }
//                return root;
//            }
//        }
//    }
////        if(g.m_board.m_grid[4][4] == '1' && g.m_turn == 1) {
////            //end game and player 1 wins
////            return true;
////        } else if (g.m_board.m_grid[2][2] == '2' && g.m_turn == 0){
////            //end game and player 2 wins
////            return false;
////        } else { // keep playing
////            if(g.m_turn == 0) {
////                for (CheckerGame checkerGame : g.nextStates()) {
////                    if(isWinningNode(checkerGame, newparent)) {
////                        return true;
////                    }
////                }
////                return false;
////            }
////            // assume two turns
////            else {
////                for (CheckerGame checkerGame : g.nextStates()) {
////                    if(!isWinningNode(checkerGame, newparent)) {
////                        return false;
////                    }
////                }
////                return true;
////            }
////        }
////    }
//
////
////    public static GameTreeNode buildWinningTreeOne(CheckerGame g) {
////        GameTreeNode root = new GameTreeNode(g);
////        // base case, game is won
////        if(g.m_board.m_grid[4][4] == '1' && g.m_turn == 1) {
////            System.out.println("Ending Round");
////            g.printGame();
////            return root;
////        } else {
////            ArrayList<CheckerGame> nextStates = g.nextStates();
////            if(g.m_turn == 0) { // player one's m_turn
////                for (CheckerGame checkerGame : nextStates) {
////                    GameTreeNode childTree = buildWinningTreeOne(checkerGame);
////                    if(childTree != null) {
////                        // only one child needs to be linked
////                        root.addChild(childTree);
////                        childTree.root.printGame();
////                        return root;
////                    }
////                }
////                // if no child gives a good winning game tree then we return null
////                return null;
////            }
////            // assume two turns
////            else {  // player two's m_turn
////                for (CheckerGame checkerGame : nextStates) {
////                    GameTreeNode childTree = buildWinningTreeOne(checkerGame);
////                    if(childTree == null) {
////                        return null;
////                    } else {
////                        childTree.root.printGame();
////                        root.addChild(childTree);
////                    }
////                }
////                return root;
////            }
////        }
////    }
//
//}
