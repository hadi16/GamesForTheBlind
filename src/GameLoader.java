import audio_player.AudioPlayer;
import sudoku.gui.SudokuBoard;

import javax.sound.sampled.LineUnavailableException;
import java.util.Map;
import java.util.Scanner;

public class GameLoader {
    public static void main(String[] args) {
        final Map<String, Runnable> optionCharToGame = Map.of(
                "S", SudokuBoard::new,
                "A", () -> {
                    try {
                        new AudioPlayer();
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                }
        );

        Scanner keyboard = new Scanner(System.in);

        Runnable gameToExecute = null;
        while (gameToExecute == null) {
            System.out.println("Press 'S' to launch Sudoku or 'A' to launch AudioPlayer.");
            gameToExecute = optionCharToGame.get(keyboard.nextLine());
        }

        keyboard.close();

        gameToExecute.run();
    }
}
