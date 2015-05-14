import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.*;

public class CheckerGameJFrame extends JFrame{
    protected CheckerState gameOrigin;
    protected CheckerState gameInstance;
    protected AlgorithmGamePanel gamePanel;
    protected JPanel statsPanel;
    protected ArrayList<SearchAlgorithm> algPool;
    protected ArrayList<Label> labels = new ArrayList<Label>();
    protected JFrame frame;
    protected boolean reset = false;


    // Frame has two panels
    public CheckerGameJFrame(CheckerState ck, ArrayList<SearchAlgorithm> pool) {
        this.gameOrigin = ck;
        this.algPool = pool;
        this.gameInstance = new CheckerState(ck);
        setupFrame();
        setupGamePanel();
        setupStatsPanel();
        addPlayButton();

        frame.setVisible(true);
        run();
    }


    private JFrame setupFrame() {
        frame = new JFrame();
        frame.setTitle("Chinese Checker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(1200, 600);
        GridLayout myLayout = new GridLayout(1,2);
        frame.setLayout(myLayout);
        return frame;
    }


    public JPanel setupGamePanel() {
        gamePanel = new AlgorithmGamePanel(gameInstance.getGraphicsConfiguration(), gameInstance, algPool);
        frame.add(gamePanel, 0);
        return gamePanel;
    }


    public JPanel setupStatsPanel() {
        statsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;

        labels.clear();
        // add components to the gamePanel
        for(int i = 0; i<algPool.size();i++) {
            Label l = new Label(String.format("<%s> Player %d expanded %d nodes", algPool.get(i).getClass().getName(), i + 1, algPool.get(i).nodes_generated));
            labels.add(l);
            constraints.gridy = i;
            statsPanel.add(l, constraints);
        }
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Statistics Panel"));
        frame.add(statsPanel, 1);
        return statsPanel;
    }

    public void addPlayButton() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 5;

        JButton playButton = new JButton("Play");

//        playButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e){
//                reset = true;
//
//            }
//        });
        statsPanel.add(playButton, constraints);
    }




//    private void preRestart() {
//        // clear stats
//        gameInstance = new CheckerState(gameOrigin);
//        for (SearchAlgorithm a : algPool) {
//            a.reset();
//        }
//        frame.removeAll();
//        setupGamePanel();
//        setupStatsPanel();
//        addPlayButton();
//        frame.add(gamePanel, 0);
//        frame.add(statsPanel, 1);
//        frame.setVisible(true);
//        reset = false;
//    }
//

    public void run() {
        while(gamePanel.state.gameOver() == 0 || reset){
            gamePanel.computer_turn();
//            if(reset) {
//                preRestart();
//            }
            updateLabels();
        }
    }


    private void updateLabels() {
        for(int i = 0; i<labels.size(); i++) {
            labels.get(i).setText(String.format("<%s> Player %d expanded %d nodes", algPool.get(i).getClass().getName(), i + 1, algPool.get(i).nodes_generated));
        }
    }

    public static void main(String[] args) {
        CheckerState checker = new CheckerState(3);
        ArrayList<SearchAlgorithm> pool= new ArrayList<SearchAlgorithm>(3);
        pool.add(new Maxn(5));
        pool.add(new MCTS_UCT_SOS(0.2, 50000, null));
        pool.add(new MCTS_UCT_PARANOID(0.2, 50000, 2));
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
