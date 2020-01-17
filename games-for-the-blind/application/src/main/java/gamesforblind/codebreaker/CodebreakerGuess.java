package gamesforblind.codebreaker;

import java.awt.*;

public class CodebreakerGuess {
    private final Color[] guessedCode;
    private final int numberInCorrectPosition;
    private final int numberOfCorrectColor;

    public CodebreakerGuess(Color[] codebreakerCode, Color[] guessedCode) {
        this.guessedCode = guessedCode;

        // TODO: Properly implement this.
        this.numberInCorrectPosition = 0;
        this.numberOfCorrectColor = 0;
    }

    public int getNumberInCorrectPosition() {
        return this.numberInCorrectPosition;
    }

    public int getNumberOfCorrectColor() {
        return this.numberOfCorrectColor;
    }
}
