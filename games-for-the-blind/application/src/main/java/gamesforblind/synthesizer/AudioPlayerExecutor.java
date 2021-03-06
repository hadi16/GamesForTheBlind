package gamesforblind.synthesizer;

import org.jetbrains.annotations.NotNull;
import phrase.Phrase;

import java.util.ArrayList;

/**
 * Class to call into the threaded audio player.
 */
public class AudioPlayerExecutor {
    /**
     * The program's audio player.
     */
    private final AudioPlayer audioPlayer;

    /**
     * Creates a dummy AudioPlayerExecutor for testing purposes.
     */
    public AudioPlayerExecutor() {
        this.audioPlayer = null;
    }

    /**
     * Creates a new AudioPlayerExecutor.
     *
     * @param audioPlayer The program's audio player.
     */
    public AudioPlayerExecutor(@NotNull AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Replaces the Phrases to play for the audio player w/ a single Phrase & prints the Phrase text to stdout.
     *
     * @param phrase The Phrase to replace in the program's audio player.
     */
    public void replacePhraseAndPrint(@NotNull Phrase phrase) {
        System.out.println(phrase.getPhraseValue());

        if (this.audioPlayer != null) {
            this.audioPlayer.replacePhraseToPlay(phrase);
        }
    }

    /**
     * Replaces the Phrases to play for the audio player w/ a list of Phrases & prints their text to stdout.
     *
     * @param phrases The list of Phrases to replace in the program's audio player.
     */
    public void replacePhraseAndPrint(@NotNull ArrayList<Phrase> phrases) {
        // Prints all of the phrases on a single line, separated with a space (" ").
        ArrayList<String> phraseStringList = new ArrayList<>();
        phrases.forEach(phrase -> phraseStringList.add(phrase.getPhraseValue()));
        String phraseString = String.join(" ", phraseStringList);

        if (!phraseString.trim().isEmpty()) {
            System.out.println(phraseString);
        }

        if (this.audioPlayer != null) {
            this.audioPlayer.replacePhraseToPlay(phrases);
        }
    }

    /**
     * Terminates the audio player by just calling into the same method in the audio player instance variable.
     */
    public void terminateAudioPlayer() {
        if (this.audioPlayer != null) {
            this.audioPlayer.terminateAudioPlayer();
        }
    }
}
