package synthesizer;

import com.google.common.hash.Hashing;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Phrase {
    IN_ROW("You have the following numbers in the same row:"),
    IN_COLUMN("You have the following numbers in the same column:"),
    IN_BLOCK("You have the following numbers in the same block:"),

    NO_SELECTED_SQUARE("You didn't select a square to fill first."),
    CANNOT_DELETE_ORIGINAL("You cannot delete an originally set square on the board."),
    CELL_VALUE_INVALID("This value is invalid for the cell."),
    SELECTED_BOTH("You have already selected both a block & square on the board."),
    UNRECOGNIZED_KEY("An unrecognized key was pressed on the keyboard: "),

    INVALID_NUMBER_TO_FILL_4("The number to fill must be between 1 and 4"),
    INVALID_NUMBER_TO_FILL_9("The number to fill must be between 1 and 9"),

    EMPTY("EMPTY"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    ELEVEN("11"),
    TWELVE("12"),
    THIRTEEN("13"),
    FOURTEEN("14"),
    FIFTEEN("15"),
    SIXTEEN("16"),
    SEVENTEEN("17"),
    EIGHTEEN("18"),
    NINETEEN("19"),
    TWENTY("20"),
    TWENTY_ONE("21"),
    TWENTY_TWO("22"),
    TWENTY_THREE("23"),
    TWENTY_FOUR("24"),
    TWENTY_FIVE("25"),
    TWENTY_SIX("26"),
    TWENTY_SEVEN("27"),
    TWENTY_EIGHT("28"),
    TWENTY_NINE("29"),
    THIRTY("30"),

    INSTRUCTIONS_4("Welcome to Sudoku! Each of the four blocks must contain the numbers 1 through 4 " +
            "within its square. Each number can only appear once in a row, column or box. Each four-square column, " +
            "or four-square row, within the entire board, must also have the numbers 1 through 4, without repetition."),
    INSTRUCTIONS_9("Welcome to Sudoku! Each of the nine blocks must contain the numbers 1 through 9 " +
            "within its square. Each number can only appear once in a row, column or box. Each nine-square column, " +
            "or nine-square row, within the entire board, must also have the numbers 1 through 9, without repetition."),

    CONGRATS("YOU'VE FINISHED THE GAME! CONGRATULATIONS!"),

    EMPTY_PIECES_OF_BOARD_PLURAL_1("There are"),
    EMPTY_PIECES_OF_BOARD_PLURAL_2("empty squares left on the board."),
    EMPTY_PIECES_OF_BOARD_SINGULAR_1("There is"),
    EMPTY_PIECES_OF_BOARD_SINGULAR_2("empty square left on the board."),
    EMPTY_PIECES_IN_SECTION("empty squares left in this section."),

    CURRENT_VALUE("The current value in this box is"),
    PLACED_NUM("You have placed a"),
    REMOVED_NUM("You have removed the number");

    public static final Path PHRASE_FILES_DIRECTORY = Paths.get(System.getProperty("user.dir"), "phrases/");
    private final String phraseValue;

    Phrase(String phraseValue) {
        this.phraseValue = phraseValue;
    }

    public String getPhraseValue() {
        return this.phraseValue;
    }

    public String getPhaseHashValue() {
        return Hashing.sha256().hashString(this.phraseValue, StandardCharsets.UTF_8).toString();
    }

    public File getPhraseAudioFile() {
        return new File(
                String.format("%s/%s.wav", PHRASE_FILES_DIRECTORY.toString(), this.getPhaseHashValue())
        );
    }

    public static Phrase convertIntegerToPhrase(int numberToConvert) {
        final Phrase[] NUM_PHRASE_LIST = new Phrase[]{
                Phrase.EMPTY,           Phrase.ONE,             Phrase.TWO,             Phrase.THREE,
                Phrase.FOUR,            Phrase.FIVE,            Phrase.SIX,             Phrase.SEVEN,
                Phrase.EIGHT,           Phrase.NINE,            Phrase.TEN,             Phrase.ELEVEN,
                Phrase.TWELVE,          Phrase.THIRTEEN,        Phrase.FOURTEEN,        Phrase.FIFTEEN,
                Phrase.SIXTEEN,         Phrase.SEVENTEEN,       Phrase.EIGHTEEN,        Phrase.NINETEEN,
                Phrase.TWENTY,          Phrase.TWENTY_ONE,      Phrase.TWENTY_TWO,      Phrase.TWENTY_THREE,
                Phrase.TWENTY_FOUR,     Phrase.TWENTY_FIVE,     Phrase.TWENTY_SIX,      Phrase.TWENTY_SEVEN,
                Phrase.TWENTY_EIGHT,    Phrase.TWENTY_NINE,     Phrase.THIRTY
        };

        if (numberToConvert < 0) {
            throw new IllegalArgumentException("The number to convert must be greater than or equal to 0!");
        }

        if (numberToConvert >= NUM_PHRASE_LIST.length) {
            throw new IllegalArgumentException("The number to convert must be less than " + NUM_PHRASE_LIST.length);
        }

        return NUM_PHRASE_LIST[numberToConvert];
    }
}
