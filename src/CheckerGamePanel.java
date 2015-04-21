import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JingyuLiu on 4/16/2015.
 */
public class CheckerGamePanel extends JPanel {
    private HashMap<IntPair, PieceShape> piece_map;
    private Dimension dim = new Dimension(600, 600);
    private CheckerState state;
    private IntPair prevClicked = null;
    private Algorithm alg;
    private JLabel gameInfo;
    /**
     *
     * @param p_map
     * @param s
     */
    public CheckerGamePanel(HashMap<IntPair, PieceShape> p_map, CheckerState s, Algorithm a) {
        this.alg = a;
        state = s;
        this.piece_map = p_map;
        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("A click is catched");
                ((CheckerGamePanel) evt.getSource()).repaint();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(state.m_turn != 0 || state.gameOver() != 0)
                    return;
                super.mouseClicked(me);
                IntPair clicked = null;
                PieceShape shape = null;
                for (IntPair tmpPair : piece_map.keySet()) {
                    PieceShape tmpShape = piece_map.get(tmpPair);
                    if (tmpShape.contains(me.getPoint())) {
                        clicked = tmpPair;
                        shape = tmpShape;
                        break;
                    }
                }
                if(clicked == null ) // not any shape clicked on
                    return;

                // A valid click is triggered
                if(prevClicked == null) { // nothing was clicked before this click
                    switch (shape.getType()) {
                        case '1':
                            markNextMove(clicked, true);
                            break;
                        case '2':
//                            markNextMove(clicked, true);
                            break;
                        default: // not a piece
                            return;
                    }
                    prevClicked = clicked;
                } else {
                    if(shape.isDest()) { // previous clicked piece can move to this dest
                        Move validMove = new Move(prevClicked, clicked);
                        markNextMove(prevClicked, false);
                        state.movePieceTo(validMove);
                        switchType(prevClicked, clicked);
                        prevClicked = null;
                        // Computer Turn Now
                        computer_turn();
                    } else { // not finishing a move
                        markNextMove(prevClicked, false);
                        switch (shape.getType()) {
                            case '1':
                                markNextMove(clicked, true);
                                break;
                            case '2':
//                            markNextMove(clicked, true);
                                break;
                            default: // not a piece
                                return;
                        }
                        prevClicked = clicked;
                    }
                }
                firePropertyChange("makingMove",false,true);
            }
        });

        gameInfo = new JLabel(gameText(s.m_turn_played, s.m_turn+1, a.node_expanded));
        this.add(gameInfo);
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

    private void markNextMove(IntPair from, boolean tomark) {
        ArrayList<IntPair> canMove = state.pieceCanMove(from);
        for (IntPair pp : canMove) {
            System.out.println("Can go to " + pp.toString());
            PieceShape move_shape = CheckerGamePanel.this.piece_map.get(pp);
            if(tomark) {
                move_shape.markDest();
            } else {
                move_shape.markOffDest();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for(PieceShape s : piece_map.values()) {
            s.fillByType(g2d);
        }
    }


    @Override
    public Dimension getPreferredSize() {
        return dim;
    }



    public void computer_turn() {
        gameInfo.setText(gameText(state.m_turn_played, state.m_turn + 1, alg.node_expanded));
        // give turn info
        if(state.gameOver() != 0) {
            System.out.printf("Winner is player %d\n", state.gameOver());
            return;
        }
        Move mv;
        int current_num_nodes_processed = alg.node_expanded;
        alg.execute_once(state, 5, 1, 0);
        System.out.printf("%d more nodes generated\n", alg.node_expanded - current_num_nodes_processed);

        // Apply best move with graphics
        mv = alg.bestMove;
        state.movePieceTo(mv);
        switchType(mv);

        System.out.printf("Player two eval to %f!\n", alg.eval_func.eval_distance_and_goal(1, state));

        System.out.printf("Number of nodes looked up: %d\n", alg.node_expanded);
        // give turn info
        if(state.gameOver() != 0) {
            System.out.printf("Winner is player %d\n", state.gameOver());
            return;
        }
        gameInfo.setText(gameText(state.m_turn_played, state.m_turn + 1, alg.node_expanded));
    }


    private String gameText(int turn, int whose_turn, int nodes_expanded) {
        String win = "";
        if(state.gameOver()!=0) {
            win = String.format("\nPlayer %c won", state.gameOver()+'1');
        }
        return String.format("Turn: %d, Player %d plays, Computer expanded %d nodes%s" , turn ,whose_turn, nodes_expanded, win);
    }
}
