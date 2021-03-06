package gamesforblind.sudoku;

import gamesforblind.enums.*;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.action.SudokuHotKeyAction;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Generator;
import gamesforblind.sudoku.generator.Grid;
import gamesforblind.sudoku.generator.Solver;
import gamesforblind.sudoku.interfaces.SudokuArrowKeyInterface;
import gamesforblind.sudoku.interfaces.SudokuBlockSelectionInterface;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import phrase.Phrase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;
import static java.util.Arrays.*;

/**
 * Class that contains information about the current state of the Sudoku board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Sudoku.
 */
public class SudokuState {
    private final SudokuType sudokuType;
    private final SudokuKeyboardInterface sudokuKeyboardInterface;
    private final AudioPlayerExecutor audioPlayerExecutor;

    private Instant startingInstant;
    private OriginalSudokuGrid originalGrid;
    private Grid sudokuGrid;
    private ArrayList<Point> originallyFilledSquares;

    private int numberOfEmptyCells;
    private boolean gameOver;

    /**
     * Creates a new SudokuState. Important: only used when the game is in playback mode.
     *
     * @param interfaceType       The keyboard interface type that is being used in the game.
     * @param sudokuType          Whether the Sudoku game is a 4x4, 6x6, or 9x9 variant.
     * @param audioPlayerExecutor Calls into the threaded audio player for the game.
     * @param originalGrid        The original state of the Sudoku board, which is used to restore the saved game state.
     */
    public SudokuState(
            @NotNull InterfaceType interfaceType,
            @NotNull SudokuType sudokuType,
            @NotNull AudioPlayerExecutor audioPlayerExecutor,
            @NotNull OriginalSudokuGrid originalGrid
    ) {
        this.gameOver = false;
        this.startingInstant = Instant.now();

        this.sudokuType = sudokuType;
        this.audioPlayerExecutor = audioPlayerExecutor;

        this.numberOfEmptyCells = this.getInitialNumberOfEmptyCells(sudokuType.getSudokuBoardSize());
        this.sudokuGrid = Grid.of(originalGrid.getGrid(), sudokuType);
        this.originalGrid = originalGrid;
        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();

        this.sudokuKeyboardInterface = this.initializeKeyboardInterface(interfaceType);
    }

    /**
     * Creates a new SudokuState. Important: only used when the game is in regular mode (NOT playback mode).
     *
     * @param interfaceType       The keyboard interface type that is being used in the game.
     * @param sudokuType          Whether the Sudoku game is a 4x4, 6x6, or 9x9 variant.
     * @param audioPlayerExecutor Calls into the threaded audio player for the game.
     */
    public SudokuState(
            @NotNull InterfaceType interfaceType,
            @NotNull SudokuType sudokuType,
            @NotNull AudioPlayerExecutor audioPlayerExecutor
    ) {
        this.gameOver = false;
        this.startingInstant = Instant.now();

        this.sudokuType = sudokuType;
        this.audioPlayerExecutor = audioPlayerExecutor;

        this.numberOfEmptyCells = this.getInitialNumberOfEmptyCells(this.sudokuType.getSudokuBoardSize());
        this.sudokuGrid = new Generator(this.sudokuType).generate(this.numberOfEmptyCells);
        this.originalGrid = OriginalSudokuGrid.of(this.sudokuGrid.toIntArray());
        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();

        this.sudokuKeyboardInterface = this.initializeKeyboardInterface(interfaceType);
    }

