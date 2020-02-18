import gamesforblind.codebreaker.CodebreakerGuess;
import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.synthesizer.AudioPlayer;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("ConstantConditions")
public class CodeBreakerTest {
    private final AudioPlayerExecutor testAudioPlayerExecutor = new AudioPlayerExecutor(new AudioPlayer());
    private final CodebreakerType codebreakerType = CodebreakerType.FOUR;
    private final CodebreakerState codebreakerState = new CodebreakerState(
            this.testAudioPlayerExecutor, this.codebreakerType
    );

    public CodeBreakerTest() throws LineUnavailableException {
    }

    @Test
    public void codeToBreakMustNotBeNull() {
        Assert.assertNotNull(this.codebreakerState.getCodeToBreak());
    }

    @Test
    public void codeToBreakMustBeUnique() {
        CodebreakerState codebreakerState = new CodebreakerState(this.testAudioPlayerExecutor, this.codebreakerType);

        Assert.assertNotEquals(this.codebreakerState.getCodeToBreak(), codebreakerState.getCodeToBreak());
    }

    @Test
    public void numberOfCorrectColorsReported() {
        int[] correctCode = new int[]{1, 2, 3, 4};
        Integer[] guessedCode = new Integer[]{1, 1, 1, 2};

        CodebreakerGuess codebreakerGuess = new CodebreakerGuess(correctCode, guessedCode);
        Assert.assertEquals(codebreakerGuess.getNumberInCorrectPosition(), 1);
        Assert.assertEquals(codebreakerGuess.getNumberOfCorrectColor(), 1);
    }

    @Test
    public void maxTrialsPlayedForCodebreakerGame() {
        // 12 long to match the max number of trials.
        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(Arrays.asList(
                null, null, null, null, null, null, null, null, null, null, null, null
        ));

        Assert.assertTrue(CodebreakerState.checkThatGameIsOver(new int[]{}, guessList));
    }

    @Test
    public void correctCodePlayedForCodebreakerGame() {
        int[] correctCode = new int[]{1, 2, 3, 4};
        Integer[] guessedCode = new Integer[]{1, 2, 3, 4};

        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(
                Collections.singletonList(new CodebreakerGuess(correctCode, guessedCode))
        );

        Assert.assertTrue(CodebreakerState.checkThatGameIsOver(correctCode, guessList));
    }

    @Test
    public void codebreakerGameIsNotOver() {
        int[] correctCode = new int[]{1, 2, 3, 4};
        Integer[] guessedCode = new Integer[]{1, 1, 1, 1};

        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(
                Collections.singletonList(new CodebreakerGuess(correctCode, guessedCode))
        );

        Assert.assertFalse(CodebreakerState.checkThatGameIsOver(correctCode, guessList));
    }

    @Test
    public void checkerThrowsWhenCodeToBreakIsNull() {
        try {
            CodebreakerState.checkThatGameIsOver(null, new ArrayList<>());
            Assert.fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {
        }
    }

    @Test
    public void checkerThrowsWhenGuessListIsNull() {
        try {
            CodebreakerState.checkThatGameIsOver(new int[]{}, null);
            Assert.fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {
        }
    }

    // Tests that if the correct code was not guessed for 5 length code and 15 guesses have already been made
    @Test
    public void codebreakerMaxTrialsForFiveLengthCodeLose() {
        // Only 15 guesses allowed
        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(Arrays.asList(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        ));

        Assert.assertTrue(CodebreakerState.checkThatGameIsOver(new int[]{}, guessList));
    }

    // Tests that if the correct code was not guessed for 6 length code and 20 guesses have already been made
    @Test
    public void codebreakerMaxTrialsForSixLengthCodeLose() {
        // 20 long to match the max number of trials.
        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(Arrays.asList(
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null
        ));
        Assert.assertTrue(CodebreakerState.checkThatGameIsOver(new int[]{}, guessList));
    }

    @Test
    public void codebreakerGameisOverRestart() {
        CodebreakerState codebreakerState = new CodebreakerState(this.testAudioPlayerExecutor, CodebreakerType.FOUR);
        int[] correctCode = new int[]{1, 2, 3, 4};
        Integer[] guessedCode = new Integer[]{1, 2, 3, 4};

        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(
                Collections.singletonList(new CodebreakerGuess(correctCode, guessedCode))
        );

        if (CodebreakerState.checkThatGameIsOver(correctCode, guessList)) {
            Assert.assertTrue(codebreakerState.restart());
        }
    }

    @Test
    public void codebreakerHint() {
        // If player wants to guess correctCode[1]
        CodebreakerState codebreakerState = new CodebreakerState(this.testAudioPlayerExecutor, CodebreakerType.FOUR);
        int[] correctCode = new int[]{1, 2, 3, 4};
        Assert.assertEquals(codebreakerState.hint(correctCode[1]), 2);
    }
}
