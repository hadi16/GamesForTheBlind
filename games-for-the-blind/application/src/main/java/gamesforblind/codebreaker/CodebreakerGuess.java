package gamesforblind.codebreaker;

import java.util.Arrays;

public class CodebreakerGuess {
    private final int[] guessedCode;
    private final int numberInCorrectPosition;
    private final int numberOfCorrectColor;

    public CodebreakerGuess(int[] correctCode, Integer[] guessedCode) {
        this.guessedCode = Arrays.stream(guessedCode).mapToInt(i -> i).toArray();

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