    /**
     * Resets the Sudoku state (provides restart functionality for the game).
     *
     * @param originalGrid If we are in playback mode, the {@link OriginalSudokuGrid} to restore (otherwise, null).
     */
    public void resetSudokuState(@Nullable OriginalSudokuGrid originalGrid) {
        this.gameOver = false;
        this.startingInstant = Instant.now();
        this.numberOfEmptyCells = this.getInitialNumberOfEmptyCells(this.sudokuType.getSudokuBoardSize());

        if (originalGrid != null) {
            // Case 1: we are in playback mode
            this.sudokuGrid = Grid.of(originalGrid.getGrid(), this.sudokuType);
            this.originalGrid = originalGrid;
        } else {
            // Case 2: we are not in playback mode (just generate a new random Sudoku board).
            this.sudokuGrid = new Generator(this.sudokuType).generate(this.numberOfEmptyCells);
            this.originalGrid = OriginalSudokuGrid.of(this.sudokuGrid.toIntArray());
        }

        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();
    }

    /**
     * Gets instance of {@link SudokuKeyboardInterface} that's appropriate to the type of interface the user requested.
     *
     * @param interfaceType The keyboard interface type that is being used in the game.
     * @return An instance of {@link SudokuKeyboardInterface} that corresponds to the passed {@link InterfaceType}
     */
    private SudokuKeyboardInterface initializeKeyboardInterface(@NotNull InterfaceType interfaceType) {
        switch (interfaceType) {
            case ARROW_KEY_INTERFACE:
                return new SudokuArrowKeyInterface(this);
            case BLOCK_SELECTION_INTERFACE:
                return new SudokuBlockSelectionInterface(this);
            default:
                throw new IllegalArgumentException("Incorrect interface type passed to Sudoku state: " + interfaceType);
        }
    }

    /**
     * Gets the number of empty cells that we initially want in the game.
     *
     * @param sudokuBoardSize The number of squares on each side of the board (e.g. 9x9 board --> 9)
     * @return The number of initial empty cells that I want in the game.
     */
    private int getInitialNumberOfEmptyCells(int sudokuBoardSize) {
        return (sudokuBoardSize * sudokuBoardSize) / 3;
    }

    /**
     * Gets the list of initially filled squares in the Sudoku board. This should be called in the constructor.
     *
     * @return A list of {@link Point}s representing all of the initially filled squares in the Sudoku board.
     */
    private ArrayList<Point> initializeOriginallyFilledSquares() {
        ArrayList<Point> originallyFilledSquares = new ArrayList<>();

        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        for (int rowIdx = 0; rowIdx < sudokuBoardSize; rowIdx++) {
            for (int columnIdx = 0; columnIdx < sudokuBoardSize; columnIdx++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(rowIdx, columnIdx);

                // Only add it to the list of originally filled squares if the cell isn't empty.
                if (cellAtPosition.getValue() != EMPTY_SUDOKU_SQUARE) {
                    originallyFilledSquares.add(new Point(columnIdx, rowIdx));
                }
            }
        }

        return originallyFilledSquares;
    }

    private ArrayList<Phrase> maybeGetCongratsMessage() {
        // Case 1: return the congrats message.
        if (this.numberOfEmptyCells != 0) {
            return new ArrayList<>();
        }

        final ArrayList<Phrase> relevantPhrases = new ArrayList<>(Collections.singletonList(Phrase.GENERAL_CONGRATS));
        final Duration timeElapsed = Duration.between(this.startingInstant, Instant.now());

        // Indicates that an error occurred with the time measurement.
        if (timeElapsed.isNegative()) {
            return relevantPhrases;
        }

        relevantPhrases.addAll(Phrase.getTimeElapsedPhrases(timeElapsed));
        return relevantPhrases;
    }

