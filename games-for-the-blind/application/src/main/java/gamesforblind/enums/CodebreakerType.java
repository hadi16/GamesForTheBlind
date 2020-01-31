package gamesforblind.enums;

/**
 * Enumeration for the different types of Codebreaker that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum CodebreakerType {
    FOUR(4),
    FIVE(5),
    SIX(6);

    /**
     * The length of the code
     */
    private final int codeLength;

    /**
     * Creates a new CodebreakerType.
     *
     * @param codeLength The length of code.
     */
    CodebreakerType(int codeLength) {
        this.codeLength = codeLength;
    }

    /**
     * @return The length of code.
     */
    public int getCodeLength() {
        return this.codeLength;
    }
}
