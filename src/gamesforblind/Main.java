package gamesforblind;

import gamesforblind.loader.GameLoader;
import gamesforblind.synthesizer.AudioFileBuilder;

/**
 * Class contains the sole entry point for the program.
 */
public class Main {
    /**
     * The sole entry point into the program.
     *
     * @param args The command line arguments to the program.
     */
    public static void main(String[] args) {
        if (Constants.BUILD_PHRASES) {
            AudioFileBuilder audioFileBuilder = new AudioFileBuilder();
            audioFileBuilder.createPhraseAudioFiles();
            audioFileBuilder.deleteOldPhraseAudioFiles();
        }

        // Uses the universal launcher for the games.
        new GameLoader();
    }
}
