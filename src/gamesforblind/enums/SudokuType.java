package gamesforblind.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Enumeration for the different types of Sudoku that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum SudokuType {
    // The 4x4 Sudoku board can work with either keyboard interface in the game.
    FOUR_BY_FOUR(
            2, 2, 4,
            new ArrayList<>(Arrays.asList(InterfaceType.ARROW_KEY_INTERFACE, InterfaceType.BLOCK_SELECTION_INTERFACE))
    ),
    // The 6x6 Sudoku board can work with only the arrow key interface in the game. A 6x6 board
    // has 3x2 blocks of 2x3, which makes the block selection interface particularly difficult to implement.
    SIX_BY_SIX(
            3, 2, 6,
            new ArrayList<>(Collections.singletonList(InterfaceType.ARROW_KEY_INTERFACE))
    ),
    // The 9x9 Sudoku board can work with either keyboard interface in the game.
    NINE_BY_NINE(
            3, 3, 9,
            new ArrayList<>(Arrays.asList(InterfaceType.ARROW_KEY_INTERFACE, InterfaceType.BLOCK_SELECTION_INTERFACE))
    );

    /**
     * The number of columns in each block.
     */
    private final int blockWidth;

    /**
     * The number of rows in each block.
     */
    private final int blockHeight;

    /**
     * The overall dimension of the Sudoku board (e.g. 9x9 --> 9).
     */
    private final int sudokuBoardSize;
    private final ArrayList<InterfaceType> supportedSudokuInterfaces;

    /**
     * Creates a new SudokuType.
     *
     * @param blockWidth                The number of columns in each block.
     * @param blockHeight               The number of rows in each block.
     * @param sudokuBoardSize           The overall dimension of the Sudoku board (e.g. 9x9 --> 9).
     * @param supportedSudokuInterfaces The supported keyboard interface types.
     */
    SudokuType(
            int blockWidth, int blockHeight, int sudokuBoardSize, ArrayList<InterfaceType> supportedSudokuInterfaces
    ) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.sudokuBoardSize = sudokuBoardSize;
        this.supportedSudokuInterfaces = supportedSudokuInterfaces;
    }

    /**
     * @return The number of columns in each block.
     */
    public int getBlockWidth() {
        return this.blockWidth;
    }

    /**
     * @return The number of rows in each block.
     */
    public int getBlockHeight() {
        return this.blockHeight;
    }

    /**
     * @return The overall dimension of the Sudoku board (e.g. 9x9 --> 9).
     */
    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }

    /**
     * @return The supported keyboard interface types.
     */
    public ArrayList<InterfaceType> getSupportedSudokuInterfaces() {
        return this.supportedSudokuInterfaces;
    }
}
