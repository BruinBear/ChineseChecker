import java.util.ArrayList;

/**
 * Created by JingyuLiu on 4/2/2015.
 */
public class GameTree {
    protected CheckerGame root;
    protected ArrayList<GameTree> children = new ArrayList<GameTree>();
    GameTree(CheckerGame g) {
        root = g;
    }


    public void addChild(GameTree g) {
        children.add(g);
    }
}
