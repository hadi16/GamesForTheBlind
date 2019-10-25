import sudoku.SudokuGame;
import synthesizer.AudioPlayer;
import synthesizer.GoogleCloudRetriever;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameLoader {
    private static boolean BUILD_PHRASES = true;

    private GameLoader() {
        final Scanner keyboard = new Scanner(System.in);

        AudioPlayer audioPlayer = null;
        try {
            audioPlayer = new AudioPlayer();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Thread audioPlayerThread = new Thread(audioPlayer);
        audioPlayerThread.start();

        new SudokuGame(this.getRequestedSudokuBoardSize(keyboard), audioPlayer);

        keyboard.close();
    }

    public static void main(String[] args) {
        if (BUILD_PHRASES) {
            GoogleCloudRetriever.createPhraseAudioFiles();
        }

        new GameLoader();
    }

    private int getRequestedSudokuBoardSize(Scanner keyboard) {
        ArrayList<Integer> SUPPORTED_SUDOKU_SIZES = new ArrayList<>(Arrays.asList(4, 9));

        int selectedBoardSize = -1;
        while (!SUPPORTED_SUDOKU_SIZES.contains(selectedBoardSize)) {
            System.out.println("What size Sudoku board would you like?");

            System.out.print("Supported values:");
            for (int boardSize : SUPPORTED_SUDOKU_SIZES) {
                System.out.print(" " + boardSize);
            }
            System.out.println();

            if (keyboard.hasNextInt()) {
                selectedBoardSize = keyboard.nextInt();
            } else {
                keyboard.next();
            }
        }
        return selectedBoardSize;
    }
}
