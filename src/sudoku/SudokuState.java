package sudoku;

import sudoku.generator.Cell;
import sudoku.generator.Generator;
import sudoku.generator.Grid;
import synthesizer.AudioPlayer;
import synthesizer.Phrase;

import java.awt.*;
import java.util.ArrayList;

public class SudokuState {
    private final AudioPlayer audioPlayer;
    private final int sudokuBoardSize;
    private final Grid sudokuGrid;
    private final ArrayList<Point> originallyFilledSquares;

    private Point selectedBlockPoint;
    private Point selectedSquarePoint;

    public SudokuState(int sudokuBoardSize, AudioPlayer audioPlayer) {
        int numberOfEmptyCells = (sudokuBoardSize * sudokuBoardSize) / 3;

        this.sudokuBoardSize = sudokuBoardSize;
        this.sudokuGrid = new Generator(sudokuBoardSize).generate(numberOfEmptyCells);

        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();
        this.audioPlayer = audioPlayer;
    }

    public SudokuState(SudokuState originalState) {
        this.sudokuBoardSize = originalState.sudokuBoardSize;
        this.sudokuGrid = new Grid(originalState.sudokuGrid);

        // Enums are immutable set (don't need copy constructor for them).
        this.selectedBlockPoint = originalState.selectedBlockPoint;
        this.selectedSquarePoint = originalState.selectedSquarePoint;

        ArrayList<Point> originallyFilledSquares = new ArrayList<>();
        for (Point point : originalState.originallyFilledSquares) {
            originallyFilledSquares.add(new Point(point));
        }
        this.originallyFilledSquares = originallyFilledSquares;
        this.audioPlayer = originalState.audioPlayer;
    }

    private ArrayList<Point> initializeOriginallyFilledSquares() {
        ArrayList<Point> originallyFilledSquares = new ArrayList<>();
        for (int rowIdx = 0; rowIdx < this.sudokuBoardSize; rowIdx++) {
            for (int columnIdx = 0; columnIdx < this.sudokuBoardSize; columnIdx++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(rowIdx, columnIdx);
                if (cellAtPosition.getValue() != 0) {
                    originallyFilledSquares.add(new Point(columnIdx, rowIdx));
                }
            }
        }
        return originallyFilledSquares;
    }

    public void setSquareNumber(int numberToFill) {
        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            Phrase relevantPhrase = Phrase.NO_SELECTED_SQUARE;
            this.audioPlayer.playPhrases(new Phrase[]{relevantPhrase});
            System.err.println(relevantPhrase.getPhraseValue());
            return;
        }

        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);
        Point pointToSet = new Point(
                this.selectedBlockPoint.x * numberOfBlocks + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocks + this.selectedSquarePoint.y
        );
        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);

        if (numberToFill == 0) {
            if (this.originallyFilledSquares.contains(pointToSet)) {
                Phrase relevantPhrase = Phrase.CANNOT_DELETE_ORIGINAL;
                this.audioPlayer.playPhrases(new Phrase[]{relevantPhrase});
                System.err.println(relevantPhrase.getPhraseValue());
            } else {
                cellToSet.setValue(0);
            }
            return;
        }

        if (!(numberToFill > 0 && numberToFill <= this.sudokuBoardSize)) {
            var phrase = this.sudokuBoardSize == 9 ? Phrase.INVALID_NUMBER_TO_FILL_9 : Phrase.INVALID_NUMBER_TO_FILL_4;
            this.audioPlayer.playPhrases(new Phrase[]{phrase});
            System.err.println(phrase.getPhraseValue());
            return;
        }

        if (!this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
            Phrase relevantPhrase = Phrase.CELL_VALUE_INVALID;
            this.audioPlayer.playPhrases(new Phrase[]{relevantPhrase});
            System.err.println(relevantPhrase.getPhraseValue());
            return;
        }

        cellToSet.setValue(numberToFill);
    }

    public void setHighlightedPoint(Point pointToSet, InputType inputType) {
        if (inputType == InputType.MOUSE) {
            int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);
            Point blockPointToSet = new Point(pointToSet.x / numberOfBlocks, pointToSet.y / numberOfBlocks);
            Point squarePointToSet = new Point(pointToSet.x % numberOfBlocks, pointToSet.y % numberOfBlocks);

            if (blockPointToSet.equals(this.selectedBlockPoint) && squarePointToSet.equals(this.selectedSquarePoint)) {
                this.selectedBlockPoint = null;
                this.selectedSquarePoint = null;
                return;
            }

            this.selectedBlockPoint = blockPointToSet;
            this.selectedSquarePoint = squarePointToSet;
            return;
        }

        if (pointToSet == null) {
            if (this.selectedSquarePoint != null) {
                this.selectedSquarePoint = null;
            } else {
                this.selectedBlockPoint = null;
            }
            return;
        }

        if (this.selectedBlockPoint != null && this.selectedSquarePoint != null) {
            Phrase relevantPhrase = Phrase.SELECTED_BOTH;
            this.audioPlayer.playPhrases(new Phrase[]{relevantPhrase});
            System.err.println(relevantPhrase.getPhraseValue());
            return;
        }

        if (this.selectedBlockPoint == null) {
            this.selectedBlockPoint = pointToSet;
            return;
        }

        this.selectedSquarePoint = pointToSet;
    }

    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }

    public Grid getSudokuGrid() {
        return this.sudokuGrid;
    }

    public Point getSelectedBlockPoint() {
        return this.selectedBlockPoint;
    }

    public Point getSelectedSquarePoint() {
        return this.selectedSquarePoint;
    }

    public ArrayList<Point> getOriginallyFilledSquares() {
        return this.originallyFilledSquares;
    }
}
