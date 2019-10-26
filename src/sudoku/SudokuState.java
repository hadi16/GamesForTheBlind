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
import java.util.Optional;

public class SudokuState {
    private final AudioPlayer audioPlayer;
    private final int sudokuBoardSize;
    private final Grid sudokuGrid;
    private final ArrayList<Point> originallyFilledSquares;

    private Point selectedBlockPoint;
    private Point selectedSquarePoint;
    private int numberOfEmptyCells;

    private boolean gameOver = false;

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
        this.gameOver = originalState.gameOver;
    }

    private void replacePhraseAndPrint(Phrase relevantPhrase) {
        System.out.println(relevantPhrase.getPhraseValue());
        this.audioPlayer.replacePhraseToPlay(relevantPhrase);
    }

    private void replacePhraseAndPrint(ArrayList<Phrase> relevantPhraseList) {
        ArrayList<String> phraseStringList = new ArrayList<>();
        relevantPhraseList.forEach(phrase -> phraseStringList.add(phrase.getPhraseValue()));
        System.out.println(String.join(" ", phraseStringList));

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

    private Optional<Cell> getCurrentlySelectedCell() {
        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            return Optional.empty();
        }

        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);
        Point newPoint = new Point(
                this.selectedBlockPoint.x * numberOfBlocks + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocks + this.selectedSquarePoint.y
        );

        return Optional.of(this.sudokuGrid.getCell(newPoint.y, newPoint.x));
    }

    private ArrayList<Phrase> getRemainingNumberOfEmptySquaresPhraseList() {
        ArrayList<Phrase> phrasesToRead;
        if (this.numberOfEmptyCells == 0) {
            phrasesToRead = new ArrayList<>(Collections.singletonList(Phrase.CONGRATS));
        } else if (this.numberOfEmptyCells == 1) {
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

        return phrasesToRead;
    }

    public void setSquareNumber(int numberToFill) {
        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            ArrayList<Phrase> phrasesToRead = new ArrayList<>(Collections.singletonList(Phrase.NO_SELECTED_SQUARE));
            phrasesToRead.addAll(this.getRemainingNumberOfEmptySquaresPhraseList());
            this.replacePhraseAndPrint(phrasesToRead);
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
                // This square will always contain a number from 1-9 or 1-4 (never 0).
                this.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                        Phrase.CANNOT_DELETE_ORIGINAL,
                        Phrase.CURRENT_VALUE,
                        Phrase.convertIntegerToPhrase(cellToSet.getValue())
                )));
            } else {
                this.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                        Phrase.REMOVED_NUM, Phrase.convertIntegerToPhrase(cellToSet.getValue()))
                ));
                cellToSet.setValue(0);
            }
            return;
        }

        if (!(numberToFill > 0 && numberToFill <= this.sudokuBoardSize)) {
            this.replacePhraseAndPrint(
                    this.sudokuBoardSize == 9 ? Phrase.INVALID_NUMBER_TO_FILL_9 : Phrase.INVALID_NUMBER_TO_FILL_4
            );
            return;
        }

        if (!this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
            this.replacePhraseAndPrint(Phrase.CELL_VALUE_INVALID);
            return;
        }

        cellToSet.setValue(numberToFill);

        this.numberOfEmptyCells--;
        if (this.numberOfEmptyCells == 0) {
            this.gameOver = true;
        }

        ArrayList<Phrase> phrasesToRead = new ArrayList<>(
                Arrays.asList(Phrase.PLACED_NUM, Phrase.convertIntegerToPhrase(numberToFill))
        );
        phrasesToRead.addAll(this.getRemainingNumberOfEmptySquaresPhraseList());
        this.replacePhraseAndPrint(phrasesToRead);
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

        Optional<Cell> selectedCell = this.getCurrentlySelectedCell();
        if (selectedCell.isPresent()) {
            this.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                    Phrase.CURRENT_VALUE, Phrase.convertIntegerToPhrase(selectedCell.get().getValue())
            )));
            return;
        }

        if (this.selectedBlockPoint == null) {
            this.selectedBlockPoint = pointToSet;
            return;
        }

        this.selectedSquarePoint = pointToSet;
    }

    public void readInstructions() {
        this.replacePhraseAndPrint(
                this.sudokuBoardSize == 9 ? Phrase.INSTRUCTIONS_9 : Phrase.INSTRUCTIONS_4
        );
    }

    public void readUnrecognizedKey(int keyCode) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(Arrays.asList(
                Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(keyCode)
        ));
        this.replacePhraseAndPrint(phrasesToRead);
    }

    public void readRowOrColumn(boolean readRow) {
        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);

        Point selectedPoint = new Point(
                this.selectedBlockPoint.x * numberOfBlocks + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocks + this.selectedSquarePoint.y
        );

        ArrayList<Phrase> phrasesToRead = new ArrayList<>();
        phrasesToRead.add(readRow ? Phrase.IN_ROW : Phrase.IN_COLUMN);
        for (int rowOrColumnIdx = 0; rowOrColumnIdx < this.sudokuBoardSize; rowOrColumnIdx++) {
            Cell cellToRead;
            if (readRow) {
                cellToRead = this.sudokuGrid.getCell(selectedPoint.y, rowOrColumnIdx);
            } else {
                cellToRead = this.sudokuGrid.getCell(rowOrColumnIdx, selectedPoint.x);
            }

            phrasesToRead.add(Phrase.convertIntegerToPhrase(cellToRead.getValue()));
        }
        this.replacePhraseAndPrint(phrasesToRead);
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

    public boolean isGameOver() {
        return this.gameOver;
    }
}
