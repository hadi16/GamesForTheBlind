package gamesforblind;

import gamesforblind.loader.GameLoader;
import gamesforblind.synthesizer.AudioFileBuilder;

/**
 * Class contains the sole entry point for the program.
 */
public class Main {
    /**
     * If set to true, audio files are created & removed based on the phrase values in the Phrase enumeration.
     * This should be set to false when deployed, since games should be strictly offline-only.
     */
    private static final boolean BUILD_PHRASES = true;

    /**
     * The sole entry point into the program.
     *
     * @param args The command line arguments to the program.
     */
    public static void main(String[] args) {
        if (BUILD_PHRASES) {
            AudioFileBuilder audioFileBuilder = new AudioFileBuilder();
            audioFileBuilder.createPhraseAudioFiles();
            audioFileBuilder.deleteOldPhraseAudioFiles();
        }

        // Uses the universal launcher for the games.
        new GameLoader();
    }
}
