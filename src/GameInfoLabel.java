import javax.swing.*;

/**
 * Created by JingyuLiu on 4/17/2015.
 */
public class GameInfoLabel extends JLabel {
    GameInfoLabel(int turn, int whose_turn, int nodes_expanded) {
        super(String.format("Turn: %d, Player %d plays, Computer expanded %d nodes", turn ,whose_turn, nodes_expanded));
    }
}
