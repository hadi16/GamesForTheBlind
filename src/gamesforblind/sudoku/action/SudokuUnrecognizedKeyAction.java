package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SudokuUnrecognizedKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuUnrecognizedKeyAction extends SudokuAction {
    @XmlElement
    private final int keyCode;

    @SuppressWarnings("unused")
    private SudokuUnrecognizedKeyAction() {
        this(0);
    }

    public SudokuUnrecognizedKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
