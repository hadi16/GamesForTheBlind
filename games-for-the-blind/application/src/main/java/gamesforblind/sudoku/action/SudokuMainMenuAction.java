package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user wants to go back to the main menu (the game loader).
 */
@XmlRootElement(name = "SudokuMainMenuAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuMainMenuAction extends SudokuAction {
}
