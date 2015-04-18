import javax.swing.*;

public class CheckerGameJFrame extends JFrame{

    private CheckerState checkerboard ;
    private Algorithm alg;

    public CheckerGameJFrame(CheckerState ck, Algorithm al) {
        checkerboard = ck;
        alg = al;
        JFrame frame = new JFrame();
        frame.setTitle("Chinese Checker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        // Call to create game panel
        CheckerGamePanel piecePanel = new CheckerGamePanel(checkerboard.getGraphicsConfiguration(), checkerboard, alg);
        frame.add(piecePanel);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        CheckerState checker = new CheckerState();
        Algorithm alg = new Algorithm("ALPHABETA_STATE");
        CheckerGameJFrame fr = new CheckerGameJFrame(checker,alg);
    }

}