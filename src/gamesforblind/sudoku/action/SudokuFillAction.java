package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Fill action class used for inputting numbers into the proper cells
 */
@XmlRootElement(name = "SudokuFillAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuFillAction extends SudokuAction {
    @XmlElement
    private final int numberToFill;

    @SuppressWarnings("unused")
    private SudokuFillAction() {
        this(0);
    }

    public SudokuFillAction(int numberToFill) {
        this.numberToFill = numberToFill;
    }

    public int getNumberToFill() {
        return this.numberToFill;
    }
}
