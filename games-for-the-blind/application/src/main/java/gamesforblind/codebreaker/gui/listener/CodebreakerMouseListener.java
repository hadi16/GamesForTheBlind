package gamesforblind.codebreaker.gui.listener;

import gamesforblind.codebreaker.CodebreakerGame;
import gamesforblind.codebreaker.action.CodebreakerMouseAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Mouse listener for Codebreaker that receives mouse inputs and appropriately calls the proper action class
 */
public class CodebreakerMouseListener implements MouseListener {
    /**
     * The current {@link CodebreakerGame}
     */
    private final CodebreakerGame codebreakerGame;

    /**
     * Creates a new CodebreakerMouseListener.
     *
     * @param codebreakerGame The current {@link CodebreakerGame}
     */
    public CodebreakerMouseListener(@NotNull CodebreakerGame codebreakerGame) {
        this.codebreakerGame = codebreakerGame;
    }

    /**
     * Triggered when the mouse clicks on the main Codebreaker GUI.
     * Reads in mouse clicks & obtains the click coordinates, which allows the proper action to be called.
     *
     * @param e The event that was triggered by a mouse click.
     */
    @Override
    public void mouseClicked(@NotNull MouseEvent e) {
        this.codebreakerGame.receiveAction(new CodebreakerMouseAction(new Point(e.getPoint())));
    }

    /* These methods are required to be overridden by the MouseListener, but they are unused. */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
