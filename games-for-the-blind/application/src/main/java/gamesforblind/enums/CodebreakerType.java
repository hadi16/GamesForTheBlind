package gamesforblind.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration for the different types of Sudoku that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum CodebreakerType {
    FOUR(4),
    FIVE(5),
    SIX(6);

    /**
     * The length of code
     */
    private final int blockWidth;

    /**
     * Creates a new CodebreakerType.
     *
     * @param blockWidth          The length of code.
     */
    CodebreakerType(int blockWidth) {
        this.blockWidth = blockWidth;
    }

    /**
     * @return The length of code.
     */
    public int getBlockWidth() {
        return this.blockWidth;
    }
}