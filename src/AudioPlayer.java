import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class AudioPlayer {
    private final String audioFilePath;
    private final Clip clip;

    private Long currentFrame;
    private PlayStatus status; // current status of clip

    private AudioPlayer(String audioFilePath) throws LineUnavailableException {
        this.clip = AudioSystem.getClip();
        this.audioFilePath = audioFilePath;
        this.resetAudioStream();
    }

    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        String selectedChar = "";
        while (!selectedChar.equals("h") && !selectedChar.equals("m")) {
            System.out.println("Enter a Letter:");
            System.out.println("h -> helloWorld.wav");
            System.out.println("m -> testWav.wav");

            selectedChar = keyboard.nextLine();
        }

        keyboard.close();

        final String AUDIO_DIRECTORY = "audio/";
        final String audioFilePath;
        if (selectedChar.equalsIgnoreCase("h")) {
            audioFilePath = AUDIO_DIRECTORY + "helloWorld.wav";
        } else {
            audioFilePath = AUDIO_DIRECTORY + "testWave.wav";
        }

        try {
            AudioPlayer audioPlayer = new AudioPlayer(audioFilePath);
            audioPlayer.runAudioPlayerPrompt();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void runAudioPlayerPrompt() {
        final Map<Integer, Runnable> choicesToFunction = Map.of(
                AudioOption.PAUSE.getOptionValue(), this::pause,
                AudioOption.STOP.getOptionValue(), this::stop,
                AudioOption.RESUME.getOptionValue(), this::resumeAudio
        );

        this.play();

        Scanner keyboard = new Scanner(System.in);

        int selectedChoice = 0;
        while (selectedChoice != AudioOption.STOP.getOptionValue()) {
            System.out.println(AudioOption.PAUSE.getOptionValue() + " -> Pause");
            System.out.println(AudioOption.STOP.getOptionValue() + " -> Stop & Exit");
            System.out.println(AudioOption.RESUME.getOptionValue() + " -> Resume");

            if (!keyboard.hasNextInt()) {
                System.out.println("You didn't enter an integer!");
                continue;
            }

            selectedChoice = keyboard.nextInt();

            Runnable functionToExecute = choicesToFunction.get(selectedChoice);
            if (functionToExecute == null) {
                System.out.println("You entered an invalid number!");
                continue;
            }

            functionToExecute.run();
        }

        keyboard.close();
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
