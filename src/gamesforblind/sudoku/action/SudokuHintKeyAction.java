package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user requests that the currently highlighted square be filled in for them (a "hint").
 */
@XmlRootElement(name = "SudokuHintKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuHintKeyAction extends SudokuAction {
}