    public void readRemainingCellsToFill() {
        if (this.numberOfEmptyCells == 1) {
            // Case 1: return the singular version of empty squares left.
            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(asList(
                    Phrase.SUDOKU_REMAINING_SINGULAR_1,
                    Phrase.ONE,
                    Phrase.SUDOKU_REMAINING_SINGULAR_2
            )));
        } else {
            // Case 2: return the plural version of empty squares left.
            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(asList(
                    Phrase.SUDOKU_REMAINING_PLURAL_1,
                    Phrase.convertIntegerToPhrase(this.numberOfEmptyCells),
                    Phrase.SUDOKU_REMAINING_PLURAL_2
            )));
        }
    }

    /**
     * If the user tries to delete an originally selected square, read a message that
     * "You cannot delete an originally set square on the board", along with message on current cell value.
     *
     * @param pointToCheck The {@link Point} to check.
     * @return true if the passed {@link Point} was one of the originally set squares. Otherwise, false.
     */
    private boolean readCannotDeleteOriginal(@NotNull Point pointToCheck) {
        // Case 1: the point to check was not one of the originally filled squares on the board.
        if (!this.originallyFilledSquares.contains(pointToCheck)) {
            return false;
        }

        // Case 2: the point to check was one of the originally filled squares on the board.
        Cell cellToSet = this.sudokuGrid.getCell(pointToCheck.y, pointToCheck.x);

        // Square will always contain a number from 1-9 or 1-4 (never 0), since it is an originally set square.
        this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(asList(
                Phrase.SUDOKU_CANNOT_DELETE_ORIGINAL,
                Phrase.SUDOKU_CURRENT_VALUE,
                Phrase.convertIntegerToPhrase(cellToSet.getValue())
        )));

        return true;
    }

    public void stopReadingPhrases() {
        this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>());
    }

    /**
     * Sets the currently selected {@link Point} to a given number (if the number is valid for the cell).
     *
     * @param numberToFill The number to fill the currently selected Point with.
     */
    public void setSquareNumber(int numberToFill) {
        Optional<Point> maybePointToSet = this.sudokuKeyboardInterface.getSelectedPoint();

        // Case 1: no Point is selected on the board.
        if (!maybePointToSet.isPresent()) {
            this.readNoSelectedSquareMessage();
            return;
        }

        Point pointToSet = maybePointToSet.get();
        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);

        // Case 2: the user tries to delete an empty cell.
        if (numberToFill == EMPTY_SUDOKU_SQUARE && cellToSet.getValue() == EMPTY_SUDOKU_SQUARE) {
            this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.SUDOKU_CANNOT_DELETE_EMPTY);
            return;
        }

        // Case 3: the user tries to delete/change an originally set square on the board.
        if (this.readCannotDeleteOriginal(pointToSet)) {
            return;
        }

        // Case 4: the user wishes to delete the cell value.
        if (numberToFill == EMPTY_SUDOKU_SQUARE) {
            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(asList(
                    Phrase.SUDOKU_REMOVED_NUMBER, Phrase.convertIntegerToPhrase(cellToSet.getValue()))
            ));
            cellToSet.setValue(EMPTY_SUDOKU_SQUARE);
            return;
        }

        final int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();

        // Case 5: the user tries to fill a square with an invalid value (e.g. 6 on a 4x4 board).
        if (!(numberToFill > 0 && numberToFill <= sudokuBoardSize)) {
            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(asList(
                    Phrase.SUDOKU_INVALID_NUMBER_TO_FILL, Phrase.convertIntegerToPhrase(sudokuBoardSize)
            )));
            return;
        }

        // Case 6: the numeric value is not valid for the selected cell (only 4x4 version).
        if (this.sudokuType == SudokuType.FOUR_BY_FOUR && !this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
            this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.SUDOKU_INVALID_VALUE);
            return;
        }

        // Case 7: the cell value is replaced with the passed numeric value.
        if (cellToSet.getValue() == EMPTY_SUDOKU_SQUARE) {
            this.numberOfEmptyCells--;
        }

        cellToSet.setValue(numberToFill);

        // Case 8: the board would not be solvable from this new state (only 4x4 version).
        if (this.sudokuType == SudokuType.FOUR_BY_FOUR) {
            Solver solver = new Solver(sudokuBoardSize);
            if (!solver.isSolvable(new Grid(this.sudokuGrid))) {
                cellToSet.setValue(EMPTY_SUDOKU_SQUARE);
                this.numberOfEmptyCells++;
                this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.SUDOKU_PLACED_UNSOLVABLE);
                return;
            }
        }

        if (this.numberOfEmptyCells == 0) {
            this.gameOver = true;
        }

        // Tell the user which number was placed along with the number of remaining empty squares on the board.
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(
                asList(Phrase.SUDOKU_YOU_PLACED, Phrase.convertIntegerToPhrase(numberToFill))
        );
        phrasesToRead.addAll(this.maybeGetCongratsMessage());
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    /**
     * Reads the instructions for the 4x4, 6x6, or 9x9 game.
     */
    public void readInstructions() {
        final Phrase boardSizePhrase = Phrase.convertIntegerToPhrase(this.sudokuType.getSudokuBoardSize());

        ArrayList<Phrase> instructionsPhrases = new ArrayList<>(asList(
                Phrase.SUDOKU_INSTRUCTIONS_1,
                boardSizePhrase,
                Phrase.SUDOKU_INSTRUCTIONS_2,
                boardSizePhrase,
                Phrase.SUDOKU_INSTRUCTIONS_3
        ));

        this.audioPlayerExecutor.replacePhraseAndPrint(instructionsPhrases);
    }

    /**
     * Reads a message that the user needs to first select a square on the board,
     * followed by telling them how many squares they have left to fill.
     */
    private void readNoSelectedSquareMessage() {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(Collections.singletonList(Phrase.SUDOKU_NO_SELECTED_CELL));
        phrasesToRead.addAll(this.maybeGetCongratsMessage());
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    /**
     * Fills the currently selected square on the board with a valid value (gives the user a hint).
     */
    public void giveHint() {
        Optional<Point> maybePointToSet = this.sudokuKeyboardInterface.getSelectedPoint();

        // Case 1: no point is currently selected on the board.
        if (!maybePointToSet.isPresent()) {
            this.readNoSelectedSquareMessage();
            return;
        }

        // Case 2: the user attempted to fill an originally filled square on the board.
        Point pointToSet = maybePointToSet.get();
        if (this.readCannotDeleteOriginal(pointToSet)) {
            return;
        }

        Cell cellToSet = this.sudokuGrid.getCell(pointToSet.y, pointToSet.x);

        // Case 3: the user has selected a square & it is not an originally filled square.
        for (int cellValue = 1; cellValue <= this.sudokuType.getSudokuBoardSize(); cellValue++) {
            if (this.sudokuGrid.isValidValueForCell(cellToSet, cellValue)) {
                if (cellToSet.getValue() == EMPTY_SUDOKU_SQUARE) {
                    this.numberOfEmptyCells--;
                }

                cellToSet.setValue(cellValue);
                break;
            }
        }

        // Read out the new value of this cell along with the amount of cells left to fill.
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(
                Collections.singletonList(Phrase.convertIntegerToPhrase(cellToSet.getValue()))
        );
        phrasesToRead.addAll(this.maybeGetCongratsMessage());
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);

        // If there are no cells left to fill, the game is over.
        if (this.numberOfEmptyCells == 0) {
            this.gameOver = true;
        }
    }

    /**
     * Reads a message that an unrecognized key was pressed, followed by which key it was.
     *
     * @param keyCode Key code of the unrecognized key that was pressed (compare against values in {@link KeyEvent}).
     */
    public void readUnrecognizedKey(int keyCode) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(asList(
                Phrase.GENERAL_UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(keyCode)
        ));
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    /**
     * Gets the {@link Phrase} values of all the cells in the same row or column (as the currently selected Point).
     *
     * @param selectedPoint The {@link Point} that is currently selected in the game.
     * @param readRow       If true, reads the values in the same ROW. If false, reads the values in the same column.
     * @return A list of {@link Phrase}s corresponding to all of the numeric values of the cells in the same row/column.
     */
    private ArrayList<Phrase> getRowOrColumnPhrases(@NotNull Point selectedPoint, boolean readRow) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>();

        // Begin with "You have the following numbers in the same row (or column)"
        phrasesToRead.add(readRow ? Phrase.SUDOKU_IN_ROW : Phrase.SUDOKU_IN_COLUMN);

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

    /**
     * Gets the {@link Phrase} values of all the cells in the same box (as the currently selected Point).
     *
     * @param selectedPoint The {@link Point} that is currently selected in the game.
     * @return A list of {@link Phrase}s corresponding to all of the numeric values of the cells in the same box.
     */
    private ArrayList<Phrase> getBlockPhrases(@NotNull Point selectedPoint) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>();

        // Begin with "You have the following numbers in the same box"
        phrasesToRead.add(Phrase.SUDOKU_IN_BOX);

        // Need to divide by blockHeight first to get the truncated quotient [e.g. (7 / 3) * 3 = 2 * 3 = 6]
        int blockHeight = this.sudokuType.getBlockHeight();
        int currentRowIndex = (selectedPoint.y / blockHeight) * blockHeight;

        final int MAX_ROW_INDEX = currentRowIndex + blockHeight;
        for (; currentRowIndex < MAX_ROW_INDEX; currentRowIndex++) {
            // Need to divide by blockWidth first to get the truncated quotient [e.g. (5 / 3) * 3 = 1 * 3 = 3]
            int blockWidth = this.sudokuType.getBlockWidth();
            int currentColumnIndex = (selectedPoint.x / blockWidth) * blockWidth;

            final int MAX_COLUMN_INDEX = currentColumnIndex + blockWidth;
            for (; currentColumnIndex < MAX_COLUMN_INDEX; currentColumnIndex++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(currentRowIndex, currentColumnIndex);
                phrasesToRead.add(Phrase.convertIntegerToPhrase(cellAtPosition.getValue()));
            }
        }

        return phrasesToRead;
    }

    /**
     * Reads the location of the currently selected {@link Cell} on the Sudoku board & its value (if one is selected).
     */
    public void readSelectedLocationWithValue() {
        Optional<Point> maybeSelectedPoint = this.sudokuKeyboardInterface.getSelectedPoint();
        if (maybeSelectedPoint.isPresent()) {
            final Point selectedPoint = maybeSelectedPoint.get();
            final int cellValue = this.sudokuGrid.getCell(selectedPoint.y, selectedPoint.x).getValue();
            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(asList(
                    Phrase.SUDOKU_YOU_ARE_IN,
                    Phrase.convertPointToLocationPhrase(selectedPoint),
                    Phrase.SUDOKU_CURRENT_VALUE,
                    Phrase.convertIntegerToPhrase(cellValue)
            )));
        }
    }

    /**
     * Sets highlighted point based on pressed hot key & reads off new selected square.
     *
     * @param sudokuHotKeyAction The {@link SudokuHotKeyAction} that was sent to the game.
     */
    public void processHotKey(@NotNull SudokuHotKeyAction sudokuHotKeyAction) {
        this.setHighlightedPoint(sudokuHotKeyAction.getArrowKeyDirection());
        this.getSelectedSquarePhrase().ifPresent(this.audioPlayerExecutor::replacePhraseAndPrint);
    }

    /**
     * Sets the highlighted point (not based on hot key) & reads off selected square.
     *
     * @param sudokuHighlightAction The {@link SudokuHighlightAction} that was sent to the game.
     */
    public void changeHighlightedSquare(@NotNull SudokuHighlightAction sudokuHighlightAction) {
        final Point previousPoint = this.sudokuKeyboardInterface.getSelectedPoint().orElse(null);
        this.setHighlightedPoint(
                sudokuHighlightAction.getPointToHighlight(), sudokuHighlightAction.getInputType()
        );
        this.getSelectedSquarePhrase().ifPresent(phrase -> {
            final ArrayList<Phrase> phrases = new ArrayList<>(Collections.singletonList(phrase));
            this.sudokuKeyboardInterface.getSelectedPoint().ifPresent(point -> {
                if (previousPoint == null) {
                    return;
                }

                final int maxIndex = this.sudokuType.getSudokuBoardSize() - 1;
                if (point.x == 0 && previousPoint.x != 0) {
                    phrases.add(Phrase.GENERAL_FIRST_COLUMN);
                } else if (point.y == 0 && previousPoint.y != 0) {
                    phrases.add(Phrase.GENERAL_FIRST_ROW);
                } else if (point.x == maxIndex && previousPoint.x != maxIndex) {
                    phrases.add(Phrase.GENERAL_LAST_COLUMN);
                } else if (point.y == maxIndex && previousPoint.y != maxIndex) {
                    phrases.add(Phrase.GENERAL_LAST_ROW);
                }
            });
            this.audioPlayerExecutor.replacePhraseAndPrint(phrases);
        });
    }

    public Optional<Phrase> getSelectedSquarePhrase() {
        Optional<Point> maybeSelectedPoint = this.sudokuKeyboardInterface.getSelectedPoint();
        if (maybeSelectedPoint.isPresent()) {
            Point selectedPoint = maybeSelectedPoint.get();
            Cell selectedCell = this.sudokuGrid.getCell(selectedPoint.y, selectedPoint.x);

            return Optional.of(
                    Phrase.convertIntegerToPhrase(selectedCell.getValue())
            );
        }

        return Optional.empty();
    }

    /**
     * Reads the values from the row, column, or box of the currently selected {@link Point}.
     *
     * @param sectionToRead Whether to read a row, column, or box.
     */
    public void readBoardSection(@NotNull SudokuSection sectionToRead) {
        Optional<Point> maybeSelectedPoint = this.sudokuKeyboardInterface.getSelectedPoint();

        // If no point is currently selected, inform the user & return.
        if (!maybeSelectedPoint.isPresent()) {
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

    /**
     * Setter that calls the setHighlightedPoint() method within {@link SudokuKeyboardInterface}.
     *
     * @param pointToSet The {@link Point} that the user has sent via an action.
     * @param inputType  Whether this action was sent via a keyboard or mouse input.
     */
    public void setHighlightedPoint(@Nullable Point pointToSet, @NotNull InputType inputType) {
        this.sudokuKeyboardInterface.setHighlightedPoint(pointToSet, inputType);
    }

    /**
     * For HOT KEYS: setter that calls the setHighlightedPoint() method within {@link SudokuKeyboardInterface}.
     *
     * @param arrowKeyDirection The {@link ArrowKeyDirection} that was pressed with this hot key (e.g. left arrow key).
     */
    public void setHighlightedPoint(@NotNull ArrowKeyDirection arrowKeyDirection) {
        this.sudokuKeyboardInterface.setHighlightedPoint(arrowKeyDirection);
    }

    /**
     * Getter for sudokuGrid
     *
     * @return The current state of the Sudoku {@link Grid} in the game.
     */
    public Grid getSudokuGrid() {
        return this.sudokuGrid;
    }

    /**
     * Getter for gameOver
     *
     * @return true if the game is over (otherwise false).
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Getter for originalGrid
     *
     * @return The original state of the Sudoku board, which is used for logging functionality in the game.
     */
    public OriginalSudokuGrid getOriginalGrid() {
        return this.originalGrid;
    }

    /**
     * Getter for sudokuKeyboardInterface
     *
     * @return The keyboard interface that is being used in the game.
     */
    public SudokuKeyboardInterface getSudokuKeyboardInterface() {
        return this.sudokuKeyboardInterface;
    }

    /**
     * Getter for sudokuType
     *
     * @return Whether the Sudoku game is a 4x4, 6x6, or 9x9 variant.
     */
    public SudokuType getSudokuType() {
        return this.sudokuType;
    }

    public Instant getTime() {
        return this.startingInstant;
    }

    public ArrayList<Point> getOriginallyFilledSquares() {
        return this.originallyFilledSquares;
    }
}
