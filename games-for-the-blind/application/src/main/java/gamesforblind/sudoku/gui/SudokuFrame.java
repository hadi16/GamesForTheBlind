package gamesforblind.sudoku.gui;

import gamesforblind.enums.GameMenuItem;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.gui.listener.SudokuKeyboardListener;
import gamesforblind.sudoku.gui.listener.SudokuMenuItemListener;
import gamesforblind.sudoku.gui.listener.SudokuMouseListener;
import gamesforblind.sudoku.gui.listener.SudokuWindowListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static gamesforblind.Constants.FRAME_DIMENSION;

/**
 * Class that creates the Sudoku GUI, adds the listeners to that GUI, and updates the Sudoku state of the GUI.
 */
public class SudokuFrame extends JFrame {
    private final SudokuState sudokuState;
    private final JFrame frame;

    /**
     * A reference to the panel that contains most of the Sudoku GUI code.
     */
    private final SudokuPanel sudokuPanel;

    /**
     * Creates a new SudokuFrame.
     *
     * @param sudokuGame   The current Sudoku game.
     * @param initialState The initial state of the game, which is used to initialize the SudokuPanel.
     * @param sudokuType   Whether the Sudoku board is a 4x4, 6x6, or 9x9.
     * @param playbackMode true if loading saved game from logs (otherwise, false).
     */
    public SudokuFrame(
            @NotNull SudokuGame sudokuGame,
            @NotNull SudokuState initialState,
            @NotNull SudokuType sudokuType,
            boolean playbackMode
    ) {
        this.sudokuState = initialState;
        this.sudokuPanel = new SudokuPanel(initialState);
        this.frame = new JFrame("Sudoku");

        this.initializeGui(sudokuGame);

        // If we are in playback mode, I don't want any new mouse clicks or keyboard presses to be registered.
        if (!playbackMode) {
            this.frame.addMouseListener(new SudokuMouseListener(sudokuGame, this, sudokuType));
            this.frame.addKeyListener(new SudokuKeyboardListener(initialState.getSudokuKeyboardInterface(), sudokuGame));
            this.frame.addWindowListener(new SudokuWindowListener(sudokuGame));
        }
    }

    /**
     * Used for creating the on-screen GUI.
     * This will start at a dimension of 500 by 500 pixels, but is fully resizable by dragging the window.
     *
     * @param sudokuGame The Sudoku game.
     */
    private void initializeGui(@NotNull SudokuGame sudokuGame) {
        this.frame.add(this.sudokuPanel);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final boolean gameOver = SudokuFrame.this.sudokuState.isGameOver();
                if (gameOver) {
                    this.cancel();
                } else {
                    SudokuFrame.this.repaintSudokuPanel();
                }
            }
        }, 1000, 1000);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize((int) (1.4 * FRAME_DIMENSION), FRAME_DIMENSION);
        this.frame.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        // Add menu to menu bar
        menuBar.add(this.getInitializedMenu(sudokuGame));

        // Add menu bar to frame
        this.frame.setJMenuBar(menuBar);
    }

    /**
     * Sets up the {@link JMenu}. Adds all the {@link JMenuItem}s & {@link SudokuMenuItemListener} to these items.
     *
     * @param sudokuGame The current Sudoku game.
     * @return The initialized {@link JMenu} for the Sudoku game.
     */
    private JMenu getInitializedMenu(@NotNull SudokuGame sudokuGame) {
        JMenu mainMenu = new JMenu("Menu");
        SudokuMenuItemListener sudokuMenuItemListener = new SudokuMenuItemListener(sudokuGame);

        /* Add menu items to menu. */
        for (GameMenuItem gameMenuItem : GameMenuItem.SUDOKU_MENU_ITEMS) {
            JMenuItem jMenuItem = new JMenuItem(gameMenuItem.toString());
            jMenuItem.addActionListener(sudokuMenuItemListener);
            mainMenu.add(jMenuItem);
        }

        return mainMenu;
    }

    /**
     * Calls repaint() on the enclosed Sudoku panel reference.
     */
    public void repaintSudokuPanel() {
        this.sudokuPanel.repaint();
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
