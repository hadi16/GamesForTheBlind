package gamesforblind.enums;

import gamesforblind.sudoku.interfaces.SudokuArrowKeyInterface;
import gamesforblind.sudoku.interfaces.SudokuBlockSelectionInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Enumeration for the different types of Sudoku that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum SudokuType {
    FOUR_BY_FOUR(
            2, 2, 4,
            new ArrayList<>(Arrays.asList(InterfaceType.ARROW_KEY_INTERFACE, InterfaceType.BLOCK_SELECTION_INTERFACE))
    ),
    SIX_BY_SIX(
            3, 2, 6,
            new ArrayList<>(Collections.singletonList(InterfaceType.ARROW_KEY_INTERFACE))
    ),
    NINE_BY_NINE(
            3, 3, 9,
            new ArrayList<>(Arrays.asList(InterfaceType.ARROW_KEY_INTERFACE, InterfaceType.BLOCK_SELECTION_INTERFACE))
    );

    private final int blockWidth;
    private final int blockHeight;
    private final int sudokuBoardSize;
    private final ArrayList<InterfaceType> supportedSudokuInterfaces;

    SudokuType(
            int blockWidth, int blockHeight, int sudokuBoardSize, ArrayList<InterfaceType> supportedSudokuInterfaces
    ) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.sudokuBoardSize = sudokuBoardSize;
        this.supportedSudokuInterfaces = supportedSudokuInterfaces;
    }

    public int getBlockWidth() {
        return this.blockWidth;
    }

    public int getBlockHeight() {
        return this.blockHeight;
    }

    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }

    public ArrayList<InterfaceType> getSupportedSudokuInterfaces() {
        return this.supportedSudokuInterfaces;
    }
}
