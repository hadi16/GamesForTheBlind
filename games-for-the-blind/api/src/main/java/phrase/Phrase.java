package phrase;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;

import static util.DurationUtil.*;
import static util.MapUtil.entry;
import static util.MapUtil.map;

/**
 * Contains all audio phrases in the program along with logic to map between Phrases & keyboard keys, numbers, etc.
 */
public enum Phrase {
    /* General: Blank or Empty Phrases */
    BLANK(" "),
    BLANK_VOICED("BLANK"),

    /* General: Numeric Phrases */
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
    THIRTY_ONE("31"),
    THIRTY_TWO("32"),
    THIRTY_THREE("33"),
    THIRTY_FOUR("34"),
    THIRTY_FIVE("35"),
    THIRTY_SIX("36"),
    THIRTY_SEVEN("37"),
    THIRTY_EIGHT("35"),
    THIRTY_NINE("39"),
    FORTY("40"),
    FORTY_ONE("41"),
    FORTY_TWO("42"),
    FORTY_THREE("43"),
    FORTY_FOUR("44"),
    FORTY_FIVE("45"),
    FORTY_SIX("46"),
    FORTY_SEVEN("47"),
    FORTY_EIGHT("48"),
    FORTY_NINE("49"),
    FIFTY("50"),
    FIFTY_ONE("51"),
    FIFTY_TWO("52"),
    FIFTY_THREE("53"),
    FIFTY_FOUR("54"),
    FIFTY_FIVE("55"),
    FIFTY_SIX("56"),
    FIFTY_SEVEN("57"),
    FIFTY_EIGHT("58"),
    FIFTY_NINE("59"),
    SIXTY("60"),

    /* General: Keyboard Key Phrases */
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
    SHIFT, SLASH, BACKSLASH, ENTER, EQUALS, MINUS, SEMICOLON, COMMA, PERIOD, QUOTE, ESCAPE,
    UP, DOWN, LEFT, RIGHT, CONTROL, BACKSPACE, ALT,
    CAPS_LOCK("CAPS LOCK"), BACK_QUOTE("BACK QUOTE"),
    OPEN_BRACKET("OPEN BRACKET"), CLOSE_BRACKET("CLOSE BRACKET"),

    /* General: Congratulations Message & Unrecognized Key */
    GENERAL_EXITING("Game is exiting. Goodbye!"),
    GENERAL_CONGRATS("YOU FINISHED THE GAME! CONGRATULATIONS!"),
    GENERAL_UNRECOGNIZED_KEY("Unrecognized key:"),

    /* General: First/Last Row/Column */
    GENERAL_FIRST_ROW("You have reached the first row."),
    GENERAL_LAST_ROW("You have reached the last row."),
    GENERAL_FIRST_COLUMN("You have reached the first column."),
    GENERAL_LAST_COLUMN("You have reached the last column."),

    /* General: Timer Phrases */
    GENERAL_TOOK_YOU("It took you:"),
    GENERAL_HOURS("hours"),
    GENERAL_MINUTES("minutes"),
    GENERAL_SECONDS("seconds"),

    /* Loader: Main Screen & Back */
    LOADER_GO_BACK("Press SPACE to go back to game selection screen."),
    LOADER_PLAY_OR_EXIT("Play Sudoku, Code Breaker, or exit? Use the arrow keys."),
    LOADER_SPACE_FOR_SUDOKU("Press SPACE for Sudoku."),
    LOADER_SPACE_FOR_CODEBREAKER("Press SPACE for Code Breaker."),
    LOADER_SPACE_FOR_EXIT("Press SPACE to exit."),

