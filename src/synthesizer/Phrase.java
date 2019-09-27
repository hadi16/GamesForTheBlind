package synthesizer;

import com.google.common.hash.Hashing;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.nio.charset.StandardCharsets;

public enum Phrase {
    IN_ROW("You have the following numbers in the same row: "),
    IN_COLUMN("You have the following numbers in the same column: "),
    IN_BLOCK("You have the following numbers in the same block: "),
    NO_SELECTED_SQUARE("You didn't select a square to fill first."),
    CANNOT_DELETE_ORIGINAL("You cannot delete an originally set square on the board."),
    CELL_VALUE_INVALID("This value is invalid for the cell."),
    SELECTED_BOTH("You have already selected both a block & square on the board."),
    UNRECOGNIZED_KEY("An unrecognized key was pressed on the keyboard."),
    INVALID_NUMBER_TO_FILL_4("The number to fill must be between 1 and 4"),
    INVALID_NUMBER_TO_FILL_9("The number to fill must be between 1 and 9"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9");

    private final String phraseValue;

    Phrase(String phraseValue) {
        this.phraseValue = phraseValue;
    }

    public String getPhraseValue() {
        return this.phraseValue;
    }

    public String getPhaseHashValue() {
        return Hashing.sha256().hashString(phraseValue, StandardCharsets.UTF_8).toString();
    }

    public File getPhraseAudioFile() {
        return new File("phrases/" + this.getPhaseHashValue() + ".mp3");
    }

    public void playPhraseAudioFile() {
        /*new MediaPlayer(
                new Media(this.getPhraseAudioFile().toURI().toString())
        ).play();*/
    }
}
