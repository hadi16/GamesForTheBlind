package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.event.KeyEvent;

/**
 * Action that is sent when the user presses a key that is registered as unrecognized by the game.
 */
@XmlRootElement(name = "SudokuReadCurrentSquareAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuReadCurrentSquareAction extends SudokuAction {
    /**
     * The number that the user wants to input into the selected Sudoku square.
     */
    @XmlElement
    private final int action;

    @SuppressWarnings("unused")
    private SudokuReadCurrentSquareAction() {this(1);

    }
    /**
     * Creates a new SudokuReadCurrentSquareAction.
     *
     * @param action
     */
    public SudokuReadCurrentSquareAction(int action) {
        this.action = action;
    }

    /**
     * Getter for action
     *
     * @return The number of the action
     */
    public int getAction() {
        return this.action;

    }

}
