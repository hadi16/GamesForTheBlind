package gamesforblind.loader.gui.listener;

import gamesforblind.loader.GameLoader;
import gamesforblind.loader.GameType;
import gamesforblind.loader.SudokuType;
import gamesforblind.loader.action.LoaderExitAction;
import gamesforblind.loader.action.LoaderGameSelectionAction;
import gamesforblind.loader.action.LoaderSudokuSelectionAction;
import gamesforblind.loader.gui.LoaderGuiConstants;

public abstract class LoaderListener {
    protected final GameLoader gameLoader;

    protected LoaderListener(GameLoader gameLoader) {
        this.gameLoader = gameLoader;
    }

    protected void sendActionBasedOnButtonText(String buttonText) {
        switch (buttonText) {
            case LoaderGuiConstants.PLAY_SUDOKU_BUTTON:
                this.gameLoader.receiveAction(new LoaderGameSelectionAction(GameType.SUDOKU));
                break;
            case LoaderGuiConstants.EXIT_BUTTON:
                this.gameLoader.receiveAction(new LoaderExitAction());
                break;
            case LoaderGuiConstants.BACK_BUTTON:
                this.gameLoader.receiveAction(new LoaderGameSelectionAction(GameType.NONE));
                break;
            case LoaderGuiConstants.FOUR_BY_FOUR_SUDOKU_BUTTON:
                this.gameLoader.receiveAction(new LoaderSudokuSelectionAction(SudokuType.FOUR_BY_FOUR));
                break;
            case LoaderGuiConstants.NINE_BY_NINE_SUDOKU_BUTTON:
                this.gameLoader.receiveAction(new LoaderSudokuSelectionAction(SudokuType.NINE_BY_NINE));
                break;
        }
    }
}
