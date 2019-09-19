import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class SudokuPanel extends JPanel{
    public SudokuPanel(){

    }

protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    int size = Math.min(200 - 4, 200 - 4) / 4;

    int y = (200 - (size * 4)) / 2;
    for (int horz = 0; horz < 4; horz++) {
        int x = (200 - (size * 4)) / 2;
        for (int vert = 0; vert < 4; vert++) {
            g.drawRect(x, y, size, size);
            x += size;
        }
        y += size;
    }
    g2d.dispose();
}
}