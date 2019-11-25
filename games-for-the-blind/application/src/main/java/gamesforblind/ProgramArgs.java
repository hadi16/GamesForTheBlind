package gamesforblind;

import gamesforblind.enums.InterfaceType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that contains all of the program's various option values. This is used to parse through the
 * command line arguments once & provide a simpler/unified way to access the program's options set by the user.
 */
public class ProgramArgs {
    /**
     * If true, user wishes to play back an old game using a stored XML log file.
     */
    private final boolean playbackMode;

    /**
     * Which interface the user wishes to use. If "-a" was passed, the new arrow key
     * interface is used; otherwise, the old block selection interface is used instead.
     */
    private final InterfaceType selectedInterfaceType;

    /**
     * Create a new ProgramArgs object.
     *
     * @param args The String arguments that were passed to main().
     */
    public ProgramArgs(@NotNull String[] args) {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        // If the "-p" argument was passed, we are in playback mode.
        this.playbackMode = argsList.contains("-p");

        // If the "-b" argument was passed, set the interface to the old block selection interface.
        // Otherwise, we are in the arrow key interface.
        if (argsList.contains("-b")) {
            this.selectedInterfaceType = InterfaceType.BLOCK_SELECTION_INTERFACE;
        } else {
            this.selectedInterfaceType = InterfaceType.ARROW_KEY_INTERFACE;
        }
    }

    /**
     * Getter for playbackMode
     *
     * @return true if the program is in playback mode (otherwise, false).
     */
    public boolean isPlaybackMode() {
        return this.playbackMode;
    }

    /**
     * Getter for selectedInterfaceType
     *
     * @return The type of keyboard interface that should be used throughout the game.
     */
    public InterfaceType getSelectedInterfaceType() {
        return this.selectedInterfaceType;
    }
}
