package sudoku.gui.listener;

import sudoku.InputType;
import sudoku.SudokuGame;
import sudoku.action.SudokuFillAction;
import sudoku.action.SudokuHighlightAction;
import sudoku.action.SudokuReadPositionAction;
import synthesizer.AudioPlayer;
import synthesizer.Phrase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

public class SudokuKeyboardListener implements KeyListener {
    private static final Map<Integer, Map<Character, Point>> BOARD_SIZE_TO_CHAR_TO_POINT = Map.of(
            4, Map.of(
                    'S', new Point(0, 0),
                    'D', new Point(1, 0),
                    'X', new Point(0, 1),
                    'C', new Point(1, 1)
            ),
            9, Map.of(
                    'W', new Point(0, 0),
                    'E', new Point(1, 0),
                    'R', new Point(2, 0),
                    'S', new Point(0, 1),
                    'D', new Point(1, 1),
                    'F', new Point(2, 1),
                    'X', new Point(0, 2),
                    'C', new Point(1, 2),
                    'V', new Point(2, 2)
            )
    );

    private final AudioPlayer audioPlayer;
    private final SudokuGame sudokuGame;
    private final Map<Character, Point> charToPoint;
    private int sudokuBoardSize;

    public SudokuKeyboardListener(SudokuGame sudokuGame, int sudokuBoardSize, AudioPlayer audioPlayer) {
        if (!BOARD_SIZE_TO_CHAR_TO_POINT.containsKey(sudokuBoardSize)) {
            throw new IllegalArgumentException(
                    "Improper Sudoku board size passed to keyboard listener: " + sudokuBoardSize
            );
        }

        this.sudokuGame = sudokuGame;
        this.charToPoint = BOARD_SIZE_TO_CHAR_TO_POINT.get(sudokuBoardSize);
        this.audioPlayer = audioPlayer;
        this.sudokuBoardSize = sudokuBoardSize;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Character.isDigit(e.getKeyChar())) {
            this.sudokuGame.receiveAction(
                    new SudokuFillAction(Character.getNumericValue(e.getKeyChar()))
            );
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.sudokuGame.receiveAction(
                    new SudokuHighlightAction(null, InputType.KEYBOARD)
            );
            return;
        }

        char selectedKeyChar = Character.toUpperCase(e.getKeyChar());
        Point currentSelectedPoint = this.charToPoint.get(selectedKeyChar);

        if (currentSelectedPoint == null ) {
            /*
                reads the entire row that the player is in
             */
            if (selectedKeyChar == 'J') {
                String type = "row";
                this.sudokuGame.receiveAction(
                        new SudokuReadPositionAction(type)
                );
                return;

            }
            else if(selectedKeyChar == 'K'){
                String type ="column";
                this.sudokuGame.receiveAction(
                        new SudokuReadPositionAction(type)
                );
                return;
            }
            else if (selectedKeyChar == 'I') {
                if (sudokuBoardSize == 4) {
                    Phrase relevantPhrase = Phrase.INSTRUCTIONS4;
                    this.audioPlayer.replacePhraseToPlay(relevantPhrase);
                    System.err.println(relevantPhrase.getPhraseValue());
                    return;

                }
                if (sudokuBoardSize == 9) {
                    Phrase relevantPhrase = Phrase.INSTRUCTIONS9;
                    this.audioPlayer.replacePhraseToPlay(relevantPhrase);
                    System.err.println(relevantPhrase.getPhraseValue());
                    return;

                }
            }
            else {
                Phrase relevantPhrase = Phrase.UNRECOGNIZED_KEY;

                this.audioPlayer.replacePhraseToPlay(relevantPhrase);
                System.err.println(relevantPhrase.getPhraseValue() + " (" + selectedKeyChar + ")");
                return;

            }

        }

        this.sudokuGame.receiveAction(
                new SudokuHighlightAction(currentSelectedPoint, InputType.KEYBOARD)
        );
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
