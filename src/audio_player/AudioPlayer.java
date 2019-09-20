package audio_player;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class AudioPlayer {
    private final String audioFilePath;
    private final Clip clip;
    private final Scanner keyboard;

    private long currentFrame;
    private PlayStatus status; // current status of clip

    public AudioPlayer(Scanner keyboard) throws LineUnavailableException {
        this.keyboard = keyboard;
        this.clip = AudioSystem.getClip();

        this.audioFilePath = this.getAudioFilePath();
        this.resetAudioStream();
        this.runAudioPlayerPrompt();
    }

    private String getAudioFilePath() {
        final String AUDIO_DIRECTORY = "audio/";
        final Map<String, String> inputStringToAudioFilePath = Map.of(
                "H", AUDIO_DIRECTORY + "helloWorld.wav",
                "M", AUDIO_DIRECTORY + "testWave.wav"
        );

        String audioFilePath = null;
        while (audioFilePath == null) {
            System.out.println("Enter a Letter:");
            inputStringToAudioFilePath.forEach(
                    (inputChar, audioFile) -> System.out.println(inputChar + " -> " + audioFile)
            );
            audioFilePath = inputStringToAudioFilePath.get(this.keyboard.nextLine());
        }

        return audioFilePath;
    }

    private void runAudioPlayerPrompt() {
        final Map<Integer, Runnable> choicesToFunction = Map.of(
                AudioOption.PAUSE.getOptionValue(), this::pause,
                AudioOption.STOP.getOptionValue(), this::stop,
                AudioOption.RESUME.getOptionValue(), this::resumeAudio
        );

        this.play();

        int selectedChoice = 0;
        while (selectedChoice != AudioOption.STOP.getOptionValue()) {
            System.out.println(AudioOption.PAUSE.getOptionValue() + " -> Pause");
            System.out.println(AudioOption.STOP.getOptionValue() + " -> Stop & Exit");
            System.out.println(AudioOption.RESUME.getOptionValue() + " -> Resume");

            if (!this.keyboard.hasNextInt()) {
                System.out.println("You didn't enter an integer!");
                continue;
            }

            selectedChoice = this.keyboard.nextInt();

            Runnable functionToExecute = choicesToFunction.get(selectedChoice);
            if (functionToExecute == null) {
                System.out.println("You entered an invalid number!");
                continue;
            }

            functionToExecute.run();
        }
    }

    // Method to play the audio
    private void play() {
        // start the clip
        this.clip.start();

        this.status = PlayStatus.PLAY;
    }

    private void pause() {
        if (this.status == PlayStatus.PAUSED) {
            System.out.println("audio is already paused");
            return;
        }

        this.currentFrame = this.clip.getMicrosecondPosition();
        this.clip.stop();
        this.status = PlayStatus.PAUSED;
    }

    // Method to stop the audio
    private void stop() {
        this.currentFrame = 0L;
        this.clip.stop();
        this.clip.close();
    }

    private void resumeAudio() {
        if (this.status == PlayStatus.PLAY) {
            System.out.println("Audio is already being played");
            return;
        }

        this.clip.close();
        this.resetAudioStream();

        this.clip.setMicrosecondPosition(this.currentFrame);
        this.play();
    }

    // Method to reset audio stream
    private void resetAudioStream() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File(this.audioFilePath).getAbsoluteFile()
            );
            this.clip.open(audioInputStream);
            this.clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
