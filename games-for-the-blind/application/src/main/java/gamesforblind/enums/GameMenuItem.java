package gamesforblind.enums;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An enumeration for all of the {@link JMenuItem}s in the Sudoku or Codebreaker menu.
 */
public enum GameMenuItem {
    HINT,
    INSTRUCTIONS,
    LANGUAGE,
    RESTART,
    RETURN_TO_MAIN_MENU;

    public static final GameMenuItem[] SUDOKU_MENU_ITEMS = new GameMenuItem[]{
            HINT, INSTRUCTIONS, LANGUAGE, RESTART, RETURN_TO_MAIN_MENU
    };

    public static final GameMenuItem[] CODEBREAKER_MENU_ITEMS = new GameMenuItem[]{
            INSTRUCTIONS, LANGUAGE, RESTART, RETURN_TO_MAIN_MENU
    };

    /**
     * Overrides the String representation of each enumeration member, which is used in the Sudoku menu GUI.
     * Replaces the underscores in the name w/ spaces and capitalizes each word in the name
     * (e.g. RETURN_TO_MAIN_MENU --> "Return To Main Menu").
     *
     * @return A more user-friendly String representation of the current enumeration member.
     */
    @Override
    public String toString() {
        List<String> lowercaseWordList = Arrays.asList(this.name().toLowerCase().split("_"));
        List<String> capitalizedWordList = lowercaseWordList.stream().map(
                word -> word.substring(0, 1).toUpperCase() + word.substring(1)
        ).collect(Collectors.toList());
        return String.join(" ", capitalizedWordList);
    }
}
