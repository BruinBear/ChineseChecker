import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* Created by JingyuLiu on 4/30/2015.
*/

public class ToolbarFrame2 extends Frame {
    JButton cutButton, copyButton, pasteButton;

    JButton javaButton, macButton, motifButton, winButton;

    public ToolbarFrame2() {
        setSize(450, 250);

        ActionListener printListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.out.println(ae.getActionCommand());
            }
        };

        // JPanel works similarly to Panel, so we'll use it
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

        cutButton = new JButton("Cut");
        cutButton.addActionListener(printListener);
        toolbar.add(cutButton);

        copyButton = new JButton("Copy");
        copyButton.addActionListener(printListener);
        toolbar.add(copyButton);

        pasteButton = new JButton("Paste");
        pasteButton.addActionListener(printListener);
        toolbar.add(pasteButton);

        add(toolbar, BorderLayout.NORTH); // The new BorderLayout add
    }

    public static void main(String args[]) {
        ToolbarFrame2 tf2 = new ToolbarFrame2();
        tf2.setVisible(true);
    }
}
