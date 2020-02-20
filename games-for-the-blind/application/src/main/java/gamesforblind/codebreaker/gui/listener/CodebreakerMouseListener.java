package gamesforblind.codebreaker.gui.listener;

import gamesforblind.codebreaker.CodebreakerGame;
import gamesforblind.codebreaker.gui.CodebreakerFrame;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.enums.InputType;
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
     * A reference to the class containing the main JFrame (for the Codebreaker GUI).
     */
    private final CodebreakerFrame codebreakerFrame;

    private final CodebreakerType codebreakerType;

    /**
     * Creates a new CodebreakerMouseListener.
     *
     * @param codebreakerGame  The current {@link CodebreakerGame}
     * @param codebreakerFrame A reference to the class containing the main JFrame (for the Codebreaker GUI).
     * @param codebreakerType  The type of Codebreaker game.
     */
    public CodebreakerMouseListener(
            @NotNull CodebreakerGame codebreakerGame,
            @NotNull CodebreakerFrame codebreakerFrame,
            @NotNull CodebreakerType codebreakerType
    ) {
        this.codebreakerGame = codebreakerGame;
        this.codebreakerFrame = codebreakerFrame;
        this.codebreakerType = codebreakerType;
    }

    /**
     * Triggered when the mouse clicks on the main Codebreaker GUI.
     * Reads in mouse clicks & obtains the click coordinates, which allows the proper action to be called.
     *
     * @param e The event that was triggered by a mouse click.
     */
    @Override
    public void mouseClicked(@NotNull MouseEvent e) {
        Point selectedPoint = e.getPoint();
        Rectangle frameBounds = this.codebreakerFrame.getFrameBounds();

        int squareDimension = Math.min(frameBounds.height, frameBounds.width) / (this.sudokuBoardSize + 1);
        this.codebreakerGame.receiveAction(
                new SudokuHighlightAction(
                        new Point(selectedPoint.x / squareDimension, selectedPoint.y / squareDimension),
                        InputType.MOUSE
                )
        );
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
