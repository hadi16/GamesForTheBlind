package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user wishes to input a number into the currently selected board square.
 */
@XmlRootElement(name = "SudokuMenuAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuMenuAction extends SudokuAction {
    /**
     * The number that the user wants to input into the selected Sudoku square.
     */
    @XmlElement
    private final int action;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private SudokuMenuAction() {this(1); }

    /**
     * Creates a new SudokuFillAction.
     *
     * @param action
     */
    public SudokuMenuAction(int action) {
        this.action = action;
    }

    /**
     * Getter for action
     *
     * @return The number that the user wants
     */
    public int getAction() {
        return this.action;

    }
}
