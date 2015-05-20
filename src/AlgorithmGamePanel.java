import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
* Created by JingyuLiu on 4/30/2015.
*/
public class AlgorithmGamePanel extends JPanel {
    private HashMap<IntPair, PieceShape> piece_map;
    private Dimension dim = new Dimension(1200, 600);
    protected CheckerState state;
    private ArrayList<SearchAlgorithm> algPool;
    private JLabel gameInfo;
    private JLabel algInfo;
    Graphics2D g2d;


    /**
     *
     * @param p_map
     * @param s
     */
    public AlgorithmGamePanel(HashMap<IntPair, PieceShape> p_map, CheckerState s, ArrayList<SearchAlgorithm> pool) {
        algPool = pool;
        state = s;
        this.piece_map = p_map;
        gameInfo = new JLabel(gameText());
        algInfo = new JLabel(algText());
        algInfo.setVerticalAlignment(JLabel.BOTTOM);
        algInfo.setHorizontalAlignment(JLabel.RIGHT);

        this.add(gameInfo);
        this.add(algInfo);
    }


    private void switchType(IntPair piece, IntPair dest) {
        PieceShape piece_s = piece_map.get(piece);
        PieceShape dest_s = piece_map.get(dest);
        dest_s.setType(piece_s.getType());
        piece_s.setType('0');
    }


    private void switchType(Move mv) {
        switchType(mv.piece, mv.dest);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        for(PieceShape s : piece_map.values()) {
            s.fillByType(g2d);
        }
    }


    @Override
    public Dimension getPreferredSize() {
        return dim;
    }


    public CheckerState computer_timed_turn() {
        SearchAlgorithm alg = algPool.get(state.m_turn);
//
        Move nextTimedMove = alg.nextMoveTimed(state,10000);
        state.applyMove(nextTimedMove);

//        Move nextNodeLimitedMove = alg.nextNodeLimitedMove(state,5000);
        switchType(nextTimedMove);

        gameInfo.setText(gameText());
        algInfo.setText(evalText());
        repaint();
        if(state.gameOver() != 0) {
            System.out.printf("Winner is player %d\n", state.gameOver());
        }
        return state;
    }

    public void computer_turn() {
        SearchAlgorithm a = algPool.get(state.m_turn);
        Move mv = a.nextMove(state);

        // Apply best move with graphics
        state.applyMove(mv);
        switchType(mv);
        state.printBoard();
        // give turn info
        gameInfo.setText(gameText());
        algInfo.setText(evalText());
        repaint();
        if(state.gameOver() != 0) {
            System.out.printf("Winner is player %d\n", state.gameOver());
            return;
        }
    }


    private String gameText() {
        String win = "";
        if(state.gameOver()!=0) {
            win = String.format("\nPlayer %d won", state.gameOver());
        }
        return String.format("Turn: %d, Player %d plays %s" , state.m_turn_played+1 ,state.m_turn+1, win);
    }


    private String algText() {
        StringBuffer buf = new StringBuffer();
        int i = 1;
        for(SearchAlgorithm alg : algPool) {
            buf.append(String.format("Player %s %d ",alg.getClass().getName() ,alg.numNodesGenertaed()));
        }
        return buf.toString();
    }

    private String evalText() {
        StringBuffer buf = new StringBuffer();
        int i = 1;
        for(double d : algPool.get(0).getTuple(state)) {
            buf.append(String.format("EVAL: %f  ", d));
            i++;
        }
        return buf.toString();
    }
}

