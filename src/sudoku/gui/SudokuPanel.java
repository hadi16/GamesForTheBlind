package sudoku.gui;

import javax.swing.*;
import java.awt.*;

public class SudokuPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics.create();

        int squareDimension = (200 - 4) / 4;

        int yPosition = (200 - (squareDimension * 4)) / 2;
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            int xPosition = (200 - (squareDimension * 4)) / 2;

            for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                xPosition += squareDimension;
            }

            yPosition += squareDimension;
        }

        graphics2D.dispose();
    }
}
