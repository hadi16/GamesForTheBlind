package gamesforblind;

import gamesforblind.loader.GameLoader;
import gamesforblind.synthesizer.AudioFileBuilder;

public class Main {
    private static final boolean BUILD_PHRASES = true;

    public static void main(String[] args) {
        if (BUILD_PHRASES) {
            AudioFileBuilder audioFileBuilder = new AudioFileBuilder();
            audioFileBuilder.createPhraseAudioFiles();
            audioFileBuilder.deleteOldPhraseAudioFiles();
        }

        new GameLoader();
    }
}
