import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CheckerGameJFrame extends JFrame{

    private AlgorithmGamePanel gamePanel;
    private JLabel labelUsername = new JLabel("Enter username: ");
    private JLabel labelPassword = new JLabel("Enter password: ");
    private JTextField textUsername = new JTextField(20);
    private JPasswordField fieldPassword = new JPasswordField(20);
    private JButton buttonLogin = new JButton("Login");


    public CheckerGameJFrame(CheckerState ck, ArrayList<SearchAlgorithm> pool) {
        JFrame frame = new JFrame();
        frame.setTitle("Chinese Checker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(1200, 600);
        GridLayout myLayout = new GridLayout(1,2);
        frame.setLayout(myLayout);

        // Call to create game gamePanel
        gamePanel = new AlgorithmGamePanel(ck.getGraphicsConfiguration(), ck, pool);

        JPanel newPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        // add components to the gamePanel
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(labelUsername, constraints);

        constraints.gridx = 1;
        newPanel.add(textUsername, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(labelPassword, constraints);

        constraints.gridx = 1;
        newPanel.add(fieldPassword, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(buttonLogin, constraints);

        // set border for the gamePanel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Login Panel"));

        // add the gamePanel to this frame
        frame.add(gamePanel);
        frame.add(newPanel);

        frame.setVisible(true);
        run();

    }

    public void run() {
        while(gamePanel.state.gameOver() ==0){
            gamePanel.computer_turn();
        }
    }

    public static void main(String[] args) {
        CheckerState checker = new CheckerState(3);
        ArrayList<SearchAlgorithm> pool= new ArrayList<SearchAlgorithm>(3);
        pool.add(new MCTS_UCT(0.2, 5000));
        pool.add(new MCTS_UCT_SOS(0.2, 5000, null));
        pool.add(new MCTS_UCT_SOS(0.2, 5000, null));
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