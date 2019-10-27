package gamesforblind.loader.gui;

/**
 * The various text values that are used throughout the loader GUI.
 * This class prevents the need to directly use String literals throughout the loader GUI code.
 */
public class LoaderGuiConstants {
    // region Button Text Values
    public static final String PLAY_SUDOKU_BUTTON = "PLAY SUDOKU";
    public static final String EXIT_BUTTON = "EXIT";

    /** I want to have "GO" & "BACK" be on separate lines. Swing supports HTML, so I used a HTML break tag for this. */
    public static final String BACK_BUTTON = "<html>GO<br/>BACK</html>";

    public static final String FOUR_BY_FOUR_SUDOKU_BUTTON = "4x4";
    public static final String NINE_BY_NINE_SUDOKU_BUTTON = "9x9";
    // endregion
}
