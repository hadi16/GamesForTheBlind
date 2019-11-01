package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SudokuHintKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuHintKeyAction extends SudokuAction {
    @XmlElement
    private final int keyCode;

    @SuppressWarnings("unused")
    public SudokuHintKeyAction() {
        this(0);
    }

    public SudokuHintKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}