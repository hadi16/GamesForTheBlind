package synthesizer;

import com.google.common.hash.Hashing;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    NINE("9"),
    INSTRUCTIONS9("welcome to sudoku! each of the nine blocks must contain the numbers 1 through 9 within its square." +
            "each number can only appear once in a row, column or box." +
            "each nine-square column, or nine-square row, within the entire board, must also have the numbers 1 through 9, without repetition."),
    INSTRUCTIONS4("welcome to sudoku! each of the nine blocks must contain the numbers 1 through 9 within its square." +
            "each number can only appear once in a row, column or box." +
            "each nine-square column, or nine-square row, within the entire board, must also have the numbers 1 through 9, without repetition."),
    CONGRATS("YOU'VE FINISHED THE GAME! CONGRATULATIONS!"),
    EMPTY_PIECES_OF_BOARD_1("There are "),
    EMPTY_PIECES_OF_BOARD_2("Empty squares left on the board."),
    EMPTY_PIECES_OF_BOARD_3("There is "),
    EMPTY_PIECES_OF_BOARD_4("Empty square left on the board."),
    EMPTY_PIECES_IN_SECTION_1("Empty squares left in this section."),
    EMPTY(" "),
    CURRENT_VALUE("The current value in this box is "),
    PLACED_NUM("You have placed a "),
    ;


    public static final Path PHRASE_FILES_DIRECTORY = Paths.get(System.getProperty("user.dir"), "phrases/");
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
        return new File(
                String.format("%s/%s.wav", PHRASE_FILES_DIRECTORY.toString(), this.getPhaseHashValue())
        );
    }
}