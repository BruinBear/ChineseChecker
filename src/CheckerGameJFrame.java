import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.*;

public class CheckerGameJFrame{
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

    private String getColor(int i) {
        String color;
        switch(i){
            case 0:
                color = "RED";
                break;
            case 1:
                color = "GREEN";
                break;
            case 2:
                color = "YELLOW";
                break;
            default:
                color = "No idea";
        }
        return color;
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
            String color = getColor(i);
            Label l = new Label(String.format("<%s> <%s>Player %d expanded %d nodes",
                    algPool.get(i).getClass().getName(),
                    color,
                    i + 1,
                    algPool.get(i).nodes_generated));
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

    public String run() {
        while(gamePanel.state.gameOver() == 0 || reset){
            gameInstance = gamePanel.computer_timed_turn();
//            gamePanel.computer_turn();
//            if(reset) {
//                preRestart();
//            }
            updateLabels();
        }
        return this.algPool.get(gamePanel.state.gameOver()-1).getClass().getName();
    }


    private void updateLabels() {
        for(int i = 0; i<labels.size(); i++) {
            labels.get(i).setText(String.format("<%s> <%s> Player %d expanded %d nodes",
                    algPool.get(i).getClass().getName(),
                    getColor(i),
                    i + 1,
                    algPool.get(i).nodes_generated));
        }
    }

    public static void main(String[] args) {
        try {
            LogManager lm = LogManager.getLogManager();
            Logger logger;
            FileHandler fh = new FileHandler("test/test.txt");

            logger = Logger.getLogger("MCTS");

            lm.addLogger(logger);
            logger.setLevel(Level.INFO);
            fh.setFormatter(new XMLFormatter());

            logger.addHandler(fh);
            ArrayList<Integer> sizes = new ArrayList<Integer>();
            sizes.add(100000);
            int games_to_play = 50;

            HashMap<String, Integer> res = new HashMap<String, Integer>();
            res.put("Maxn", 0);
            res.put("Paranoid", 0);
            res.put("MCTS_UCT_SOS", 0);
            for (int i = 0; i < games_to_play; i++) {
                logger.log(Level.INFO, String.format("Game %d starts.", i+1));
                CheckerState checker = new CheckerState(3);
                ArrayList<SearchAlgorithm> pool = new ArrayList<SearchAlgorithm>(3);
                pool.add(new MCTS_UCT_SOS(0.2, 1000000, null));
                pool.add(new Maxn(8));
                pool.add(new Paranoid(8));
                Collections.shuffle(pool);
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

                CheckerGameJFrame fr = new CheckerGameJFrame(checker, pool);

                // Run game
                String winner = fr.run();
                logger.log(Level.INFO, "Winner is: "+winner);
                logger.log(Level.INFO, fr.gameInstance.toString());
                logger.log(Level.INFO, "Turns played: "+fr.gameInstance.m_turn_played);

                for(SearchAlgorithm g : pool) {
                    logger.log(Level.INFO, "<"+g.getClass().getName()+"> generated "+g.nodes_generated+" nodes");
                }
                // Update result set
                res.put(winner, res.get(winner) + 1);
                try {
                    Thread.sleep(1000);                 //1000 milliseconds is one second.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                fr.frame.dispose();
            }
            // Log all the games played in this node setting

            for (String key : res.keySet()) {
                logger.log(Level.INFO, String.format("%s wins %d games(%f).", key, res.get(key), (double) res.get(key) / games_to_play));
            }
            fh.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }
}
