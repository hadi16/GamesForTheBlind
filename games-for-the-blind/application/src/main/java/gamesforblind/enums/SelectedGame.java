package gamesforblind.enums;

/**
 * Enumeration for the different games that can be selected in the loader GUI.
 * NONE is needed for the instances when the user presses the BACK button in the GUI.
 * MASTERMIND isn't needed because it doesn't have an options pane (instead, just directly load Mastermind).
 */
public enum SelectedGame {
    NONE, SUDOKU, MASTERMIND
}
