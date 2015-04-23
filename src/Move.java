/**
 * Created by JingyuLiu on 4/2/2015.
 */
public class Move {
    IntPair piece;
    IntPair dest;
    int improvement;

    Move(IntPair p, IntPair m) {
        piece = new IntPair(p.x, p.y);
        dest = new IntPair(m.x, m.y);
    }

    Move(Move mv) {
        piece = mv.piece;
        dest = mv.dest;
    }

    Move(Move mv, IntPair goal) {
        this(mv);
        improvement = mv.piece.minPathDistance(goal) - mv.dest.minPathDistance(goal);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Move)) {
            return false;
        }

        Move mv = (Move) o;

        return mv.piece == this.piece && mv.dest == this.dest;
    }


    @Override
    public String toString(){
        return String.format("(%d, %d) -> (%d, %d)", piece.x, piece.y, dest.x, dest.y);
    }

}
