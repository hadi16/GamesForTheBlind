package gamesforblind;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;

/**
 * Holds any constants that are used throughout the whole program.
 */
public class Constants {
    /**
     * If set to true, audio files are created & removed based on the phrase values in the Phrase enumeration.
     * This should be set to false when deployed, since games should be strictly offline-only.
     */
    public static final boolean BUILD_PHRASES = false;

    /**
     * The minimum dimension of each JFrame in the game (the minimum of the height & width of the screen dimensions).
     */
    public static final int FRAME_DIMENSION = (int) Math.min(
            Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
            Toolkit.getDefaultToolkit().getScreenSize().getWidth()
    );

    public static final boolean SAVE_LOGS = true;

    public static final File LOG_FILES_DIRECTORY = new File(
            Paths.get(System.getProperty("user.dir"), "logs/").toString()
    );

    // region Loader Button Text Values
    /* The various button text values that are used throughout the loader GUI. */
    public static final String PLAY_SUDOKU_BUTTON = "PLAY SUDOKU";
    public static final String EXIT_BUTTON = "EXIT";

    /**
     * I want to have "GO" & "BACK" be on separate lines. Swing supports HTML, so I used a HTML break tag for this.
     */
    public static final String BACK_BUTTON = "<html>GO<br/>BACK</html>";

    public static final String FOUR_BY_FOUR_SUDOKU_BUTTON = "4x4";
    public static final String NINE_BY_NINE_SUDOKU_BUTTON = "9x9";
    // endregion
}
