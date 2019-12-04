package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user requests that the location of the square is read to them.
 */
@XmlRootElement(name = "SudokuLocationAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuLocationAction extends SudokuAction {
}
