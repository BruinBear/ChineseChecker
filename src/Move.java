/**
 * Created by JingyuLiu on 4/2/2015.
 */
public class Move {
    IntPair piece;
    IntPair move;
    Move(IntPair p, IntPair m) {
        piece = new IntPair(p.x, p.y);
        move = new IntPair(m.x, m.y);
    }
}
