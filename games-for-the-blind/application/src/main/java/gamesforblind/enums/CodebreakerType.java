package gamesforblind.enums;

/**
 * Enumeration for the different types of Codebreaker that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum CodebreakerType {
    FOUR(4),
    FIVE(5),
    SIX(6);

    /**
     * The length of code
     */
    private final int codebreakerType;

    /**
     * Creates a new CodebreakerType.
     *
     * @param codebreakerType The length of code.
     */
    CodebreakerType(int codebreakerType) {
        this.codebreakerType = codebreakerType;
    }

    /**
     * @return The length of code.
     */
    public int getNumber() {
        return this.codebreakerType;
    }

}
