package synthesizer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private final Clip clip;
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

    public void playPhrases(Phrase[] phrasesToPlay) {
        for (Phrase phrase : phrasesToPlay) {
            while (true) {
                if (!this.clip.isRunning()) {
                    this.clip.close();
                    break;
                }
            }

            this.audioFile = phrase.getPhraseAudioFile();
            this.resetAudioStream();
            this.clip.start();
        }
    }
}
