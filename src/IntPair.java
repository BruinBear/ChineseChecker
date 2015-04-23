/**
 * Created by JingyuLiu on 3/31/2015.
 */
public class IntPair {
    final int x;
    final int y;
    IntPair(int x, int y) {this.x=x;this.y=y;}

    @Override
    public String toString() {
        return String.format("(%02d,%02d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof IntPair)) {
            return false;
        }

        IntPair p = (IntPair) o;

        return p.x == this.x && p.y == this.y;
    }

    @Override
    public int hashCode() {
        return 100*x+y;
    }


    /**
     * given the board, calculate the min distance given one-step moves can be made in 6 directions
     * @param b IntPair
     */
    public int minPathDistance(IntPair b) {
        if((this.x<b.x && this.y<b.y) || (this.x>=b.x && this.y>=b.y)) {
            return Math.abs(this.y-b.y)+Math.abs(this.x-b.x);
        } else {
            int duplicate = Math.min(Math.abs(this.x-b.x), Math.abs(this.y-b.y));
            return Math.abs(this.y-b.y)+Math.abs(this.x-b.x) - duplicate;
        }
    }
}
