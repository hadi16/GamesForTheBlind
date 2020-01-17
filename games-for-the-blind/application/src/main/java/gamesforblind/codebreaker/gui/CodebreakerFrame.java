package gamesforblind.codebreaker.gui;

import gamesforblind.enums.GameMenuItem;
import gamesforblind.codebreaker.CodebreakerGame;
import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.codebreaker.gui.listener.CodebreakerMenuItemListener;
import gamesforblind.codebreaker.gui.listener.CodebreakerWindowListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static gamesforblind.Constants.FRAME_DIMENSION;

/**
 * Class that creates the Mastermind GUI, adds the listeners to that GUI, and updates the Mastermind state of the GUI.
 */
public class CodebreakerFrame extends JFrame {
    private final JFrame frame;

    /**
     * A reference to the panel that contains most of the Mastermind GUI code.
     */
    private final CodebreakerPanel codebreakerPanel;

    /**
     * Creates a new MastermindFrame.
     *
     * @param codebreakerGame The current Mastermind game.
     * @param initialState   The initial state of the game, which is used to initialize the MastermindPanel.
     * @param playbackMode   true if loading saved game from logs (otherwise, false).
     */
    public CodebreakerFrame(
            @NotNull CodebreakerGame codebreakerGame,
            @NotNull CodebreakerState initialState,
            boolean playbackMode
    ) {
        this.codebreakerPanel = new CodebreakerPanel(initialState);
        this.frame = new JFrame("Mastermind");

        this.initializeGui(codebreakerGame);

        if (!playbackMode) {
            this.frame.addWindowListener(new CodebreakerWindowListener(codebreakerGame));
        }
    }

    /**
     * Used for creating the on-screen GUI.
     * This will start at a dimension of 500 by 500 pixels, but is fully resizable by dragging the window.
     *
     * @param codebreakerGame The Mastermind game.
     */
    private void initializeGui(@NotNull CodebreakerGame codebreakerGame) {
        this.frame.add(this.codebreakerPanel);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        this.frame.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        // Add menu to menu bar
        menuBar.add(this.getInitializedMenu(codebreakerGame));

        // Add menu bar to frame
        this.frame.setJMenuBar(menuBar);
    }

    /**
     * Sets up the {@link JMenu}. Adds all the {@link JMenuItem}s & {@link CodebreakerMenuItemListener} to these items.
     *
     * @param codebreakerGame The current Sudoku game.
     * @return The initialized {@link JMenu} for the Sudoku game.
     */
    private JMenu getInitializedMenu(@NotNull CodebreakerGame codebreakerGame) {
        this.frame.add(this.codebreakerPanel);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        this.frame.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        // Add menu bar to frame
        this.frame.setJMenuBar(menuBar);
        JMenu mainMenu = new JMenu("Menu");
        CodebreakerMenuItemListener codebreakerMenuItemListener = new CodebreakerMenuItemListener(codebreakerGame);

        /* Add menu items to menu. */
        for (GameMenuItem gameMenuItem : GameMenuItem.MASTERMIND_MENU_ITEMS) {
            JMenuItem jMenuItem = new JMenuItem(gameMenuItem.toString());
            jMenuItem.addActionListener(codebreakerMenuItemListener);
            mainMenu.add(jMenuItem);
        }

        return mainMenu;
    }

    /**
     * Calls repaint() on the enclosed Mastermind panel reference.
     */
    public void repaintMastermindPanel() {
        this.codebreakerPanel.repaint();
    }

    /**
     * Getter for the enclosed {@link JFrame}'s bounds. Used to determine proper sizing of elements on the GUI.
     *
     * @return A {@link Rectangle} containing the {@link JFrame} bounds.
     */
    public Rectangle getFrameBounds() {
        return new Rectangle(this.frame.getBounds());
    }

    /**
     * Closes the previous game's {@link JFrame}
     */
    public void closeFrames() {
        this.frame.setVisible(false);
        this.frame.dispose();
    }
}
