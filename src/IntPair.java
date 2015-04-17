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
}
