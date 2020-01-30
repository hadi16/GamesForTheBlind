import gamesforblind.codebreaker.CodebreakerGuess;
import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.synthesizer.AudioPlayer;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CodeBreakerTest {
    private final AudioPlayerExecutor testAudioPlayerExecutor = new AudioPlayerExecutor(new AudioPlayer());
    private final CodebreakerType codebreakerType = CodebreakerType.FOUR;
    private final CodebreakerState codebreakerState = new CodebreakerState(this.testAudioPlayerExecutor, this.codebreakerType);

    public CodeBreakerTest() throws LineUnavailableException {
    }

    @Test
    public void codeSizeMustMatchCodeToBreakSize() {
        Assert.assertEquals(this.codebreakerState.getCodeSize(), this.codebreakerState.getCodeToBreak().length);
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
        Color[] codebreakerCode = new Color[]{
                Color.BLACK, Color.BLUE, Color.GREEN, Color.WHITE
        };

        Color[] codeGuess = new Color[]{
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK
        };

        CodebreakerGuess codebreakerGuess = new CodebreakerGuess(codebreakerCode, codeGuess);
        Assert.assertEquals(codebreakerGuess.getNumberInCorrectPosition(), 1);
        Assert.assertEquals(codebreakerGuess.getNumberOfCorrectColor(), 1);
    }

    @Test
    public void maxTrialsPlayedForCodebreakerGame() {
        // 12 long to match the max number of trials.
        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(Arrays.asList(
                null, null, null, null, null, null, null, null, null, null, null, null
        ));

        Assert.assertTrue(CodebreakerState.checkThatGameIsOver(new Color[]{}, guessList));
    }

    @Test
    public void correctCodePlayedForCodebreakerGame() {
        Color[] codebreakerCode = new Color[]{
                Color.BLACK, Color.BLUE, Color.GREEN, Color.WHITE
        };

        Color[] guessedCode = new Color[]{
                Color.BLACK, Color.BLUE, Color.GREEN, Color.WHITE
        };

        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(
                Collections.singletonList(new CodebreakerGuess(codebreakerCode, guessedCode))
        );

        Assert.assertTrue(CodebreakerState.checkThatGameIsOver(codebreakerCode, guessList));
    }

    @Test
    public void codebreakerGameIsNotOver() {
        Color[] codebreakerCode = new Color[]{
                Color.BLACK, Color.BLUE, Color.GREEN, Color.WHITE
        };

        Color[] guessedCode = new Color[]{
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK
        };

        ArrayList<CodebreakerGuess> guessList = new ArrayList<>(
                Collections.singletonList(new CodebreakerGuess(codebreakerCode, guessedCode))
        );

        Assert.assertFalse(CodebreakerState.checkThatGameIsOver(codebreakerCode, guessList));
    }

    @Test
    public void checkerThrowsWhenCodeToBreakIsNull() {
        try {
            CodebreakerState.checkThatGameIsOver(null, new ArrayList<>());
            Assert.fail();
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void checkerThrowsWhenGuessListIsNull() {
        try {
            CodebreakerState.checkThatGameIsOver(new Color[]{}, null);
            Assert.fail();
        } catch (NullPointerException e) {
        }
    }
}
