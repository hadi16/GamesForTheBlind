package gamesforblind;

import java.awt.*;

/**
 * Holds any constants that are used throughout the whole program.
 */
public class Constants {
    /**
     * The minimum dimension of each JFrame in the game (the minimum of the height & width of the screen dimensions).
     */
    public static final int FRAME_DIMENSION = (int) Math.min(
            Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
            Toolkit.getDefaultToolkit().getScreenSize().getWidth()
    );
}
