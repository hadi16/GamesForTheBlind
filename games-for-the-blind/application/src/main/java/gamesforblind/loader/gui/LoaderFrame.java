package gamesforblind.loader.gui;

import gamesforblind.ProgramArgs;
import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.InterfaceType;
import gamesforblind.enums.SelectedGame;
import gamesforblind.loader.GameLoader;
import gamesforblind.loader.gui.listener.LoaderActionListener;
import gamesforblind.loader.gui.listener.LoaderKeyboardListener;
import gamesforblind.loader.gui.listener.LoaderWindowListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import static gamesforblind.Constants.*;

/**
 * Contains the logic for the loader GUI.
 */
public class LoaderFrame extends JFrame implements ComponentListener {
    /**
     * Used for the button text of the currently highlighted button in the GUI.
     */
    private static final Color DARK_GOLD = new Color(218, 165, 32);

    /**
     * When in the main screen, contains buttons in following order: "PLAY_SUDOKU","PLAY_CODEBREAKER", "EXIT"
     * When in the Sudoku screen, contains buttons in following order: "BACK", "4x4", "9x9"
     */
    private final ArrayList<JButton> relevantButtons = new ArrayList<>();

    /**
     * Reference to the mouse listener for the loader GUI.
     */
    private final LoaderActionListener loaderActionListener;

    /**
     * Reference to keyboard listener for loader GUI. Reference needed to remove it when loader GUI closes.
     */
    private final LoaderKeyboardListener loaderKeyboardListener;

    private final ProgramArgs programArgs;
    private final LoaderWindowListener loaderWindowListener;
    /**
     * JFrame for loader GUI. When moving between screens in GUI, this value is reassigned & old one is disposed.
     */
    private JFrame loaderFrame;
    /**
     * Holds the index of the currently highlighted button.
     * In main screen, 0 --> "PLAY SUDOKU", 1 --> "CODEBREAKER", 2 --> "EXIT"
     * In Sudoku screen, 0 --> "BACK", 1 --> "4x4", 2 --> "9x9"
     */
    private int highlightedButtonIndex = 0;

