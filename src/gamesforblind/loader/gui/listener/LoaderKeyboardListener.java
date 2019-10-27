package gamesforblind.loader.gui.listener;

import gamesforblind.loader.ArrowKeyDirection;
import gamesforblind.loader.GameLoader;
import gamesforblind.loader.action.LoaderArrowKeyAction;
import gamesforblind.loader.action.LoaderUnrecognizedKeyAction;
import gamesforblind.loader.gui.LoaderFrame;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LoaderKeyboardListener extends LoaderListener implements KeyEventDispatcher {
    private final LoaderFrame loaderFrame;

    public LoaderKeyboardListener(GameLoader gameLoader, LoaderFrame loaderFrame) {
        super(gameLoader);
        this.loaderFrame = loaderFrame;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            this.keyPressed(e);
        }

        return false;
    }

    private void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.sendActionBasedOnButtonText(this.loaderFrame.getCurrentlyHighlightedButtonText());
            return;
        }

        ArrowKeyDirection arrowKeyDirection = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                arrowKeyDirection = ArrowKeyDirection.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                arrowKeyDirection = ArrowKeyDirection.RIGHT;
                break;
            case KeyEvent.VK_UP:
                arrowKeyDirection = ArrowKeyDirection.UP;
                break;
            case KeyEvent.VK_DOWN:
                arrowKeyDirection = ArrowKeyDirection.DOWN;
                break;
        }

        if (arrowKeyDirection == null) {
            this.gameLoader.receiveAction(new LoaderUnrecognizedKeyAction(e.getKeyCode()));
            return;
        }

        this.gameLoader.receiveAction(new LoaderArrowKeyAction(arrowKeyDirection));
    }
}