    /* Loader: Sudoku Options Screen */
    LOADER_WHICH_SUDOKU_ALL("Which Sudoku game: 4 by 4, 6 by 6, or 9 by 9? Use arrow keys."),
    LOADER_WHICH_SUDOKU_NO_SIX("Which Sudoku game: 4 by 4 or 9 by 9? Use arrow keys."),
    LOADER_SELECT_SUDOKU_FOUR("Press SPACE for 4 by 4 version of Sudoku."),
    LOADER_SELECT_SUDOKU_SIX("Press SPACE for 6 by 6 version of Sudoku."),
    LOADER_SELECT_SUDOKU_NINE("Press SPACE for 9 by 9 version of Sudoku."),

    /* Loader: Codebreaker Options Screen */
    LOADER_WHICH_CODEBREAKER("Which Code Breaker game: 4, 5, or 6? Use arrow keys."),
    LOADER_SELECT_CODEBREAKER_FOUR("Press SPACE for 4 version of Code Breaker."),
    LOADER_SELECT_CODEBREAKER_FIVE("Press SPACE for 5 version of Code Breaker."),
    LOADER_SELECT_CODEBREAKER_SIX("Press SPACE for 6 version of Code Breaker."),

    /* Sudoku & Codebreaker: locations */
    c1r1, c1r2, c1r3, c1r4, c1r5, c1r6, c1r7, c1r8, c1r9, c1r10,
    c1r11, c1r12, c1r13, c1r14, c1r15, c1r16, c1r17, c1r18, c1r19, c1r20,

    c2r1, c2r2, c2r3, c2r4, c2r5, c2r6, c2r7, c2r8, c2r9, c2r10,
    c2r11, c2r12, c2r13, c2r14, c2r15, c2r16, c2r17, c2r18, c2r19, c2r20,

    c3r1, c3r2, c3r3, c3r4, c3r5, c3r6, c3r7, c3r8, c3r9, c3r10,
    c3r11, c3r12, c3r13, c3r14, c3r15, c3r16, c3r17, c3r18, c3r19, c3r20,

    c4r1, c4r2, c4r3, c4r4, c4r5, c4r6, c4r7, c4r8, c4r9, c4r10,
    c4r11, c4r12, c4r13, c4r14, c4r15, c4r16, c4r17, c4r18, c4r19, c4r20,

    c5r1, c5r2, c5r3, c5r4, c5r5, c5r6, c5r7, c5r8, c5r9, c5r10,
    c5r11, c5r12, c5r13, c5r14, c5r15, c5r16, c5r17, c5r18, c5r19, c5r20,

    c6r1, c6r2, c6r3, c6r4, c6r5, c6r6, c6r7, c6r8, c6r9, c6r10,
    c6r11, c6r12, c6r13, c6r14, c6r15, c6r16, c6r17, c6r18, c6r19, c6r20,

    c7r1, c7r2, c7r3, c7r4, c7r5, c7r6, c7r7, c7r8, c7r9, c7r10,
    c7r11, c7r12, c7r13, c7r14, c7r15, c7r16, c7r17, c7r18, c7r19, c7r20,

    c8r1, c8r2, c8r3, c8r4, c8r5, c8r6, c8r7, c8r8, c8r9, c8r10,
    c8r11, c8r12, c8r13, c8r14, c8r15, c8r16, c8r17, c8r18, c8r19, c8r20,

    c9r1, c9r2, c9r3, c9r4, c9r5, c9r6, c9r7, c9r8, c9r9, c9r10,
    c9r11, c9r12, c9r13, c9r14, c9r15, c9r16, c9r17, c9r18, c9r19, c9r20,

    /* Sudoku: Instructions Phrases. */
    SUDOKU_INSTRUCTIONS_1("Welcome to Sudoku! Each cell must contain the numbers 1 through"),
    SUDOKU_INSTRUCTIONS_2(". Each row, column, or box must have the numbers 1 through"),
    SUDOKU_INSTRUCTIONS_3("without repetition. To hear the numbers in the selected row press S, to hear" +
            " the numbers in the selected column press D, to hear the numbers in the selected box press F. If you" +
            " have selected an empty cell and are stuck, press H to fill in the cell. Navigate using arrow keys." +
            " Press and hold the control key and press an arrow key to jump to the beginning or ending of the" +
            " row or column. When you have selected the box you want to add a number to, select a number on your" +
            " keyboard to try and place it into the cell. Press INSERT and DOWN to hear the current location and" +
            " its contents. Press A to hear how many cells still need to be filled. Press Q twice to restart the game." +
            " Press ALT and M to return to the main menu."
    ),

