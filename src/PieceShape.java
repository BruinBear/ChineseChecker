import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by JingyuLiu on 4/17/2015.
 */
public class PieceShape extends Ellipse2D.Double {
    // grid pos
    private char p_char;
    private boolean isDest = false;

    PieceShape(double x, double y, double w, double h, char c) {
        setFrame(x,y,w,h);
        p_char = c;
    }

    public void setType(char c) {
        p_char = c;
    }


    public boolean isDest() {
        return isDest;
    }


    public void markDest() {
        isDest = true;
    }


    public void markOffDest() {
        isDest = false;
    }


    public char getType() {
        return p_char;
    }


    public void fillByType(Graphics2D g2d) {
        if(isDest) {
            g2d.setPaint(Color.gray);
        } else {
            switch(p_char) {
                case '0':
                    g2d.setPaint(Color.white);
                    break;
                case '1':
                    g2d.setPaint(Color.red);
                    break;
                case '2':
                    g2d.setPaint(Color.green);
                    break;
                case '3':
                    g2d.setPaint(Color.yellow);
                    break;
                case '4':
                    g2d.setPaint(Color.blue);
                    break;
                case '5':
                    g2d.setPaint(Color.pink);
                    break;
                case '6':
                    g2d.setPaint(Color.cyan);
                    break;
                default: // don't do anything
            }
        }
        g2d.fill(this);
    }
}
