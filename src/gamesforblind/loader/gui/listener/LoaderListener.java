package gamesforblind.loader.gui.listener;

import gamesforblind.loader.GameLoader;
import gamesforblind.loader.action.LoaderExitAction;
import gamesforblind.loader.action.LoaderGameSelectionAction;
import gamesforblind.loader.action.LoaderSudokuSelectionAction;
import gamesforblind.loader.enums.SelectedGame;
import gamesforblind.loader.enums.SudokuType;

import static gamesforblind.Constants.*;

/**
 * An abstract class that all listeners in the loader GUI inherit from. This is done to
 * maintain consistency between the mouse & keyboard selection actions in the loader GUI.
 */
public abstract class LoaderListener {
    /**
     * The game loader, which is needed to send actions that the user has made.
     */
    protected final GameLoader gameLoader;

    /**
     * Creates a new {@link LoaderListener}. This is never called directly ({@link LoaderListener} is abstract).
     *
     * @param gameLoader The game loader for the program.
     */
    protected LoaderListener(GameLoader gameLoader) {
        this.gameLoader = gameLoader;
    }

    /**
     * Sends a selection action to the game with what button was selected.
     * This is done either when:
     * a) user clicks a button in the loader GUI with the mouse.
     * b) user selects a button in the loader GUI with the space bar.
     *
     * @param buttonText The text of the selected button in the loader GUI.
     */
    protected void sendSelectionActionBasedOnButtonText(String buttonText) {
        switch (buttonText) {
            // Cases for when the user can select between playing Sudoku & exiting the program.
            case PLAY_SUDOKU_BUTTON:
                this.gameLoader.receiveAction(new LoaderGameSelectionAction(SelectedGame.SUDOKU));
                break;
            case EXIT_BUTTON:
                this.gameLoader.receiveAction(new LoaderExitAction());
                break;

            // Cases for when the user can select between going back, a new 4x4 Sudoku game, or a new 9x9 Sudoku game.
            case BACK_BUTTON:
                this.gameLoader.receiveAction(new LoaderGameSelectionAction(SelectedGame.NONE));
                break;
            case FOUR_BY_FOUR_SUDOKU_BUTTON:
                this.gameLoader.receiveAction(new LoaderSudokuSelectionAction(SudokuType.FOUR_BY_FOUR));
                break;
            case NINE_BY_NINE_SUDOKU_BUTTON:
                this.gameLoader.receiveAction(new LoaderSudokuSelectionAction(SudokuType.NINE_BY_NINE));
                break;
        }
    }
}
