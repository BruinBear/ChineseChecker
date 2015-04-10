import java.awt.*;
import javax.swing.*;

public class CanvasDemo extends JFrame{

    private class GameCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            g.drawString("My First kiss", 10, 20);
            Image board = new ImageIcon("checkerboard.gif").getImage();
            g.drawImage(board, 10,10,435,500,null);
        }
    }
    private GameCanvas canvas = new GameCanvas();

    public static void __main(String[] args) {
        CanvasDemo fr = new CanvasDemo();
    }

    public CanvasDemo() {
        setLayout(new BorderLayout());
        setSize(800, 600);
        add("Center", canvas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Canvas Demo");
        setLocationRelativeTo(null);
        setVisible(true);
    }

}