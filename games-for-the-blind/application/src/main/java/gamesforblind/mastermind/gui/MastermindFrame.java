package gamesforblind.mastermind.gui;

import gamesforblind.enums.GameMenuItem;
import gamesforblind.mastermind.MastermindGame;
import gamesforblind.mastermind.MastermindState;
import gamesforblind.mastermind.gui.listener.MastermindMenuItemListener;
import gamesforblind.mastermind.gui.listener.MastermindWindowListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static gamesforblind.Constants.FRAME_DIMENSION;

/**
 * Class that creates the Mastermind GUI, adds the listeners to that GUI, and updates the Mastermind state of the GUI.
 */
public class MastermindFrame extends JFrame {
    private final JFrame frame;

    /**
     * A reference to the panel that contains most of the Mastermind GUI code.
     */
    private final MastermindPanel mastermindPanel;

    /**
     * Creates a new MastermindFrame.
     *
     * @param mastermindGame The current Mastermind game.
     * @param initialState   The initial state of the game, which is used to initialize the MastermindPanel.
     * @param playbackMode   true if loading saved game from logs (otherwise, false).
     */
    public MastermindFrame(
            @NotNull MastermindGame mastermindGame,
            @NotNull MastermindState initialState,
            boolean playbackMode
    ) {
        this.mastermindPanel = new MastermindPanel(initialState);
        this.frame = new JFrame("Mastermind");

        this.initializeGui(mastermindGame);

        if (!playbackMode) {
            this.frame.addWindowListener(new MastermindWindowListener(mastermindGame));
        }
    }

    /**
     * Used for creating the on-screen GUI.
     * This will start at a dimension of 500 by 500 pixels, but is fully resizable by dragging the window.
     *
     * @param mastermindGame The Mastermind game.
     */
    private void initializeGui(@NotNull MastermindGame mastermindGame) {
        this.frame.add(this.mastermindPanel);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        this.frame.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        // Add menu to menu bar
        menuBar.add(this.getInitializedMenu(mastermindGame));

        // Add menu bar to frame
        this.frame.setJMenuBar(menuBar);

    }

    /**
     * Sets up the {@link JMenu}. Adds all the {@link JMenuItem}s & {@link MastermindMenuItemListener} to these items.
     *
     * @param mastermindGame The current Sudoku game.
     * @return The initialized {@link JMenu} for the Sudoku game.
     */
    private JMenu getInitializedMenu(@NotNull MastermindGame mastermindGame) {
        this.frame.add(this.mastermindPanel);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        this.frame.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        // Add menu bar to frame
        this.frame.setJMenuBar(menuBar);
        JMenu mainMenu = new JMenu("Menu");
        MastermindMenuItemListener mastermindMenuItemListener = new MastermindMenuItemListener(mastermindGame);

        /* Add menu items to menu. */
        for (GameMenuItem gameMenuItem : GameMenuItem.MASTERMIND_MENU_ITEMS) {
            JMenuItem jMenuItem = new JMenuItem(gameMenuItem.toString());
            jMenuItem.addActionListener(mastermindMenuItemListener);
            mainMenu.add(jMenuItem);
        }

        return mainMenu;
    }

    /**
     * Calls repaint() on the enclosed Mastermind panel reference.
     */
    public void repaintMastermindPanel() {
        this.mastermindPanel.repaint();
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
