package gamesforblind.loader.action;

/**
 * Action for when the user presses an unrecognized key while in the loader GUI.
 */
public class LoaderUnrecognizedKeyAction extends LoaderAction {
    /**
     * The key code of the unrecognized key that was selected.
     */
    private final int keyCode;

    /**
     * Creates a new {@link LoaderUnrecognizedKeyAction}
     * @param keyCode The key code of the unrecognized key that was selected.
     */
    public LoaderUnrecognizedKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Getter for the keyCode instance variable.
     * @return The key code of the unrecognized key that was selected.
     */
    public int getKeyCode() {
        return this.keyCode;
    }
}
