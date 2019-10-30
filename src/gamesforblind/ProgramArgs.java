package gamesforblind;

import java.util.ArrayList;
import java.util.Arrays;

public class ProgramArgs {
    private final boolean playbackMode;

    public ProgramArgs(String[] args) {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        // If the "-p" argument was passed, we are in playback mode.
        this.playbackMode = argsList.contains("-p");
    }

    public boolean isPlaybackMode() {
        return this.playbackMode;
    }
}