    /**
     * Creates a new LoaderFrame.
     *
     * @param gameLoader  The game loader for the program, which is needed for mouse & keyboard listeners.
     * @param programArgs The program arguments that were passed.
     */
    public LoaderFrame(@NotNull GameLoader gameLoader, @NotNull ProgramArgs programArgs) {
        this.programArgs = programArgs;

        // Initialize the listener instance variables & add the keyboard listener (mouse listener set later).
        this.loaderActionListener = new LoaderActionListener(gameLoader);
        this.loaderWindowListener = new LoaderWindowListener(gameLoader);
        this.loaderKeyboardListener = new LoaderKeyboardListener(gameLoader, this);

        if (!programArgs.isPlaybackMode()) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.loaderKeyboardListener);
        }

        this.setLoaderGuiBasedOnSelectedGame(SelectedGame.NONE);
    }

    /**
     * Helper method to get a JButton for the loader GUI.
     *
     * @param buttonText    The text of the button.
     * @param preferredSize The requested size of the button.
     * @return The created JButton for the loader GUI.
     */
    private JButton getUIButton(@NotNull String buttonText, @NotNull Dimension preferredSize) {
        // Make the size of the button font dependent on the size of the frame.
        final Font BUTTON_FONT = new Font("Arial", Font.BOLD, FRAME_DIMENSION / 13);

        JButton button = new JButton(buttonText);
        if (!this.programArgs.isPlaybackMode()) {
            button.addActionListener(this.loaderActionListener);
        }

        // Needed to set the background color of the button (for highlighted buttons in the GUI).
        button.setOpaque(true);

        button.setFont(BUTTON_FONT);
        button.setPreferredSize(preferredSize);
        return button;
    }

    /**
     * For level selection
     */
    private ArrayList<JComponent> getInitializedOptionsPanelAsList(
            ArrayList<String> buttonNameList, Function<Integer, Dimension> getButtonDimension
    ) {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout());

        buttonNameList.forEach(buttonName -> {
            final Dimension buttonDimension = getButtonDimension.apply(buttonNameList.size());
            optionsPanel.add(this.getUIButton(buttonName, buttonDimension));
        });

        return new ArrayList<>(Collections.singletonList(optionsPanel));
    }

    /**
     * Gets the game component that is variable depending on the currently selected game (the upper portion of the GUI).
     *
     * @param selectedGame The currently selected game. If set to NONE, I am in the main screen of the loader GUI.
     * @return The upper portion of the loader GUI.
     */
    private ArrayList<JComponent> getSelectedGameComponent(@NotNull SelectedGame selectedGame) {
        Function<Integer, Dimension> getButtonDimension = (numberOfButtons) -> {
            return new Dimension(FRAME_DIMENSION / numberOfButtons, FRAME_DIMENSION / 3);
        };
        JPanel gamePanel = new JPanel();

        // Case 1: I am in the main screen (return list of JButtons with "PLAY SUDOKU" & "PLAY CODEBREAKER").
        if (selectedGame == SelectedGame.NONE) {
            ArrayList<JComponent> gameButtonList = new ArrayList<>();

            final String[] GAME_BUTTONS = new String[]{PLAY_SUDOKU_BUTTON, PLAY_CODEBREAKER_BUTTON};
            for (String gameButtonText : GAME_BUTTONS) {
                JButton newButton = this.getUIButton(gameButtonText, getButtonDimension.apply(GAME_BUTTONS.length));
                gameButtonList.add(newButton);
                gamePanel.add(newButton);
            }

            return gameButtonList;
        }

        // Case 2: I am in the Sudoku selection screen (return a JPanel with "BACK", "4x4", and "9x9" buttons).
        if (selectedGame == SelectedGame.SUDOKU) {
            ArrayList<String> buttonNameList = new ArrayList<>(Arrays.asList(
                    BACK_BUTTON, FOUR_BY_FOUR_SUDOKU_BUTTON, SIX_BY_SIX_SUDOKU_BUTTON, NINE_BY_NINE_SUDOKU_BUTTON
            ));

            // 6x6 boards are unsupported in the block selection interface.
            if (this.programArgs.getSelectedInterfaceType() == InterfaceType.BLOCK_SELECTION_INTERFACE) {
                buttonNameList.remove(SIX_BY_SIX_SUDOKU_BUTTON);
            }

            return this.getInitializedOptionsPanelAsList(buttonNameList, getButtonDimension);
        }

        // Case 3: I am in the Codebreaker selection screen (return a JPanel with "BACK", "4","5", "6" buttons).
        if (selectedGame == SelectedGame.CODEBREAKER) {
            ArrayList<String> buttonNameList = new ArrayList<>(Arrays.asList(
                    BACK_BUTTON, FOUR_CODEBREAKER_BUTTON, FIVE_CODEBREAKER_BUTTON, SIX_CODEBREAKER_BUTTON
            ));

            return this.getInitializedOptionsPanelAsList(buttonNameList, getButtonDimension);
        }

        // Should never be thrown, but needed to prevent compilation error.
        throw new IllegalArgumentException("Game type is invalid!");
    }

    /**
     * Dispose of the old loaderFrame (if one already exists) & reinitialize a new frame based on the game
     * that the user has selected. If selectedGame is set to NONE, just show the main selection screen.
     *
     * @param selectedGame The game that the user has selected.
     */
    public void setLoaderGuiBasedOnSelectedGame(@NotNull SelectedGame selectedGame) {
        // Dispose the old frame.
        if (this.loaderFrame != null) {
            this.loaderFrame.setVisible(false);
            this.loaderFrame.dispose();
        }

        this.loaderFrame = new JFrame("Game Loader Menu");

        ArrayList<JComponent> selectedGameComponents = this.getSelectedGameComponent(selectedGame);
        selectedGameComponents.forEach(component -> component.setVisible(true));

        JButton exitButton = this.getUIButton(EXIT_BUTTON, new Dimension(FRAME_DIMENSION, FRAME_DIMENSION / 3));

        // Disable the exit button when in a game selection screen (e.g. Sudoku's).
        exitButton.setEnabled(selectedGame == SelectedGame.NONE);

        //if game has been selected hide exit button
        if (selectedGame != SelectedGame.NONE) {
            exitButton.setVisible(false);
        }

        // The selectedGameComponent is on top of the GUI
        // & the exit button is on the bottom of the GUI.
        Container frameContainer = this.loaderFrame.getContentPane();

        // Add 1 to rows to account for exit button, only one column for game selection menu
        frameContainer.setLayout(new GridLayout(selectedGameComponents.size() + 1, 1));

        if (selectedGameComponents.size() > 2) {
            throw new IllegalArgumentException("Too many 'PLAY ____' buttons!");
        }

        for (JComponent selectedGameComponent : selectedGameComponents) {
            frameContainer.add(selectedGameComponent);
        }
        frameContainer.add(exitButton);

        // Call the logger save when the close button is clicked.
        this.loaderFrame.addWindowListener(this.loaderWindowListener);
        this.loaderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.loaderFrame.pack();
        this.loaderFrame.setSize((int) (FRAME_DIMENSION * 1.7), FRAME_DIMENSION);
        this.loaderFrame.setVisible(true);

        this.highlightedButtonIndex = 0;
        this.setRelevantButtons(selectedGame);
        this.setHighlightedButtonColors(false);
    }

    /**
     * Set or reset the highlighted button colors in the loader GUI.
     *
     * @param reset If true, reset the currently highlighted button colors. Otherwise, highlight them.
     */
    private void setHighlightedButtonColors(boolean reset) {
        JButton highlightedButton = this.relevantButtons.get(this.highlightedButtonIndex);

        if (reset) {
            // Case 1: reset the button colors
            highlightedButton.setFocusPainted(false);
            highlightedButton.setForeground(Color.BLACK);
            highlightedButton.setBackground(null);
        } else {
            // Case 2: highlight the button colors.
            highlightedButton.setFocusPainted(true);
            highlightedButton.setForeground(DARK_GOLD);
            highlightedButton.setBackground(Color.YELLOW);
        }

        this.loaderFrame.repaint();
    }

    /**
     * Resets the currently relevant buttons in the GUI.
     *
     * @param selectedGame The currently selected game.
     */
    private void setRelevantButtons(@NotNull SelectedGame selectedGame) {
        this.relevantButtons.clear();

        Component[] frameContainerComponents = this.loaderFrame.getContentPane().getComponents();
        if (frameContainerComponents.length == 0) {
            return;
        }

        // Case 1: I am in the main screen of the loader GUI.
        if (selectedGame == SelectedGame.NONE) {
            // Just add each of the content pane's child components to the list of relevant buttons.
            for (Component component : frameContainerComponents) {
                if (component instanceof JButton) {
                    this.relevantButtons.add((JButton) component);
                }
            }
        }

        // Case 2: I am in the Sudoku or Codebreaker game selection screen.
        if (selectedGame == SelectedGame.SUDOKU || selectedGame == SelectedGame.CODEBREAKER) {
            // I only care about the first child component (the JPanel w/ "BACK", etc.)
            Component menuComponent = frameContainerComponents[0];
            if (menuComponent instanceof JPanel) {
                JPanel menuPanel = (JPanel) menuComponent;

                // Add each of the JPanel's child components to the list of relevant buttons.
                for (Component component : menuPanel.getComponents()) {
                    if (component instanceof JButton) {
                        this.relevantButtons.add((JButton) component);
                    }
                }
            }
        }
    }

    /**
     * Called when the user closes the loader GUI & opens one of the games (e.g. 4x4 Sudoku).
     */
    public void closeLoaderFrames() {
        // Need to explicitly remove the current keyboard listener. Otherwise, will still be attached to the game GUI.
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.loaderKeyboardListener);

        this.loaderFrame.setVisible(false);
        this.loaderFrame.dispose();
    }

    /**
     * Changes the highlighted button index based on which arrow key was pressed by the user.
     *
     * @param arrowKeyDirection The arrow key that was pressed by the user.
     */
    public void changeHighlightedButton(@NotNull ArrowKeyDirection arrowKeyDirection) {
        // Reset the old highlighted button colors.
        this.setHighlightedButtonColors(true);

        switch (arrowKeyDirection) {
            // Treat UP & LEFT arrow keys the same (decrement highlighted button index).
            case UP:
            case LEFT:
                if (this.highlightedButtonIndex == 0) {
                    this.highlightedButtonIndex = this.relevantButtons.size() - 1;
                } else {
                    this.highlightedButtonIndex--;
                }
                break;

            // Treat DOWN & RIGHT arrow keys the same (increment highlighted button index).
            case DOWN:
            case RIGHT:
                if (this.highlightedButtonIndex == this.relevantButtons.size() - 1) {
                    this.highlightedButtonIndex = 0;
                } else {
                    this.highlightedButtonIndex++;
                }
                break;
        }

        // Set the new highlighted button colors.
        this.setHighlightedButtonColors(false);
    }

    /**
     * @return The text of the currently highlighted button in the GUI.
     */
    public String getCurrentlyHighlightedButtonText() {
        return this.relevantButtons.get(this.highlightedButtonIndex).getText();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        this.loaderFrame.repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
