import javax.swing.*;

/**
 * Created by JingyuLiu on 4/17/2015.
 */
public class GameInfoLabel extends JLabel {
    public String gameText(int turn, int whose_turn, int nodes_expanded) {
        return String.format("Turn: %d, Player %d plays, Computer expanded %d nodes", turn ,whose_turn, nodes_expanded);
    }
}
