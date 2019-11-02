package gamesforblind;

import gamesforblind.enums.InterfaceType;

import java.util.ArrayList;
import java.util.Arrays;

public class ProgramArgs {
    private final boolean playbackMode;
    private final InterfaceType selectedInterfaceType;

    public ProgramArgs(String[] args) {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        // If the "-p" argument was passed, we are in playback mode.
        this.playbackMode = argsList.contains("-p");

        if (argsList.contains("-a")) {
            this.selectedInterfaceType = InterfaceType.ARROW_KEY_INTERFACE;
        } else {
            this.selectedInterfaceType = InterfaceType.BLOCK_SELECTION_INTERFACE;
        }
    }

    public boolean isPlaybackMode() {
        return this.playbackMode;
    }

    public InterfaceType getSelectedInterfaceType() {
        return this.selectedInterfaceType;
    }
}
