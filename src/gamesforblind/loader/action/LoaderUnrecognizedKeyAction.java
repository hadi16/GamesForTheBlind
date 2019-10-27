package gamesforblind.loader.action;

public class LoaderUnrecognizedKeyAction extends LoaderAction {
    private final int keyCode;

    public LoaderUnrecognizedKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
