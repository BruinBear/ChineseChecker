/**
 * Created by JingyuLiu on 4/2/2015.
 */
public class Move {
    IntPair piece;
    IntPair dest;
    Move(IntPair p, IntPair m) {
        piece = new IntPair(p.x, p.y);
        dest = new IntPair(m.x, m.y);
    }
}
