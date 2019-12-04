package gamesforblind.mastermind.gui;

import gamesforblind.mastermind.MastermindState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Contains the main GUI code for Mastermind. Serves as a custom JPanel for Mastermind GUI (extends JPanel).
 */
public class MastermindPanel extends JPanel {
    /**
     * Creates a new MastermindPanel.
     *
     * @param initialState The initial state of the Mastermind game.
     */
    public MastermindPanel(@NotNull MastermindState initialState) {
    }

    /**
     * When repaint() or paint() is called, paints the Sudoku GUI.
     * Might look into using comic sans as a font.
     *
     * @param graphics The {@link Graphics} object used for painting.
     */
    @Override
    protected void paintComponent(@NotNull Graphics graphics) {
        super.paintComponent(graphics);
    }
}
