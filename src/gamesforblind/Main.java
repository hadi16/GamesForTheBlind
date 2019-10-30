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
        System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");

        if (Constants.BUILD_PHRASES) {
            AudioFileBuilder audioFileBuilder = new AudioFileBuilder();
            audioFileBuilder.createPhraseAudioFiles();
            audioFileBuilder.deleteOldPhraseAudioFiles();
        }

        new GameLoader(new ProgramArgs(args));
    }
}
