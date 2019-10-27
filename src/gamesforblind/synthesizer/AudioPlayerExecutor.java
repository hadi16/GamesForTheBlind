package gamesforblind.synthesizer;

import java.util.ArrayList;

/**
 * Class to call into the threaded audio player.
 */
public class AudioPlayerExecutor {
    /** The program's audio player. */
    private final AudioPlayer audioPlayer;

    /**
     * Creates a new {@link AudioPlayerExecutor}.
     * @param audioPlayer The program's audio player.
     */
    public AudioPlayerExecutor(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Replaces the Phrases to play for the audio player w/ a single Phrase & prints the Phrase text to stdout.
     * @param phrase The Phrase to replace in the program's audio player.
     */
    public void replacePhraseAndPrint(Phrase phrase) {
        System.out.println(phrase.getPhraseValue());
        this.audioPlayer.replacePhraseToPlay(phrase);
    }

    /**
     * Replaces the Phrases to play for the audio player w/ a list of Phrases & prints their text to stdout.
     * @param phrases The list of Phrases to replace in the program's audio player.
     */
    public void replacePhraseAndPrint(ArrayList<Phrase> phrases) {
        // Prints all of the phrases on a single line, separated with a space (" ").
        ArrayList<String> phraseStringList = new ArrayList<>();
        phrases.forEach(phrase -> phraseStringList.add(phrase.getPhraseValue()));
        System.out.println(String.join(" ", phraseStringList));

        this.audioPlayer.replacePhraseToPlay(phrases);
    }

    /**
     * Terminates the audio player by just calling into the same method in the audio player instance variable.
     */
    public void terminateAudioPlayer() {
        this.audioPlayer.terminateAudioPlayer();
    }
}
