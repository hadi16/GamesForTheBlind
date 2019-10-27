package gamesforblind.loader.action;

import gamesforblind.loader.enums.ArrowKeyDirection;

/**
 * Action for when the user presses an arrow key (up/down/left/right) while in the loader screen.
 */
public class LoaderArrowKeyAction extends LoaderAction {
    /**
     * Signifies which arrow key was pressed in the loader (up/down/left/right).
     */
    private final ArrowKeyDirection arrowKeyDirection;

    /**
     * Creates a new {@link LoaderArrowKeyAction}
     *
     * @param arrowKeyDirection Which arrow key was pressed in the loader.
     */
    public LoaderArrowKeyAction(ArrowKeyDirection arrowKeyDirection) {
        this.arrowKeyDirection = arrowKeyDirection;
    }

    /**
     * Getter for the arrowKeyDirection instance variable.
     *
     * @return Which arrow key was pressed in the loader (up/down/left/right).
     */
    public ArrowKeyDirection getArrowKeyDirection() {
        return this.arrowKeyDirection;
    }
}