    /* Sudoku: numbers in same row/column/box */
    SUDOKU_IN_ROW("Numbers in same row:"),
    SUDOKU_IN_COLUMN("Numbers in same column:"),
    SUDOKU_IN_BOX("Numbers in same box:"),

    /* Sudoku: Error Phrases */
    SUDOKU_NO_SELECTED_CELL("You didn't select a cell first."),
    SUDOKU_CANNOT_DELETE_ORIGINAL("You cannot delete an originally set cell."),
    SUDOKU_CANNOT_DELETE_EMPTY("You cannot delete an already empty cell."),
    SUDOKU_INVALID_VALUE("Incorrect value for this cell."),
    SUDOKU_INVALID_NUMBER_TO_FILL("The number to fill must be between 1 and"),
    SUDOKU_PLACED_UNSOLVABLE("Placing number here would make the board unsolvable."),

    /* Sudoku: Empty Cells Left */
    SUDOKU_REMAINING_PLURAL_1("There are"),
    SUDOKU_REMAINING_PLURAL_2("empty cells left on the board."),
    SUDOKU_REMAINING_SINGULAR_1("There is"),
    SUDOKU_REMAINING_SINGULAR_2("empty cell left on the board."),

    /* Sudoku: Information Phrases */
    SUDOKU_YOU_ARE_IN("You are in"),
    SUDOKU_CURRENT_VALUE("Current value in cell:"),
    SUDOKU_YOU_PLACED("You placed"),
    SUDOKU_REMOVED_NUMBER("You removed the number"),

    /* Codebreaker: Instructions Phrases */
    CODEBREAKER_INSTRUCTIONS_1("Welcome to Code Breaker! You need to guess a secret code of"),
    CODEBREAKER_INSTRUCTIONS_2("numbers. To make a guess use the arrow keys to select a space and enter a" +
            " number from 1 to 6. Once you have finalized your guess, hit the space key to see if you guessed correctly." +
            " If you did not guess the right code, the small box to the right of your guess will contain"),
    CODEBREAKER_INSTRUCTIONS_3("pegs of either black or red. A red peg means that one of the numbers" +
            " you have guessed is correct, but it is in the wrong place. A black peg means that one of your numbers" +
            " is the correct number and is in the correct place. If you have guessed correctly, you win the game!" +
            " Press INSERT and DOWN to hear the current location and its contents. To hear previous guesses, use the" +
            " up and down arrow keys to move through the board. Once you are in the desired location, press A to hear" +
            " the previous guess and feedback. For a hint, press H to automatically fill in the selected space. This" +
            " can be done three times. Press Q twice to restart the game. Press ALT and M to return to the main menu."
    ),

    /* Codebreaker: Information Phrases */
    CODEBREAKER_ROW("Row"),
    CODEBREAKER_CODE_PLACED("Code Placed:"),
    CODEBREAKER_GUESS_NUMBER("Guess Number:"),
    CODEBREAKER_CODE_GUESSED("Code guessed:"),
    CODEBREAKER_GUESS_SO_FAR("has this guess so far:"),
    CODEBREAKER_NO_MORE_GUESSES("You're out of guesses. The correct code is: "),

    /* Codebreaker: Error Phrases */
    CODEBREAKER_NO_MORE_HINTS("You have run out of hints."),
    CODEBREAKER_CANNOT_CHANGE("You cannot change a previously guessed code!"),
    CODEBREAKER_NEED_CODE("You didn't enter the whole code yet!"),

    /* Codebreaker: Peg Types */
    CODEBREAKER_BLACK_PEGS("Black Pegs:"),
    CODEBREAKER_RED_PEGS("Red Pegs:");

