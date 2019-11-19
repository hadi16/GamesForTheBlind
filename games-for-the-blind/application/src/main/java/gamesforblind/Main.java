package gamesforblind;

import gamesforblind.loader.GameLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Class contains the sole entry point for the program.
 */
public class Main {
    /**
     * Sets the GUI attributes that are used throughout the games (e.g. increasing menu font size.
     */
    private static void setUIAttributes() {
        // Increase the font size for the game menus & make it bold.
        Font MENU_FONT = new Font("Arial", Font.BOLD, 36);
        UIManager.put("Menu.font", MENU_FONT);
        UIManager.put("MenuItem.font", MENU_FONT);

        // Allows "COMMAND" + "Q" to trigger the XML log save.
        System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
    }

    /**
     * The sole entry point into the program.
     *
     * @param args The command line arguments to the program.
     */
    public static void main(String[] args) {
        setUIAttributes();

        new GameLoader(new ProgramArgs(args));
    }
}
