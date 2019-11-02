package gamesforblind.sudoku;

import gamesforblind.enums.InputType;
import gamesforblind.enums.InterfaceType;
import gamesforblind.enums.SudokuSection;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Generator;
import gamesforblind.sudoku.generator.Grid;
import gamesforblind.sudoku.interfaces.SudokuBlockSelectionInterface;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import gamesforblind.synthesizer.Phrase;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class SudokuState {
    private final OriginalSudokuGrid originalGrid;
    private final SudokuType sudokuType;
    private final SudokuKeyboardInterface sudokuKeyboardInterface;

    private final AudioPlayerExecutor audioPlayerExecutor;
    private final Grid sudokuGrid;
    private final ArrayList<Point> originallyFilledSquares;

    private int numberOfEmptyCells;
    private boolean gameOver = false;

    public SudokuState(InterfaceType interfaceType, SudokuType sudokuType,
                       AudioPlayerExecutor audioPlayerExecutor, OriginalSudokuGrid originalGrid) {
        this.sudokuType = sudokuType;
        this.audioPlayerExecutor = audioPlayerExecutor;
        this.originalGrid = originalGrid;

        this.numberOfEmptyCells = this.getInitialNumberOfEmptyCells(sudokuType.getSudokuBoardSize());
        this.sudokuGrid = Grid.of(originalGrid.getGrid(), sudokuType);
        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();

        this.sudokuKeyboardInterface = this.initializeKeyboardInterface(interfaceType);
    }

    public SudokuState(InterfaceType interfaceType, SudokuType sudokuType, AudioPlayerExecutor audioPlayerExecutor) {
        this.sudokuType = sudokuType;
        this.audioPlayerExecutor = audioPlayerExecutor;

        this.numberOfEmptyCells = this.getInitialNumberOfEmptyCells(sudokuType.getSudokuBoardSize());

        this.sudokuGrid = new Generator(sudokuType).generate(this.numberOfEmptyCells);
        this.originalGrid = OriginalSudokuGrid.of(this.sudokuGrid.toIntArray());

        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();

        this.sudokuKeyboardInterface = this.initializeKeyboardInterface(interfaceType);
    }

    private SudokuKeyboardInterface initializeKeyboardInterface(InterfaceType interfaceType) {
        switch (interfaceType) {
            case ARROW_KEY_INTERFACE:
                throw new IllegalArgumentException("Arrow key interface not implemented yet!");
            case BLOCK_SELECTION_INTERFACE:
                return new SudokuBlockSelectionInterface(this.sudokuType, this.sudokuGrid);
            default:
                throw new IllegalArgumentException("Incorrect interface type passed to Sudoku state: " + interfaceType);
        }
    }

    private int getInitialNumberOfEmptyCells(int sudokuBoardSize) {
        return (sudokuBoardSize * sudokuBoardSize) / 3;
    }

    private ArrayList<Point> initializeOriginallyFilledSquares() {
        ArrayList<Point> originallyFilledSquares = new ArrayList<>();

        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        for (int rowIdx = 0; rowIdx < sudokuBoardSize; rowIdx++) {
            for (int columnIdx = 0; columnIdx < sudokuBoardSize; columnIdx++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(rowIdx, columnIdx);
                if (cellAtPosition.getValue() != 0) {
                    originallyFilledSquares.add(new Point(columnIdx, rowIdx));
                }
            }
        }

        return originallyFilledSquares;
    }

    private ArrayList<Phrase> getRemainingNumberOfEmptySquaresPhraseList() {
        ArrayList<Phrase> phrasesToRead;
        if (this.numberOfEmptyCells == 0) {
            phrasesToRead = new ArrayList<>(Collections.singletonList(Phrase.CONGRATS));
        } else if (this.numberOfEmptyCells == 1) {
            phrasesToRead = new ArrayList<>(Arrays.asList(
                    Phrase.EMPTY_PIECES_OF_BOARD_SINGULAR_1, Phrase.ONE, Phrase.EMPTY_PIECES_OF_BOARD_SINGULAR_2
            ));
        } else {
            phrasesToRead = new ArrayList<>(Arrays.asList(
                    Phrase.EMPTY_PIECES_OF_BOARD_PLURAL_1,
                    Phrase.convertIntegerToPhrase(this.numberOfEmptyCells),
                    Phrase.EMPTY_PIECES_OF_BOARD_PLURAL_2
            ));
        }

        return phrasesToRead;
    }

    private boolean readCannotDeleteOriginal(Point point) {
        if (!this.originallyFilledSquares.contains(point)) {
            return false;
        }

        Cell cellToSet = this.sudokuGrid.getCell(point.y, point.x);

        // Square will always contain a number from 1-9 or 1-4 (never 0), since it is an originally set square.
        this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                Phrase.CANNOT_DELETE_ORIGINAL,
                Phrase.CURRENT_VALUE,
                Phrase.convertIntegerToPhrase(cellToSet.getValue())
        )));

        return true;
    }

    public void setSquareNumber(int numberToFill) {
        Optional<Point> maybePointToSet = this.sudokuKeyboardInterface.getSelectedPoint();
        if (maybePointToSet.isEmpty()) {
            this.readNoSelectedSquareMessage();
            return;
        }

        Point pointToSet = maybePointToSet.get();
        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);

        if (numberToFill == 0) {
            if (this.readCannotDeleteOriginal(pointToSet)) {
                return;
            }

            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                    Phrase.REMOVED_NUM, Phrase.convertIntegerToPhrase(cellToSet.getValue()))
            ));
            cellToSet.setValue(0);
            return;
        }

        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        if (!(numberToFill > 0 && numberToFill <= sudokuBoardSize)) {
            this.audioPlayerExecutor.replacePhraseAndPrint(
                    sudokuBoardSize == 9 ? Phrase.INVALID_NUMBER_TO_FILL_9 : Phrase.INVALID_NUMBER_TO_FILL_4
            );
            return;
        }

        /* Working on this - Callum */
        /*Solver solver = new Solver(this.sudokuBoardSize);

        cellToSet.setValue(numberToFill);//update cell for checking
        Grid gridBackup = this.getSudokuGrid();//create test grid

        if (!solver.superSolver(gridBackup)) {
            if (!this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
                this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CELL_VALUE_INVALID);

                cellToSet.setValue(0); // Reset the value to empty in real grid, it is set elsewhere if correct
                return;
            }
        }*/

        if (!this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
            this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CELL_VALUE_INVALID);
            return;
        }

        if (cellToSet.getValue() == 0) {
            this.numberOfEmptyCells--;
        }
        cellToSet.setValue(numberToFill);

        if (this.numberOfEmptyCells == 0) {
            this.gameOver = true;
        }

        ArrayList<Phrase> phrasesToRead = new ArrayList<>(
                Arrays.asList(Phrase.PLACED_NUM, Phrase.convertIntegerToPhrase(numberToFill))
        );
        phrasesToRead.addAll(this.getRemainingNumberOfEmptySquaresPhraseList());
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    public void readInstructions() {
        this.audioPlayerExecutor.replacePhraseAndPrint(
                this.sudokuType.getSudokuBoardSize() == 9 ? Phrase.INSTRUCTIONS_9 : Phrase.INSTRUCTIONS_4
        );
    }

    private void readNoSelectedSquareMessage() {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(Collections.singletonList(Phrase.NO_SELECTED_SQUARE));
        phrasesToRead.addAll(this.getRemainingNumberOfEmptySquaresPhraseList());
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    public void giveHint() {
        Optional<Point> maybePointToSet = this.sudokuKeyboardInterface.getSelectedPoint();

        if (maybePointToSet.isEmpty()) {
            this.readNoSelectedSquareMessage();
            return;
        }

        Point pointToSet = maybePointToSet.get();
        if (this.readCannotDeleteOriginal(pointToSet)) {
            return;
        }

        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);

        for (int cellValue = 1; cellValue <= this.sudokuType.getSudokuBoardSize(); cellValue++) {
            if (this.sudokuGrid.isValidValueForCell(cellToSet, cellValue)) {
                cellToSet.setValue(cellValue);
                this.numberOfEmptyCells--;
                break;
            }
        }

        ArrayList<Phrase> phrasesToRead = new ArrayList<>(
                Collections.singletonList(Phrase.convertIntegerToPhrase(cellToSet.getValue()))
        );
        phrasesToRead.addAll(this.getRemainingNumberOfEmptySquaresPhraseList());
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);

        if (this.numberOfEmptyCells == 0) {
            this.gameOver = true;
        }
    }

    public void readUnrecognizedKey(int keyCode) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(Arrays.asList(
                Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(keyCode)
        ));
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    private ArrayList<Phrase> getRowOrColumnPhrases(Point selectedPoint, boolean readRow) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>();
        phrasesToRead.add(readRow ? Phrase.IN_ROW : Phrase.IN_COLUMN);
        for (int rowOrColumnIdx = 0; rowOrColumnIdx < this.sudokuType.getSudokuBoardSize(); rowOrColumnIdx++) {
            Cell cellToRead;
            if (readRow) {
                cellToRead = this.sudokuGrid.getCell(selectedPoint.y, rowOrColumnIdx);
            } else {
                cellToRead = this.sudokuGrid.getCell(rowOrColumnIdx, selectedPoint.x);
            }

            phrasesToRead.add(Phrase.convertIntegerToPhrase(cellToRead.getValue()));
        }
        return phrasesToRead;
    }

    private ArrayList<Phrase> getBlockPhrases(Point selectedPoint) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>();
        phrasesToRead.add(Phrase.IN_BLOCK);

        int blockHeight = this.sudokuType.getBlockHeight();
        int currentRowIndex = (selectedPoint.y / blockHeight) * blockHeight;
        int maxRowIndex = currentRowIndex + blockHeight;
        for (; currentRowIndex < maxRowIndex; currentRowIndex++) {
            int blockWidth = this.sudokuType.getBlockWidth();
            int currentColumnIndex = (selectedPoint.x / blockWidth) * blockWidth;
            int maxColumnIndex = currentColumnIndex + blockWidth;

            for (; currentColumnIndex < maxColumnIndex; currentColumnIndex++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(currentRowIndex, currentColumnIndex);
                phrasesToRead.add(Phrase.convertIntegerToPhrase(cellAtPosition.getValue()));
            }
        }

        return phrasesToRead;
    }

    public void readSelectedSquare() {
        Optional<Cell> maybeSelectedCell = this.sudokuKeyboardInterface.getSelectedCell();
        if (maybeSelectedCell.isPresent()) {
            Cell selectedCell = maybeSelectedCell.get();
            this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.convertIntegerToPhrase(selectedCell.getValue()));
        }
    }

    public void readBoardSection(SudokuSection sectionToRead) {
        Optional<Point> maybeSelectedPoint = this.sudokuKeyboardInterface.getSelectedPoint();

        if (maybeSelectedPoint.isEmpty()) {
            this.readNoSelectedSquareMessage();
            return;
        }

        Point selectedPoint = maybeSelectedPoint.get();
        ArrayList<Phrase> phrasesToRead = new ArrayList<>();
        switch (sectionToRead) {
            case ROW:
                phrasesToRead = this.getRowOrColumnPhrases(selectedPoint, true);
                break;
            case COLUMN:
                phrasesToRead = this.getRowOrColumnPhrases(selectedPoint, false);
                break;
            case BLOCK:
                phrasesToRead = this.getBlockPhrases(selectedPoint);
                break;
        }

        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    public void setHighlightedPoint(Point pointToSet, InputType inputType) {
        this.sudokuKeyboardInterface.setHighlightedPoint(pointToSet, inputType);
    }

    public int getSudokuBoardSize() {
        return this.sudokuType.getSudokuBoardSize();
    }

    public Grid getSudokuGrid() {
        return this.sudokuGrid;
    }

    public ArrayList<Point> getOriginallyFilledSquares() {
        return this.originallyFilledSquares;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public OriginalSudokuGrid getOriginalGrid() {
        return this.originalGrid;
    }

    public SudokuKeyboardInterface getSudokuKeyboardInterface() {
        return this.sudokuKeyboardInterface;
    }
}