    /**
     * The directory for all of the Phrase audio files ("resources/phrases" folder under the root of the project).
     */
    public static final Path PHRASE_FILES_DIRECTORY = Paths.get(
            System.getProperty("user.dir"), "../application/src/main/resources/phrases/"
    );

    /**
     * The Phrase's String value, which is the phrase that needs to be fetched via the Google Cloud API.
     */
    private final String phraseValue;

    /**
     * Creates a new Phrase
     * When no phrase value is passed, just set the phrase value to the name of the enumeration member.
     */
    Phrase() {
        this.phraseValue = this.name();
    }

    /**
     * Creates a new Phrase
     *
     * @param phraseValue The phrase value to set to the given enumeration member.
     */
    Phrase(@NotNull String phraseValue) {
        this.phraseValue = phraseValue;
    }

    /**
     * Converts a key code on the keyboard to a Phrase. If no mapping exists, return Phrase.EMPTY.
     *
     * @param keyCode The key code on the keyboard to convert.
     * @return A Phrase that represents the passed key code from the keyboard.
     */
    public static Phrase keyCodeToPhrase(int keyCode) {
        final Map<Integer, Phrase> KEY_CODE_TO_PHRASE = map(
                /* Numeric keyboard values */
                entry(KeyEvent.VK_0, Phrase.ZERO),
                entry(KeyEvent.VK_1, Phrase.ONE),
                entry(KeyEvent.VK_2, Phrase.TWO),
                entry(KeyEvent.VK_3, Phrase.THREE),
                entry(KeyEvent.VK_4, Phrase.FOUR),
                entry(KeyEvent.VK_5, Phrase.FIVE),
                entry(KeyEvent.VK_6, Phrase.SIX),
                entry(KeyEvent.VK_7, Phrase.SEVEN),
                entry(KeyEvent.VK_8, Phrase.EIGHT),
                entry(KeyEvent.VK_9, Phrase.NINE),

                /* Numeric numpad keyboard values */
                entry(KeyEvent.VK_NUMPAD0, Phrase.ZERO),
                entry(KeyEvent.VK_NUMPAD1, Phrase.ONE),
                entry(KeyEvent.VK_NUMPAD2, Phrase.TWO),
                entry(KeyEvent.VK_NUMPAD3, Phrase.THREE),
                entry(KeyEvent.VK_NUMPAD4, Phrase.FOUR),
                entry(KeyEvent.VK_NUMPAD5, Phrase.FIVE),
                entry(KeyEvent.VK_NUMPAD6, Phrase.SIX),
                entry(KeyEvent.VK_NUMPAD7, Phrase.SEVEN),
                entry(KeyEvent.VK_NUMPAD8, Phrase.EIGHT),
                entry(KeyEvent.VK_NUMPAD9, Phrase.NINE),

                /* Alphabetic keyboard values */
                entry(KeyEvent.VK_A, Phrase.A),
                entry(KeyEvent.VK_B, Phrase.B),
                entry(KeyEvent.VK_C, Phrase.C),
                entry(KeyEvent.VK_D, Phrase.D),
                entry(KeyEvent.VK_E, Phrase.E),
                entry(KeyEvent.VK_F, Phrase.F),
                entry(KeyEvent.VK_G, Phrase.G),
                entry(KeyEvent.VK_H, Phrase.H),
                entry(KeyEvent.VK_I, Phrase.I),
                entry(KeyEvent.VK_J, Phrase.J),
                entry(KeyEvent.VK_K, Phrase.K),
                entry(KeyEvent.VK_L, Phrase.L),
                entry(KeyEvent.VK_M, Phrase.M),
                entry(KeyEvent.VK_N, Phrase.N),
                entry(KeyEvent.VK_O, Phrase.O),
                entry(KeyEvent.VK_P, Phrase.P),
                entry(KeyEvent.VK_Q, Phrase.Q),
                entry(KeyEvent.VK_R, Phrase.R),
                entry(KeyEvent.VK_S, Phrase.S),
                entry(KeyEvent.VK_T, Phrase.T),
                entry(KeyEvent.VK_U, Phrase.U),
                entry(KeyEvent.VK_V, Phrase.V),
                entry(KeyEvent.VK_W, Phrase.W),
                entry(KeyEvent.VK_X, Phrase.X),
                entry(KeyEvent.VK_Y, Phrase.Y),
                entry(KeyEvent.VK_Z, Phrase.Z),

                /* Arrow key keyboard values */
                entry(KeyEvent.VK_UP, Phrase.UP),
                entry(KeyEvent.VK_DOWN, Phrase.DOWN),
                entry(KeyEvent.VK_LEFT, Phrase.LEFT),
                entry(KeyEvent.VK_RIGHT, Phrase.RIGHT),

                /* Other keyboard values */
                entry(KeyEvent.VK_SHIFT, Phrase.SHIFT),
                entry(KeyEvent.VK_SLASH, Phrase.SLASH),
                entry(KeyEvent.VK_BACK_SLASH, Phrase.BACKSLASH),
                entry(KeyEvent.VK_ENTER, Phrase.ENTER),
                entry(KeyEvent.VK_EQUALS, Phrase.EQUALS),
                entry(KeyEvent.VK_MINUS, Phrase.MINUS),
                entry(KeyEvent.VK_SEMICOLON, Phrase.SEMICOLON),
                entry(KeyEvent.VK_COMMA, Phrase.COMMA),
                entry(KeyEvent.VK_PERIOD, Phrase.PERIOD),
                entry(KeyEvent.VK_QUOTE, Phrase.QUOTE),
                entry(KeyEvent.VK_CAPS_LOCK, Phrase.CAPS_LOCK),
                entry(KeyEvent.VK_CONTROL, Phrase.CONTROL),
                entry(KeyEvent.VK_BACK_SPACE, Phrase.BACKSPACE),
                entry(KeyEvent.VK_OPEN_BRACKET, Phrase.OPEN_BRACKET),
                entry(KeyEvent.VK_CLOSE_BRACKET, Phrase.CLOSE_BRACKET),
                entry(KeyEvent.VK_BACK_QUOTE, Phrase.BACK_QUOTE),
                entry(KeyEvent.VK_ESCAPE, Phrase.ESCAPE),
                entry(KeyEvent.VK_ALT, Phrase.ALT)
        );

        // Return just an empty Phrase to prevent a null ptr exception (calls audio file that contains no sound).
        Phrase maybePhrase = KEY_CODE_TO_PHRASE.get(keyCode);
        return (maybePhrase != null) ? maybePhrase : Phrase.BLANK;
    }

