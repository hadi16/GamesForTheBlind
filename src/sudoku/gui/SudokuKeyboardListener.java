package sudoku.gui;

import sudoku.Direction;
import sudoku.SudokuGame;
import sudoku.action.SudokuHighlightAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

public class SudokuKeyboardListener implements KeyListener {
    private static final Map<Integer, Map<Character, Direction>> BOARD_SIZE_TO_CHAR_TO_DIRECTION = Map.of(
            4, Map.of(
                    'S', Direction.NORTHWEST,
                    'D', Direction.NORTHEAST,
                    'X', Direction.SOUTHWEST,
                    'C', Direction.SOUTHEAST
            ),
            9, Map.of(
                    'W', Direction.NORTHWEST,
                    'E', Direction.NORTH,
                    'R', Direction.NORTHEAST,
                    'S', Direction.WEST,
                    'D', Direction.CENTER,
                    'F', Direction.EAST,
                    'X', Direction.SOUTHWEST,
                    'C', Direction.SOUTH,
                    'V', Direction.SOUTHEAST
            )
    );

    private final SudokuGame sudokuGame;
    private final Map<Character, Direction> charToBoardDirection;

    public SudokuKeyboardListener(SudokuGame sudokuGame, int sudokuBoardSize) {
        if (!BOARD_SIZE_TO_CHAR_TO_DIRECTION.containsKey(sudokuBoardSize)) {
            throw new IllegalArgumentException(
                    "Improper Sudoku board size passed to keyboard listener: " + sudokuBoardSize
            );
        }

        this.sudokuGame = sudokuGame;
        this.charToBoardDirection = BOARD_SIZE_TO_CHAR_TO_DIRECTION.get(sudokuBoardSize);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char selectedKeyChar = Character.toUpperCase(e.getKeyChar());

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.sudokuGame.receiveAction(new SudokuHighlightAction(null));
            return;
        }

        Direction currentSelectedDirection = this.charToBoardDirection.get(selectedKeyChar);
        if (currentSelectedDirection == null) {
            System.err.println("An unrecognized key was pressed on the keyboard: " + selectedKeyChar);
            return;
        }

        this.sudokuGame.receiveAction(new SudokuHighlightAction(currentSelectedDirection));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
