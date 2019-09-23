import audio_player.AudioPlayer;
import sudoku.gui.SudokuBoard;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class GameLoader {
    private GameLoader() {
        this.runGameLoader();
    }

    public static void main(String[] args) {
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

    private void runGameLoader() {
        final Scanner keyboard = new Scanner(System.in);
        final Map<String, Runnable> optionCharToGame = Map.of(
                "S", () -> new SudokuBoard(this.getRequestedSudokuBoardSize(keyboard)),
                "A", () -> {
                    try {
                        new AudioPlayer(keyboard);
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                }
        );

        Runnable gameToExecute = null;
        while (gameToExecute == null) {
            System.out.println("Press 'S' to launch Sudoku or 'A' to launch AudioPlayer.");
            gameToExecute = optionCharToGame.get(keyboard.nextLine());
        }

        gameToExecute.run();

        keyboard.close();
    }
}
