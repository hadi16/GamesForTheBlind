package gamesforblind;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;

/**
 * Holds any constants that are used in the program.
 */
public class Constants {
    /**
     * An EMPTY square on the Sudoku board is just represented by the constant 0.
     */
    public static final int EMPTY_SUDOKU_SQUARE = 0;

    /**
     * The minimum dimension of each JFrame in the game (the minimum of the height & width of the screen dimensions).
     */
    public static final int FRAME_DIMENSION = (int) Math.min(
            Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
            Toolkit.getDefaultToolkit().getScreenSize().getWidth()
    );

    /**
     * The XML log files directory ("logs" under the root project directory).
     */
    public static final File LOG_FILES_DIRECTORY = new File(
            Paths.get(System.getProperty("user.dir"), "logs/").toString()
    );

    // region Loader Button Text Values

    /* The various button text values that are used throughout the loader GUI. */
    public static final String PLAY_SUDOKU_BUTTON = "PLAY SUDOKU";
    public static final String PLAY_CODEBREAKER_BUTTON = "PLAY CODEBREAKER";
    public static final String EXIT_BUTTON = "EXIT";

    /**
     * I want to have "GO" & "BACK" be on separate lines. Swing supports HTML, so I used a HTML break tag for this.
     */
    public static final String BACK_BUTTON = "<html>GO<br/>BACK</html>";

    public static final String FOUR_BY_FOUR_SUDOKU_BUTTON = "4x4";
    public static final String SIX_BY_SIX_SUDOKU_BUTTON = "6x6";
    public static final String NINE_BY_NINE_SUDOKU_BUTTON = "9x9";

    public static final String FOUR_CODEBREAKER_BUTTON = "4";
    public static final String FIVE_CODEBREAKER_BUTTON = "5";
    public static final String SIX_CODEBREAKER_BUTTON = "6";
    // endregion
}
