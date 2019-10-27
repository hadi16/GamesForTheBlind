package gamesforblind.loader.action;

import gamesforblind.loader.enums.SelectedGame;

/**
 * Action for when the user makes a menu selection in the loader, either with the keyboard or mouse.
 */
public class LoaderGameSelectionAction extends LoaderAction {
    /**
     * Signifies which game the user has selected (e.g. Sudoku).
     * If the back button is selected, this is set to SelectedGame.NONE
     */
    private final SelectedGame selectedGame;

    /**
     * Creates a new {@link LoaderGameSelectionAction}
     *
     * @param selectedGame Which game the user has selected. If the user wishes to go back, this is NONE.
     */
    public LoaderGameSelectionAction(SelectedGame selectedGame) {
        this.selectedGame = selectedGame;
    }

    /**
     * Getter for the selectedGame instance variable.
     *
     * @return Which game the user has selected. If the user wishes to go back, this is NONE.
     */
    public SelectedGame getSelectedGame() {
        return this.selectedGame;
    }
}