    public static Phrase convertIntegerToPhrase(int numberToConvert) {
        return convertIntegerToPhrase(numberToConvert, false);
    }

    /**
     * Converts a number between 0 (inclusive) & 30 (inclusive) to a numeric Phrase (0 maps to BLANK_VOICED).
     *
     * @param numberToConvert The number to convert to a Phrase.
     * @return The numeric Phrase that corresponds to the given number that was passed.
     */
    public static Phrase convertIntegerToPhrase(int numberToConvert, boolean useZero) {
        if (useZero && numberToConvert == 0) {
            return Phrase.ZERO;
        }

        final Phrase[] NUM_PHRASE_ARRAY = new Phrase[]{
                Phrase.BLANK_VOICED,
                Phrase.ONE, Phrase.TWO, Phrase.THREE, Phrase.FOUR, Phrase.FIVE, Phrase.SIX, Phrase.SEVEN, Phrase.EIGHT,
                Phrase.NINE, Phrase.TEN, Phrase.ELEVEN, Phrase.TWELVE, Phrase.THIRTEEN, Phrase.FOURTEEN, Phrase.FIFTEEN,
                Phrase.SIXTEEN, Phrase.SEVENTEEN, Phrase.EIGHTEEN, Phrase.NINETEEN, Phrase.TWENTY, Phrase.TWENTY_ONE,
                Phrase.TWENTY_TWO, Phrase.TWENTY_THREE, Phrase.TWENTY_FOUR, Phrase.TWENTY_FIVE, Phrase.TWENTY_SIX,
                Phrase.TWENTY_SEVEN, Phrase.TWENTY_EIGHT, Phrase.TWENTY_NINE, Phrase.THIRTY, Phrase.THIRTY_ONE,
                Phrase.THIRTY_TWO, Phrase.THIRTY_THREE, Phrase.THIRTY_FOUR, Phrase.THIRTY_FIVE, Phrase.THIRTY_SIX,
                Phrase.THIRTY_SEVEN, Phrase.THIRTY_EIGHT, Phrase.THIRTY_NINE, Phrase.FORTY, Phrase.FORTY_ONE,
                Phrase.FORTY_TWO, Phrase.FORTY_THREE, Phrase.FORTY_FOUR, Phrase.FORTY_FIVE, Phrase.FORTY_SIX,
                Phrase.FORTY_SEVEN, Phrase.FORTY_EIGHT, Phrase.FORTY_NINE, Phrase.FIFTY, Phrase.FIFTY_ONE,
                Phrase.FIFTY_TWO, Phrase.FIFTY_THREE, Phrase.FIFTY_FOUR, Phrase.FIFTY_FIVE, Phrase.FIFTY_SIX,
                Phrase.FIFTY_SEVEN, Phrase.FIFTY_EIGHT, Phrase.FIFTY_NINE, Phrase.SIXTY
        };

        /* Verify that passed number is between 0 & 30 (if not, throw an error) */
        if (numberToConvert < 0) {
            throw new IllegalArgumentException("The number to convert must be greater than or equal to 0!");
        }
        if (numberToConvert >= NUM_PHRASE_ARRAY.length) {
            throw new IllegalArgumentException("The number to convert must be less than " + NUM_PHRASE_ARRAY.length);
        }

        return NUM_PHRASE_ARRAY[numberToConvert];
    }

