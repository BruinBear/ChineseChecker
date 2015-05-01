import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CheckerGameJFrame extends JFrame{

    private AlgorithmGamePanel panel;

    public CheckerGameJFrame(CheckerState ck, ArrayList<SearchAlgorithm> pool) {
        JFrame frame = new JFrame();
        frame.setTitle("Chinese Checker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        // Call to create game panel
        panel = new AlgorithmGamePanel(ck.getGraphicsConfiguration(), ck, pool);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        run();

    }

    public void run() {
        while(panel.state.gameOver() ==0){
            panel.computer_turn();
        }
    }

    public static void main(String[] args) {
        CheckerState checker = new CheckerState(3);
        ArrayList<SearchAlgorithm> pool= new ArrayList<SearchAlgorithm>(3);
        pool.add(new Maxn(4));
        pool.add(new MCTS_UCT(0.5, 5000));
        pool.add(new MCTS_UCT(0.5, 5000));
//        pool.add(new SOS(4, new double[][]{
//                {1,      0,        0},
//                {-1,     1,      0.5},
//                {-1,     0.5,      1}
//        }));
//        pool.add(new SOS(4, new double[][]{
//                {1,      0,        0},
//                {-1,     1,      0.5},
//                {-1,     0.5,      1}
//        }));
        CheckerGameJFrame fr = new CheckerGameJFrame(checker,pool);
    }
}