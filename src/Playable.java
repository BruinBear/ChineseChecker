import javax.swing.*;

public class Playable{

    private CheckerState checkerboard = new CheckerState();
    private CheckerState checkerboard_buf;
    private SearchAlgorithm alg = new Minimax("ALPHABETA", 3);

    public Playable() {
        JFrame frame = new JFrame();
        frame.setTitle("Chinese Checker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        PlayablePanel playablePanel = new PlayablePanel(checkerboard.getGraphicsConfiguration(), checkerboard, alg);
        frame.add(playablePanel);
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
        Playable fr = new Playable();
//        fr.startGame();
    }


}