    /**
     * Converts a selected {@link Point} on the board to a Phrase.
     *
     * @param selectedPoint The currently selected {@link Point} in the game.
     * @return The location on the board as a Phrase (e.g. "c1r2", "c2r3", etc.)
     */
    public static Phrase convertPointToLocationPhrase(@NotNull Point selectedPoint) {
        final Phrase[][] LOCATIONS_PHRASE_LIST = new Phrase[][]{
                new Phrase[]{
                        c1r1, c1r2, c1r3, c1r4, c1r5, c1r6, c1r7, c1r8, c1r9, c1r10,
                        c1r11, c1r12, c1r13, c1r14, c1r15, c1r16, c1r17, c1r18, c1r19, c1r20
                },
                new Phrase[]{
                        c2r1, c2r2, c2r3, c2r4, c2r5, c2r6, c2r7, c2r8, c2r9, c2r10,
                        c2r11, c2r12, c2r13, c2r14, c2r15, c2r16, c2r17, c2r18, c2r19, c2r20
                },
                new Phrase[]{
                        c3r1, c3r2, c3r3, c3r4, c3r5, c3r6, c3r7, c3r8, c3r9, c3r10,
                        c3r11, c3r12, c3r13, c3r14, c3r15, c3r16, c3r17, c3r18, c3r19, c3r20
                },
                new Phrase[]{
                        c4r1, c4r2, c4r3, c4r4, c4r5, c4r6, c4r7, c4r8, c4r9, c4r10,
                        c4r11, c4r12, c4r13, c4r14, c4r15, c4r16, c4r17, c4r18, c4r19, c4r20
                },
                new Phrase[]{
                        c5r1, c5r2, c5r3, c5r4, c5r5, c5r6, c5r7, c5r8, c5r9, c5r10,
                        c5r11, c5r12, c5r13, c5r14, c5r15, c5r16, c5r17, c5r18, c5r19, c5r20
                },
                new Phrase[]{
                        c6r1, c6r2, c6r3, c6r4, c6r5, c6r6, c6r7, c6r8, c6r9, c6r10,
                        c6r11, c6r12, c6r13, c6r14, c6r15, c6r16, c6r17, c6r18, c6r19, c6r20
                },
                new Phrase[]{
                        c7r1, c7r2, c7r3, c7r4, c7r5, c7r6, c7r7, c7r8, c7r9, c7r10,
                        c7r11, c7r12, c7r13, c7r14, c7r15, c7r16, c7r17, c7r18, c7r19, c7r20
                },
                new Phrase[]{
                        c8r1, c8r2, c8r3, c8r4, c8r5, c8r6, c8r7, c8r8, c8r9, c8r10,
                        c8r11, c8r12, c8r13, c8r14, c8r15, c8r16, c8r17, c8r18, c8r19, c8r20
                },
                new Phrase[]{
                        c9r1, c9r2, c9r3, c9r4, c9r5, c9r6, c9r7, c9r8, c9r9, c9r10,
                        c9r11, c9r12, c9r13, c9r14, c9r15, c9r16, c9r17, c9r18, c9r19, c9r20
                }
        };

        // x value: 0 maps to "c1", 1 maps to "c2", etc.
        // y value: 0 maps to "r1", 1 maps to "r2", etc.
        return LOCATIONS_PHRASE_LIST[selectedPoint.x][selectedPoint.y];
    }

