package gamesforblind.synthesizer;

import com.google.common.hash.Hashing;

import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public enum Phrase {
    SPACE_FOR_EXIT("Press the SPACE BAR to exit."),
    SPACE_FOR_SUDOKU("Press the SPACE BAR to play Sudoku."),
    GO_BACK_TO_GAME_SELECTION("Press the SPACE BAR to go back to the game selection screen."),
    SELECT_SUDOKU_FOUR("Press the SPACE BAR to play the 4 by 4 version of Sudoku."),
    SELECT_SUDOKU_NINE("Press the SPACE BAR to play the 9 by 9 version of Sudoku."),
    WHICH_SUDOKU_GAME("Which Sudoku game would you like to play, 4 by 4 or 9 by 9? " +
            "Use the arrow keys to make your selection."),
    PLAY_OR_EXIT("Would you like to play Sudoku or exit? Use the arrow keys to make your selection."),
    EXITING("The game is exiting. Goodbye!"),

    IN_ROW("You have the following numbers in the same row:"),
    IN_COLUMN("You have the following numbers in the same column:"),
    IN_BLOCK("You have the following numbers in the same block:"),

    NO_SELECTED_SQUARE("You didn't select a square first."),
    CANNOT_DELETE_ORIGINAL("You cannot delete an originally set square on the board."),
    CELL_VALUE_INVALID("This value is invalid for the cell."),
    SELECTED_BOTH("You have already selected both a block & square on the board."),
    UNRECOGNIZED_KEY("An unrecognized key was pressed on the keyboard:"),

    INVALID_NUMBER_TO_FILL_4("The number to fill must be between 1 and 4"),
    INVALID_NUMBER_TO_FILL_9("The number to fill must be between 1 and 9"),

    BLANK(" "),

    EMPTY("EMPTY"),

    ZERO("0"),
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

    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
    SHIFT, SLASH, BACKSLASH, ENTER, EQUALS, MINUS, SEMICOLON, COMMA, PERIOD, QUOTE, ESCAPE,
    UP, DOWN, LEFT, RIGHT, CONTROL, BACKSPACE, ALT,
    CAPS_LOCK("CAPS LOCK"), BACK_QUOTE("BACK QUOTE"),
    OPEN_BRACKET("OPEN BRACKET"), CLOSE_BRACKET("CLOSE BRACKET"),

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

    Phrase() {
        this.phraseValue = this.name();
    }

    Phrase(String phraseValue) {
        this.phraseValue = phraseValue;
    }

    public String getPhraseValue() {
        return this.phraseValue;
    }

    private String getPhaseHashValue() {
        return Hashing.sha256().hashString(this.phraseValue, StandardCharsets.UTF_8).toString();
    }

    public File getPhraseAudioFile() {
        return new File(
                String.format("%s/%s.wav", PHRASE_FILES_DIRECTORY.toString(), this.getPhaseHashValue())
        );
    }

    public static Phrase keyCodeToPhrase(int keyCode) {
        final Map<Integer, Phrase> KEY_CODE_TO_PHRASE = ofEntries(
                entry(KeyEvent.VK_0,                Phrase.ZERO),
                entry(KeyEvent.VK_1,                Phrase.ONE),
                entry(KeyEvent.VK_2,                Phrase.TWO),
                entry(KeyEvent.VK_3,                Phrase.THREE),
                entry(KeyEvent.VK_4,                Phrase.FOUR),
                entry(KeyEvent.VK_5,                Phrase.FIVE),
                entry(KeyEvent.VK_6,                Phrase.SIX),
                entry(KeyEvent.VK_7,                Phrase.SEVEN),
                entry(KeyEvent.VK_8,                Phrase.EIGHT),
                entry(KeyEvent.VK_9,                Phrase.NINE),

                entry(KeyEvent.VK_NUMPAD0,          Phrase.ZERO),
                entry(KeyEvent.VK_NUMPAD1,          Phrase.ONE),
                entry(KeyEvent.VK_NUMPAD2,          Phrase.TWO),
                entry(KeyEvent.VK_NUMPAD3,          Phrase.THREE),
                entry(KeyEvent.VK_NUMPAD4,          Phrase.FOUR),
                entry(KeyEvent.VK_NUMPAD5,          Phrase.FIVE),
                entry(KeyEvent.VK_NUMPAD6,          Phrase.SIX),
                entry(KeyEvent.VK_NUMPAD7,          Phrase.SEVEN),
                entry(KeyEvent.VK_NUMPAD8,          Phrase.EIGHT),
                entry(KeyEvent.VK_NUMPAD9,          Phrase.NINE),

                entry(KeyEvent.VK_A,                Phrase.A),
                entry(KeyEvent.VK_B,                Phrase.B),
                entry(KeyEvent.VK_C,                Phrase.C),
                entry(KeyEvent.VK_D,                Phrase.D),
                entry(KeyEvent.VK_E,                Phrase.E),
                entry(KeyEvent.VK_F,                Phrase.F),
                entry(KeyEvent.VK_G,                Phrase.G),
                entry(KeyEvent.VK_H,                Phrase.H),
                entry(KeyEvent.VK_I,                Phrase.I),
                entry(KeyEvent.VK_J,                Phrase.J),
                entry(KeyEvent.VK_K,                Phrase.K),
                entry(KeyEvent.VK_L,                Phrase.L),
                entry(KeyEvent.VK_M,                Phrase.M),
                entry(KeyEvent.VK_N,                Phrase.N),
                entry(KeyEvent.VK_O,                Phrase.O),
                entry(KeyEvent.VK_P,                Phrase.P),
                entry(KeyEvent.VK_Q,                Phrase.Q),
                entry(KeyEvent.VK_R,                Phrase.R),
                entry(KeyEvent.VK_S,                Phrase.S),
                entry(KeyEvent.VK_T,                Phrase.T),
                entry(KeyEvent.VK_U,                Phrase.U),
                entry(KeyEvent.VK_V,                Phrase.V),
                entry(KeyEvent.VK_W,                Phrase.W),
                entry(KeyEvent.VK_X,                Phrase.X),
                entry(KeyEvent.VK_Y,                Phrase.Y),
                entry(KeyEvent.VK_Z,                Phrase.Z),

                entry(KeyEvent.VK_UP,               Phrase.UP),
                entry(KeyEvent.VK_DOWN,             Phrase.DOWN),
                entry(KeyEvent.VK_LEFT,             Phrase.LEFT),
                entry(KeyEvent.VK_RIGHT,            Phrase.RIGHT),

                entry(KeyEvent.VK_SHIFT,            Phrase.SHIFT),
                entry(KeyEvent.VK_SLASH,            Phrase.SLASH),
                entry(KeyEvent.VK_BACK_SLASH,       Phrase.BACKSLASH),
                entry(KeyEvent.VK_ENTER,            Phrase.ENTER),
                entry(KeyEvent.VK_EQUALS,           Phrase.EQUALS),
                entry(KeyEvent.VK_MINUS,            Phrase.MINUS),
                entry(KeyEvent.VK_SEMICOLON,        Phrase.SEMICOLON),
                entry(KeyEvent.VK_COMMA,            Phrase.COMMA),
                entry(KeyEvent.VK_PERIOD,           Phrase.PERIOD),
                entry(KeyEvent.VK_QUOTE,            Phrase.QUOTE),
                entry(KeyEvent.VK_CAPS_LOCK,        Phrase.CAPS_LOCK),
                entry(KeyEvent.VK_CONTROL,          Phrase.CONTROL),
                entry(KeyEvent.VK_BACK_SPACE,       Phrase.BACKSPACE),
                entry(KeyEvent.VK_OPEN_BRACKET,     Phrase.OPEN_BRACKET),
                entry(KeyEvent.VK_CLOSE_BRACKET,    Phrase.CLOSE_BRACKET),
                entry(KeyEvent.VK_BACK_QUOTE,       Phrase.BACK_QUOTE),
                entry(KeyEvent.VK_ESCAPE,           Phrase.ESCAPE),
                entry(KeyEvent.VK_ALT,              Phrase.ALT)
        );

        return Objects.requireNonNullElse(KEY_CODE_TO_PHRASE.get(keyCode), Phrase.BLANK);
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