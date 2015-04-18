import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class PlayableChecker extends JFrame{

    private CheckerState checkerboard = new CheckerState();
    private CheckerState checkerboard_buf;
    private Algorithm alg = new Algorithm("ALPHABETA_STATE");

    public PlayableChecker() {
        JFrame frame = new JFrame();
        frame.setTitle("Chinese Checker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        PiecePanel piecePanel = new PiecePanel(checkerboard.getGraphicsConfiguration(), checkerboard, alg);
        frame.add(piecePanel);
        frame.pack();
        frame.setVisible(true);
//        checkerboard = new CheckerState();
//        checkerboard_buf = new CheckerState();
//        setLayout(new BorderLayout());
//        setSize(600, 600);
//        add("Center", canvas);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setTitle("Chinese Checker");
//        setLocationRelativeTo(null);
//        setVisible(true);
    }





    public static void main(String[] args) {
        PlayableChecker fr = new PlayableChecker();
//        fr.startGame();
    }


}