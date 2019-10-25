package sudoku;

import sudoku.generator.Cell;
import sudoku.generator.Generator;
import sudoku.generator.Grid;
import synthesizer.AudioPlayer;
import synthesizer.Phrase;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SudokuState {
    private final AudioPlayer audioPlayer;
    private final int sudokuBoardSize;
    private final Grid sudokuGrid;
    private final ArrayList<Point> originallyFilledSquares;

    private Point selectedBlockPoint;
    private Point selectedSquarePoint;
    private int numberOfEmptyCells;

    public SudokuState(int sudokuBoardSize, AudioPlayer audioPlayer) {
        this.numberOfEmptyCells = (sudokuBoardSize * sudokuBoardSize) / 3;

        this.sudokuBoardSize = sudokuBoardSize;
        this.sudokuGrid = new Generator(sudokuBoardSize).generate(this.numberOfEmptyCells);

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
        this.numberOfEmptyCells = originalState.numberOfEmptyCells;
    }

    private void replacePhraseAndPrintToError(Phrase relevantPhrase) {
        System.err.println(relevantPhrase.getPhraseValue());
        this.audioPlayer.replacePhraseToPlay(relevantPhrase);
    }

    private void replacePhraseAndPrintToError(ArrayList<Phrase> relevantPhraseList) {
        relevantPhraseList.forEach(x -> System.err.print(x.getPhraseValue()));
        System.err.println();

        this.audioPlayer.replacePhraseToPlay(relevantPhraseList);
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

    private void readRemainingNumberOfCells() {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>();
        if (this.numberOfEmptyCells >= 1 && this.numberOfEmptyCells <= 9) {
            if (this.numberOfEmptyCells == 1) {
                phrasesToRead = new ArrayList<>(Arrays.asList(
                        Phrase.EMPTY_PIECES_OF_BOARD_SINGULAR_1, Phrase.ONE, Phrase.EMPTY_PIECES_OF_BOARD_SINGULAR_2)
                );
            } else {
                phrasesToRead = new ArrayList<>(Arrays.asList(
                        Phrase.EMPTY_PIECES_OF_BOARD_PLURAL_1,
                        Phrase.convertIntegerToPhrase(this.numberOfEmptyCells),
                        Phrase.EMPTY_PIECES_OF_BOARD_PLURAL_2
                ));
            }
        } else if (this.numberOfEmptyCells == 0) {
            phrasesToRead = new ArrayList<>(Collections.singletonList(Phrase.CONGRATS));
        }

        this.replacePhraseAndPrintToError(phrasesToRead);
    }

    public void setSquareNumber(int numberToFill) {
        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            this.replacePhraseAndPrintToError(Phrase.NO_SELECTED_SQUARE);
            this.readRemainingNumberOfCells();
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
                this.replacePhraseAndPrintToError(new ArrayList<>(Arrays.asList(
                        Phrase.CANNOT_DELETE_ORIGINAL,
                        Phrase.CURRENT_VALUE,
                        Phrase.convertIntegerToPhrase(cellToSet.getValue())
                )));
            } else {
                cellToSet.setValue(0);
                this.replacePhraseAndPrintToError(new ArrayList<>(Arrays.asList(
                        Phrase.PLACED_NUM, Phrase.convertIntegerToPhrase(cellToSet.getValue()))
                ));
            }
            return;
        }

        if (!(numberToFill > 0 && numberToFill <= this.sudokuBoardSize)) {
            this.replacePhraseAndPrintToError(
                    this.sudokuBoardSize == 9 ? Phrase.INVALID_NUMBER_TO_FILL_9 : Phrase.INVALID_NUMBER_TO_FILL_4
            );
            return;
        }

        if (!this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
            this.replacePhraseAndPrintToError(Phrase.CELL_VALUE_INVALID);
            return;
        }

        cellToSet.setValue(numberToFill);
        this.numberOfEmptyCells--;
        this.readRemainingNumberOfCells();
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
            this.replacePhraseAndPrintToError(Phrase.SELECTED_BOTH);
            return;
        }

        if (this.selectedBlockPoint == null) {
            this.selectedBlockPoint = pointToSet;
            return;
        }

        this.selectedSquarePoint = pointToSet;
    }

    /*
    - changing "You have already selected both a block & square on the board." to "current value in this box is"
    - call instructions
     */
    public void readTheSection() {
    }

    public void readTheRow() {
    }

    public void readTheColumn() {
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
