package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user wishes to exit the current Sudoku game.
 * Triggered by closing the window using the GUI button or the keystroke COMMAND + Q.
 */
@XmlRootElement(name = "SudokuExitAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuExitAction extends SudokuAction {
}
