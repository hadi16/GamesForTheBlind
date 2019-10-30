package gamesforblind.synthesizer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Audio player for the program. Run on a separate thread to prevent audio from blocking the main program thread.
 */
public class AudioPlayer implements Runnable {
    /**
     * The audio clip that is currently playing.
     */
    private final Clip clip;

    /**
     * When this is set to false, the audio player is terminated.
     */
    private boolean isActive = true;

    /**
     * The remaining phrases that need to be played in the game.
     */
    private ArrayList<Phrase> phrasesToPlay = new ArrayList<>();

    /**
     * Creates a new {@link AudioPlayer}
     *
     * @throws LineUnavailableException Thrown when the {@link Clip} in the audio player cannot be started.
     */
    public AudioPlayer() throws LineUnavailableException {
        this.clip = AudioSystem.getClip();
    }

    /**
     * Resets the audio stream to the passed audio file.
     *
     * @param audioFile The audio file to initialize.
     */
    private void resetAudioStream(File audioFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile.getAbsoluteFile());
            this.clip.open(audioInputStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the first remaining Phrase in the phrasesToPlay instance variable.
     */
    private void playPhrase() {
        synchronized (this) {
            if (this.phrasesToPlay.isEmpty()) {
                return;
            }

            // Resets the audio stream to the first remaining Phrase & start the clip.
            this.resetAudioStream(this.phrasesToPlay.remove(0).getPhraseAudioFile());
            this.clip.start();
        }
    }

    /**
     * Replaces the phrases to play with a single Phrase.
     *
     * @param phrase The {@link Phrase} to replace the phrases to play with.
     */
    public void replacePhraseToPlay(Phrase phrase) {
        synchronized (this) {
            // Since I'm REPLACING the phrases to play, I need to first close the running clip.
            if (this.clip.isRunning()) {
                this.clip.close();
            }

            this.phrasesToPlay = new ArrayList<>(Collections.singletonList(phrase));
        }
    }

    /**
     * Replaces the phrases to play with a list of Phrases
     *
     * @param phrases The {@link ArrayList} of {@link Phrase}s to replace the phrases to play with.
     */
    public void replacePhraseToPlay(ArrayList<Phrase> phrases) {
        synchronized (this) {
            // Since I'm REPLACING the phrases to play, I need to first close the running clip.
            if (this.clip.isRunning()) {
                this.clip.close();
            }

            this.phrasesToPlay = phrases;
        }
    }

    /**
     * The entry point to the AudioPlayer thread (loops until isActive is set to false).
     */
    @Override
    public void run() {
        // Terminate only when isActive is set to false.
        while (this.isActive) {
            synchronized (this) {
                if (!this.clip.isRunning()) {
                    this.clip.close();
                    this.playPhrase();
                }
            }

            try {
                // Divide by 1000 to convert microseconds to milliseconds.
                Thread.sleep(this.clip.getMicrosecondLength() / 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Terminate the audio player (sets the isActive flag to false).
     */
    public void terminateAudioPlayer() {
        synchronized (this) {
            this.isActive = false;
        }
    }
}
