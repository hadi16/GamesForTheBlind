package synthesizer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class AudioPlayer implements Runnable {
    private final Clip clip;
    private ArrayList<Phrase> phrasesToPlay = new ArrayList<>();
    private File audioFile;

    public AudioPlayer() throws LineUnavailableException {
        this.clip = AudioSystem.getClip();
    }

    private void resetAudioStream() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.audioFile.getAbsoluteFile());
            this.clip.open(audioInputStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playPhrase() {
        synchronized (this) {
            if (this.phrasesToPlay.isEmpty()) {
                return;
            }

            this.clip.close();

            this.audioFile = this.phrasesToPlay.remove(0).getPhraseAudioFile();
            this.resetAudioStream();

            this.clip.start();
        }
    }

    public void replacePhraseToPlay(Phrase phrase) {
        synchronized (this) {
            if (this.clip.isRunning()) {
                this.clip.close();
            }

            this.phrasesToPlay = new ArrayList<>(Collections.singletonList(phrase));
        }
    }

    public void replacePhraseToPlay(ArrayList<Phrase> phrases) {
        synchronized (this) {
            if (this.clip.isRunning()) {
                this.clip.close();
            }

            this.phrasesToPlay = phrases;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!this.clip.isRunning()) {
                this.playPhrase();
            }
        }
    }
}
