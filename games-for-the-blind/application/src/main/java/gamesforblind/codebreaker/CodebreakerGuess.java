package gamesforblind.codebreaker;

import java.util.ArrayList;
import java.util.Arrays;

public class CodebreakerGuess {
    private final int[] guessedCode;
    private final int numberInCorrectPosition;
    private final int numberOfCorrectColor;

    public CodebreakerGuess(int[] correctCode, Integer[] guessedCode) {
        this.guessedCode = Arrays.stream(guessedCode).mapToInt(i -> i).toArray();

        this.numberInCorrectPosition = this.initNumberInCorrectPosition(correctCode);
        this.numberOfCorrectColor = this.initNumberOfCorrectColor(correctCode) - this.numberInCorrectPosition;
    }

    private int initNumberInCorrectPosition(int[] correctCode) {
        int numberInCorrectPosition = 0;
        for (int i = 0; i < correctCode.length; i++) {
            if (this.guessedCode[i] == correctCode[i]) {
                numberInCorrectPosition++;
            }
        }
        return numberInCorrectPosition;
    }

    private int initNumberOfCorrectColor(int[] correctCode) {
        int numberOfCorrectColor = 0;

        ArrayList<Integer> correctCodeCopy = new ArrayList<Integer>();
        for (int value : correctCode) {
            correctCodeCopy.add(value);
        }

        for (int i : this.guessedCode) {
            int foundIndex = correctCodeCopy.indexOf(i);
            if (foundIndex != -1) {
                numberOfCorrectColor++;
                correctCodeCopy.remove(foundIndex);
            }
        }

        return numberOfCorrectColor;
    }

    public int[] getGuessedCode() {
        return this.guessedCode;
    }

    public int getNumberInCorrectPosition() {
        return this.numberInCorrectPosition;
    }

    public int getNumberOfCorrectColor() {
        return this.numberOfCorrectColor;
    }
}
