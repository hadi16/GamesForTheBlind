package gamesforblind.codebreaker.gui;

import gamesforblind.codebreaker.CodebreakerGame;
import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.codebreaker.gui.listener.CodebreakerKeyboardListener;
import gamesforblind.codebreaker.gui.listener.CodebreakerMenuItemListener;
import gamesforblind.codebreaker.gui.listener.CodebreakerMouseListener;
import gamesforblind.codebreaker.gui.listener.CodebreakerWindowListener;
import gamesforblind.enums.GameMenuItem;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static gamesforblind.Constants.FRAME_DIMENSION;

/**
 * Class that creates the Codebreaker GUI, adds the listeners to that GUI, and updates the Codebreaker state of the GUI.
 */
public class CodebreakerFrame extends JFrame {
    private final CodebreakerState codebreakerState;
    private final JFrame frame;

    /**
     * A reference to the panel that contains most of the Codebreaker GUI code.
     */
    private final CodebreakerPanel codebreakerPanel;

    /**
     * Creates a new CodebreakerFrame.
     *
     * @param codebreakerGame The current Codebreaker game.
     * @param initialState    The initial state of the game, which is used to initialize the CodebreakerPanel.
     * @param playbackMode    true if loading saved game from logs (otherwise, false).
     */
    public CodebreakerFrame(
            @NotNull CodebreakerGame codebreakerGame,
            @NotNull CodebreakerState initialState,
            boolean playbackMode
    ) {
        this.codebreakerState = initialState;
        this.codebreakerPanel = new CodebreakerPanel(initialState);
        this.frame = new JFrame("Codebreaker");

        this.initializeGui(codebreakerGame);

        if (!playbackMode) {
            this.frame.addWindowListener(new CodebreakerWindowListener(codebreakerGame));
            this.frame.addKeyListener(new CodebreakerKeyboardListener(codebreakerGame));
            this.frame.addMouseListener(new CodebreakerMouseListener(codebreakerGame));
        }
    }

    /**
     * Used for creating the on-screen GUI.
     * This will start at a dimension of 500 by 500 pixels, but is fully resizable by dragging the window.
     *
     * @param codebreakerGame The Codebreaker game.
     */
    private void initializeGui(@NotNull CodebreakerGame codebreakerGame) {
        this.frame.add(this.codebreakerPanel);
        this.frame.getContentPane().add(this.codebreakerPanel);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final boolean gameOver = CodebreakerFrame.this.codebreakerState.isGameOver();
                if (gameOver) {
                    this.cancel();
                } else {
                    CodebreakerFrame.this.repaintCodebreakerPanel();
                }
            }
        }, 1000, 1000);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize((int) (1.5 * FRAME_DIMENSION), FRAME_DIMENSION);
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
     * @param codebreakerGame The current Codebreaker game.
     * @return The initialized {@link JMenu} for the Codebreaker game.
     */
    private JMenu getInitializedMenu(@NotNull CodebreakerGame codebreakerGame) {
        this.frame.add(this.codebreakerPanel);

        JMenuBar menuBar = new JMenuBar();

        // Add menu bar to frame
        this.frame.setJMenuBar(menuBar);
        JMenu mainMenu = new JMenu("Menu");
        CodebreakerMenuItemListener codebreakerMenuItemListener = new CodebreakerMenuItemListener(codebreakerGame);

        /* Add menu items to menu. */
        for (GameMenuItem gameMenuItem : GameMenuItem.CODEBREAKER_MENU_ITEMS) {
            JMenuItem jMenuItem = new JMenuItem(gameMenuItem.toString());
            jMenuItem.addActionListener(codebreakerMenuItemListener);
            mainMenu.add(jMenuItem);
        }

        return mainMenu;
    }

    /**
     * Calls repaint() on the enclosed Codebreaker panel reference.
     */
    public void repaintCodebreakerPanel() {
        this.codebreakerPanel.repaint();
    }

    /**
     * Closes the previous game's {@link JFrame}
     */
    public void closeFrames() {
        this.frame.setVisible(false);
        this.frame.dispose();
        this.codebreakerPanel.getPopUpFrame().setVisible(false);
        this.codebreakerPanel.getPopUpFrame().dispose();
    }

    public Optional<Point> getMouseSelectedPoint(Point mousePoint) {
        return this.codebreakerPanel.getMouseSelectedPoint(mousePoint);
    }
}
