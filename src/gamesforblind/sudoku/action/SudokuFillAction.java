package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user wishes to input a number into the currently selected board square.
 */
@XmlRootElement(name = "SudokuFillAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuFillAction extends SudokuAction {
    /**
     * The number that the user wants to input into the selected Sudoku square.
     */
    @XmlElement
    private final int numberToFill;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private SudokuFillAction() {
        this(0);
    }

    /**
     * Creates a new SudokuFillAction.
     * @param numberToFill The number that the user wants to input into the selected Sudoku square.
     */
    public SudokuFillAction(int numberToFill) {
        this.numberToFill = numberToFill;
    }

    /**
     * Getter for numberToFill
     * @return The number that the user wants to input into the selected Sudoku square.
     */
    public int getNumberToFill() {
        return this.numberToFill;
    }
}
