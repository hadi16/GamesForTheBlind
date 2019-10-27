package gamesforblind.loader.gui;

import gamesforblind.loader.ArrowKeyDirection;
import gamesforblind.loader.GameLoader;
import gamesforblind.loader.GameType;
import gamesforblind.loader.gui.listener.LoaderActionListener;
import gamesforblind.loader.gui.listener.LoaderKeyboardListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static gamesforblind.Constants.FRAME_DIMENSION;

public class LoaderFrame extends JFrame {
    private static final Color DARK_GOLD = new Color(218, 165, 32);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, FRAME_DIMENSION / 11);

    private final ArrayList<JButton> relevantButtons = new ArrayList<>();
    private final LoaderActionListener loaderActionListener;
    private final LoaderKeyboardListener loaderKeyboardListener;
    private JFrame loaderFrame;

    private int highlightedButtonIndex = 0;

    public LoaderFrame(GameLoader gameLoader) {
        this.loaderActionListener = new LoaderActionListener(gameLoader);
        this.loaderKeyboardListener = new LoaderKeyboardListener(gameLoader, this);

        this.loaderFrame = new JFrame();


        this.setFrameBasedOnSelectedGame(GameType.NONE);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.loaderKeyboardListener);
    }

    private JButton getUIButton(String buttonText, Dimension preferredSize) {
        JButton uiButton = new JButton(buttonText);
        uiButton.addActionListener(this.loaderActionListener);
        uiButton.setOpaque(true);
        uiButton.setFont(BUTTON_FONT);
        uiButton.setPreferredSize(preferredSize);
        return uiButton;
    }

    private JComponent getSelectedGameComponent(GameType selectedGameType) {
        final int HEIGHT_DIVISOR = 3;

        if (selectedGameType == GameType.NONE) {
            return this.getUIButton(
                    LoaderGuiConstants.PLAY_SUDOKU_BUTTON,
                    new Dimension(FRAME_DIMENSION, FRAME_DIMENSION / HEIGHT_DIVISOR)
            );
        }

        if (selectedGameType == GameType.SUDOKU) {
            final Dimension BUTTON_SIZE = new Dimension(
                    FRAME_DIMENSION / 3, FRAME_DIMENSION / HEIGHT_DIVISOR
            );
            JPanel sudokuOptionsPanel = new JPanel();
            sudokuOptionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, FRAME_DIMENSION / 10, 0));

            sudokuOptionsPanel.add(this.getUIButton(LoaderGuiConstants.BACK_BUTTON, BUTTON_SIZE));
            sudokuOptionsPanel.add(this.getUIButton(LoaderGuiConstants.FOUR_BY_FOUR_SUDOKU_BUTTON, BUTTON_SIZE));
            sudokuOptionsPanel.add(this.getUIButton(LoaderGuiConstants.NINE_BY_NINE_SUDOKU_BUTTON, BUTTON_SIZE));

            sudokuOptionsPanel.requestFocusInWindow();
            sudokuOptionsPanel.revalidate();
            return sudokuOptionsPanel;
        }

        throw new IllegalArgumentException("Game type is invalid!");
    }

    private void setFrameBasedOnSelectedGame(GameType selectedGameType) {
        this.loaderFrame.setVisible(false);
        this.loaderFrame.dispose();

        this.loaderFrame = new JFrame();

        JComponent selectedGameComponent = this.getSelectedGameComponent(selectedGameType);
        selectedGameComponent.setVisible(true);

        JButton exitButton = this.getUIButton(
                LoaderGuiConstants.EXIT_BUTTON, new Dimension(FRAME_DIMENSION, FRAME_DIMENSION / 3)
        );
        exitButton.setEnabled(selectedGameType == GameType.NONE);

        Container frameContainer = this.loaderFrame.getContentPane();
        frameContainer.add(selectedGameComponent, BorderLayout.PAGE_START);
        frameContainer.add(exitButton, BorderLayout.PAGE_END);

        this.loaderFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.loaderFrame.setSize((int) (FRAME_DIMENSION * 1.7), FRAME_DIMENSION);
        this.loaderFrame.setVisible(true);

        this.highlightedButtonIndex = 0;
        this.setRelevantButtons(selectedGameType);
        this.setHighlightedButtonColors(false);
    }

    private void setHighlightedButtonColors(boolean reset) {
        JButton highlightedButton = this.relevantButtons.get(this.highlightedButtonIndex);

        if (reset) {
            highlightedButton.setForeground(Color.BLACK);
            highlightedButton.setBackground(null);
        } else {
            highlightedButton.setForeground(DARK_GOLD);
            highlightedButton.setBackground(Color.YELLOW);
        }

        this.loaderFrame.repaint();
    }

    private void setRelevantButtons(GameType selectedGameType) {
        this.relevantButtons.clear();
        Component[] frameContainerComponents = this.loaderFrame.getContentPane().getComponents();

        if (frameContainerComponents.length == 0) {
            return;
        }

        if (selectedGameType == GameType.NONE) {
            for (Component component : frameContainerComponents) {
                if (component instanceof JButton) {
                    this.relevantButtons.add((JButton) component);
                }
            }
        }

        if (selectedGameType == GameType.SUDOKU) {
            Component sudokuMenuComponent = frameContainerComponents[0];
            if (sudokuMenuComponent instanceof JPanel) {
                JPanel sudokuMenuPanel = (JPanel) sudokuMenuComponent;
                for (Component component : sudokuMenuPanel.getComponents()) {
                    if (component instanceof JButton) {
                        this.relevantButtons.add((JButton) component);
                    }
                }
            }
        }
    }

    public void closeLoaderFrames() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.loaderKeyboardListener);
        this.loaderFrame.setVisible(false);
        this.loaderFrame.dispose();
    }

    public void setSelectedGameComponent(GameType selectedGameType) {
        this.setFrameBasedOnSelectedGame(selectedGameType);
    }

    public void changeHighlightedButton(ArrowKeyDirection arrowKeyDirection) {
        this.setHighlightedButtonColors(true);

        switch (arrowKeyDirection) {
            case UP:
            case LEFT:
                if (this.highlightedButtonIndex == 0) {
                    this.highlightedButtonIndex = this.relevantButtons.size() - 1;
                } else {
                    this.highlightedButtonIndex--;
                }
                break;
            case DOWN:
            case RIGHT:
                if (this.highlightedButtonIndex == this.relevantButtons.size() - 1) {
                    this.highlightedButtonIndex = 0;
                } else {
                    this.highlightedButtonIndex++;
                }
                break;
        }

        this.setHighlightedButtonColors(false);
    }

    public String getCurrentlyHighlightedButtonText() {
        return this.relevantButtons.get(this.highlightedButtonIndex).getText();
    }
}
