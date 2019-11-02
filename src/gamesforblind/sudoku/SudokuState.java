package gamesforblind.sudoku;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuSection;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Generator;
import gamesforblind.sudoku.generator.Grid;
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

    private final AudioPlayerExecutor audioPlayerExecutor;
    private final Grid sudokuGrid;
    private final ArrayList<Point> originallyFilledSquares;

    private Point selectedBlockPoint;
    private Point selectedSquarePoint;

    private int numberOfEmptyCells;

    private boolean gameOver = false;

    public SudokuState(SudokuType sudokuType, AudioPlayerExecutor audioPlayerExecutor, OriginalSudokuGrid originalGrid) {
        this.sudokuType = sudokuType;
        this.audioPlayerExecutor = audioPlayerExecutor;
        this.originalGrid = originalGrid;

        this.numberOfEmptyCells = this.getInitialNumberOfEmptyCells(sudokuType.getSudokuBoardSize());
        this.sudokuGrid = Grid.of(originalGrid.getGrid(), sudokuType);
        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();
    }

    public SudokuState(SudokuType sudokuType, AudioPlayerExecutor audioPlayerExecutor) {
        this.sudokuType = sudokuType;
        this.audioPlayerExecutor = audioPlayerExecutor;

        this.numberOfEmptyCells = this.getInitialNumberOfEmptyCells(sudokuType.getSudokuBoardSize());

        this.sudokuGrid = new Generator(sudokuType).generate(this.numberOfEmptyCells);
        this.originalGrid = OriginalSudokuGrid.of(this.sudokuGrid.toIntArray());

        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();
    }

    public SudokuState(SudokuState originalState) {
        this.sudokuGrid = new Grid(originalState.sudokuGrid);

        // Enums are immutable set (don't need copy constructor for them).
        this.sudokuType = originalState.sudokuType;
        this.selectedBlockPoint = originalState.selectedBlockPoint;
        this.selectedSquarePoint = originalState.selectedSquarePoint;

        ArrayList<Point> originallyFilledSquares = new ArrayList<>();
        for (Point point : originalState.originallyFilledSquares) {
            originallyFilledSquares.add(new Point(point));
        }
        this.originallyFilledSquares = originallyFilledSquares;
        this.audioPlayerExecutor = originalState.audioPlayerExecutor;
        this.numberOfEmptyCells = originalState.numberOfEmptyCells;
        this.gameOver = originalState.gameOver;

        this.originalGrid = new OriginalSudokuGrid(originalState.originalGrid);
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

    private Optional<Cell> getSelectedCellAtPosition(Point selectedSquarePoint, Point selectedBlockPoint) {
        if (selectedSquarePoint == null || selectedBlockPoint == null) {
            return Optional.empty();
        }

        int numberOfBlocksWidth = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockWidth();
        int numberOfBlocksHeight = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockHeight();
        Point cellPoint = new Point(
                selectedBlockPoint.x * numberOfBlocksWidth + selectedSquarePoint.x,
                selectedBlockPoint.y * numberOfBlocksHeight + selectedSquarePoint.y
        );

        return Optional.of(this.sudokuGrid.getCell(cellPoint.y, cellPoint.x));
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

    private boolean readCannotDeleteOriginal() {
        Point selectedPoint = this.getSelectedPoint();

        if (!this.originallyFilledSquares.contains(selectedPoint)) {
            return false;
        }

        Cell cellToSet = this.sudokuGrid.getCell(selectedPoint.y, selectedPoint.x);

        // Square will always contain a number from 1-9 or 1-4 (never 0), since it is an originally set square.
        this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                Phrase.CANNOT_DELETE_ORIGINAL,
                Phrase.CURRENT_VALUE,
                Phrase.convertIntegerToPhrase(cellToSet.getValue())
        )));

        return true;
    }

    public void setSquareNumber(int numberToFill) {
        if (this.readNoSelectedSquareMessage()) {
            return;
        }

        Point pointToSet = this.getSelectedPoint();
        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);

        if (numberToFill == 0) {
            if (this.readCannotDeleteOriginal()) {
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

    public void setHighlightedPoint(Point pointToSet, InputType inputType) {
        if (inputType == InputType.MOUSE) {
            // Offset needed for the column labels ("A", "B", etc). For some reason,
            // the same offset is not needed for the row labels (not sure why).
            pointToSet = new Point(pointToSet.x, pointToSet.y - 1);

            int numberOfBlocksWidth = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockWidth();
            int numberOfBlocksHeight = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockHeight();

            Point blockPointToSet = new Point(
                    pointToSet.x / numberOfBlocksWidth, pointToSet.y / numberOfBlocksHeight
            );
            Point squarePointToSet = new Point(
                    pointToSet.x % numberOfBlocksWidth, pointToSet.y % numberOfBlocksHeight
            );

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

        Optional<Cell> selectedCell = this.getSelectedCellAtPosition(this.selectedSquarePoint, this.selectedBlockPoint);
        if (selectedCell.isPresent()) {
            this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.convertIntegerToPhrase(selectedCell.get().getValue()));
            return;
        }

        if (this.selectedBlockPoint == null) {
            this.selectedBlockPoint = pointToSet;
            return;
        }

        this.selectedSquarePoint = pointToSet;
    }

    public void readInstructions() {
        this.audioPlayerExecutor.replacePhraseAndPrint(
                this.sudokuType.getSudokuBoardSize() == 9 ? Phrase.INSTRUCTIONS_9 : Phrase.INSTRUCTIONS_4
        );
    }

    private boolean readNoSelectedSquareMessage() {
        if (this.selectedSquarePoint == null || this.selectedBlockPoint == null) {
            ArrayList<Phrase> phrasesToRead = new ArrayList<>(Collections.singletonList(Phrase.NO_SELECTED_SQUARE));
            phrasesToRead.addAll(this.getRemainingNumberOfEmptySquaresPhraseList());
            this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
            return true;
        }

        return false;
    }

    public void giveHint() {
        if (this.readNoSelectedSquareMessage()) {
            return;
        }

        if (this.readCannotDeleteOriginal()) {
            return;
        }

        Point pointToSet = this.getSelectedPoint();
        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);
        
        for (int cellValue = 1; cellValue <= this.sudokuType.getSudokuBoardSize(); cellValue++) {
            if (this.sudokuGrid.isValidValueForCell(cellToSet, cellValue)) {
                cellToSet.setValue(cellValue);
                break;
            }
        }

        this.numberOfEmptyCells--;

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

    private ArrayList<Phrase> getBlockPhrases() {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>();
        phrasesToRead.add(Phrase.IN_BLOCK);

        for (int rowIndex = 0; rowIndex < this.sudokuType.getBlockHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < this.sudokuType.getBlockWidth(); columnIndex++) {
                Optional<Cell> cell = this.getSelectedCellAtPosition(
                        new Point(columnIndex, rowIndex), this.selectedBlockPoint
                );

                cell.ifPresent(
                        cellValue -> phrasesToRead.add(Phrase.convertIntegerToPhrase(cellValue.getValue()))
                );
            }
        }

        return phrasesToRead;
    }

    public void readBoardSection(SudokuSection sectionToRead) {
        if (this.readNoSelectedSquareMessage()) {
            return;
        }

        Point selectedPoint = this.getSelectedPoint();

        ArrayList<Phrase> phrasesToRead = new ArrayList<>();
        switch (sectionToRead) {
            case ROW:
                phrasesToRead = this.getRowOrColumnPhrases(selectedPoint, true);
                break;
            case COLUMN:
                phrasesToRead = this.getRowOrColumnPhrases(selectedPoint, false);
                break;
            case BLOCK:
                phrasesToRead = this.getBlockPhrases();
                break;
        }

        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    private Point getSelectedPoint() {
        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        int numberOfBlocksWidth = sudokuBoardSize / this.sudokuType.getBlockWidth();
        int numberOfBlocksHeight = sudokuBoardSize / this.sudokuType.getBlockHeight();

        return new Point(
                this.selectedBlockPoint.x * numberOfBlocksWidth + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocksHeight + this.selectedSquarePoint.y
        );
    }

    public int getSudokuBoardSize() {
        return this.sudokuType.getSudokuBoardSize();
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

    public OriginalSudokuGrid getOriginalGrid() {
        return this.originalGrid;
    }
}
