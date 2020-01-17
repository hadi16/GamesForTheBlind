package gamesforblind.enums;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration for the different types of Mastermind that are supported in the game (e.g. 4,5,6).
 */
public enum MastermindType {
    // The 4 Mastermind game
    FOUR(4), FIVE(5), SIX(6);
    /**
     * The number of boxes
     */
    private final int boxSize;


    /**
     * Creates a new MastermindType.
     *
     * @param boxSize          The number of boxes
     */
    MastermindType(int blockWidth) {
        this.boxSize = blockWidth;
    }

    /**
     * @return The number of boxes
     */
    public int getBlockWidth() {
        return this.boxSize;
    }


}
