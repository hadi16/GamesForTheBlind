package gamesforblind.enums;

/**
 * Enumeration for the different types of Codebreaker that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum CodebreakerType {
    FOUR(4, 12),
    FIVE(5, 15),
    SIX(6, 20);

    /**
     * The length of the code
     */
    private final int codeLength;

    private final int numberOfRows;

    /**
     * Creates a new CodebreakerType.
     *
     * @param codeLength The length of code.
     */
    CodebreakerType(int codeLength, int numberOfRows) {
        this.codeLength = codeLength;
        this.numberOfRows = numberOfRows;
    }

    /**
     * @return The length of code.
     */
    public int getCodeLength() {
        return this.codeLength;
    }

    public int getNumberOfRows() {
        return this.numberOfRows;
    }
}
