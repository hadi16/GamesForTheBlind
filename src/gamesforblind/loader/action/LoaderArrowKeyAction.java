package gamesforblind.loader.action;

import gamesforblind.loader.ArrowKeyDirection;

public class LoaderArrowKeyAction extends LoaderAction {
    private final ArrowKeyDirection arrowKeyDirection;

    public LoaderArrowKeyAction(ArrowKeyDirection arrowKeyDirection) {
        this.arrowKeyDirection = arrowKeyDirection;
    }

    public ArrowKeyDirection getArrowKeyDirection() {
        return this.arrowKeyDirection;
    }
}
