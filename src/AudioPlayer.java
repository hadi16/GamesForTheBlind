import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class AudioPlayer {
    private final String audioFilePath;
    private Long currentFrame;
    private Clip clip;
    private PlayStatus status; // current status of clip

    private AudioPlayer(String audioFilePath) {
        try {
            this.clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

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

        System.out.println("Enter a Letter:");
        System.out.println("h -> helloWorld.wav");
        System.out.println("m -> testWav.wav");

        String selectedChar = "";
        while (!selectedChar.equals("h") && !selectedChar.equals("m")) {
            selectedChar = keyboard.nextLine();
        }

        String audioFilePath = selectedChar.equalsIgnoreCase("h") ? "audio/helloWorld.wav" : "audio/testWave.wav";
        new AudioPlayer(audioFilePath).runAudioPlayerPrompt();
    }

    private void runAudioPlayerPrompt() {
        final Map<Integer, Runnable> choicesToFunction = Map.of(
                1, this::pause,
                2, this::stop,
                3, this::resumeAudio
        );

        this.play();

        Scanner scanner = new Scanner(System.in);

        int selectedChoice = 0;
        while (selectedChoice != 2) {
            System.out.println("1 -> Pause");
            System.out.println("2 -> Exit");
            System.out.println("3 -> Resume");

            if (!scanner.hasNextInt()) {
                System.out.println("You didn't enter an integer!");
                continue;
            }

            selectedChoice = scanner.nextInt();

            Runnable functionToExecute = choicesToFunction.get(selectedChoice);
            if (functionToExecute == null) {
                System.out.println("You entered an invalid number!");
                continue;
            }

            functionToExecute.run();
        }
        scanner.close();
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
