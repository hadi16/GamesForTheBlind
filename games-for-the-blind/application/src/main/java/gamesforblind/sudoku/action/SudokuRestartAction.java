package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user wants to restart the current Sudoku board.
 */
@XmlRootElement(name = "SudokuRestartAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuRestartAction extends SudokuAction {
}
