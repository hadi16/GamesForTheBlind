package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user requests that the instructions for Sudoku are read.
 */
@XmlRootElement(name = "SudokuInstructionsAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuInstructionsAction extends SudokuAction {
}