    public static ArrayList<Phrase> getTimeElapsedPhrases(Duration timeElapsed) {
        final ArrayList<Phrase> phrases = new ArrayList<>(Collections.singletonList(Phrase.GENERAL_TOOK_YOU));

        final int hoursElapsed = toHoursPart(timeElapsed);
        if (hoursElapsed > 0) {
            phrases.addAll(Arrays.asList(Phrase.convertIntegerToPhrase(hoursElapsed), Phrase.GENERAL_HOURS));
        }

        final int minutesElapsed = toMinutesPart(timeElapsed);
        if (minutesElapsed > 0) {
            phrases.addAll(Arrays.asList(Phrase.convertIntegerToPhrase(minutesElapsed), Phrase.GENERAL_MINUTES));
        }

        final int secondsElapsed = toSecondsPart(timeElapsed);
        if (secondsElapsed > 0) {
            phrases.addAll(Arrays.asList(Phrase.convertIntegerToPhrase(secondsElapsed), Phrase.GENERAL_SECONDS));
        }

        return phrases;
    }

    /**
     * Getter for the phraseValue instance variable
     *
     * @return The value of the current Phrase.
     */
    public String getPhraseValue() {
        return this.phraseValue;
    }

    /**
     * Helper method to retrieve a SHA-256 hash of the current phraseValue.
     *
     * @return A SHA-256 hash of the current phraseValue (empty string if exception thrown).
     */
    private String getPhaseHashValue() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(this.phraseValue.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Used for calling into Google Cloud Text-to-Speech & generating the audio files.
     *
     * @return The {@link File} associated with the current Phrase.
     */
    public File getPhraseAudioFile() {
        return new File(
                String.format("%s/%s.wav", PHRASE_FILES_DIRECTORY.toString(), this.getPhaseHashValue())
        );
    }

    /**
     * Used when reading audio files in the game. Uses "resources" so that it works in a JAR file.
     *
     * @return The {@link InputStream} to the given Phrase wrapped by an Optional.
     */
    public Optional<InputStream> getPhraseInputStream() {
        // Audio file is a resource, which is under "phrases/<SHA_256_value>.wav"
        final String audioFileName = String.format("phrases/%s.wav", this.getPhaseHashValue());
        return Optional.ofNullable(
                ClassLoader.getSystemClassLoader().getResourceAsStream(audioFileName)
        );
    }
}
