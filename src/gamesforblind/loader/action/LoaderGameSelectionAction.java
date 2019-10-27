package gamesforblind.loader.action;

import gamesforblind.loader.GameType;

public class LoaderGameSelectionAction extends LoaderAction {
    private final GameType selectedGame;

    public LoaderGameSelectionAction(GameType selectedGame) {
        this.selectedGame = selectedGame;
    }

    public GameType getSelectedGame() {
        return this.selectedGame;
    }
}
