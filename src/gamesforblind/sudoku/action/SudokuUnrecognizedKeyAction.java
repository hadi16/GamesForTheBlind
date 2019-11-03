package gamesforblind.sudoku.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.event.KeyEvent;

/**
 * Action that is sent when the user presses a key that is registered as unrecognized by the game.
 */
@XmlRootElement(name = "SudokuUnrecognizedKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuUnrecognizedKeyAction extends SudokuAction {
    /**
     * Key code of unrecognized key that was pressed. Can check for equality against the constants in {@link KeyEvent}
     */
    @XmlElement
    private final int keyCode;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private SudokuUnrecognizedKeyAction() {
        this(0);
    }

    /**
     * Creates a new SudokuUnrecognizedKeyAction
     *
     * @param keyCode Key code of unrecognized key that was pressed (check against constants in {@link KeyEvent}).
     */
    public SudokuUnrecognizedKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Getter for keyCode
     *
     * @return Key code of unrecognized key that was pressed. Check for equality against constants in {@link KeyEvent}.
     */
    public int getKeyCode() {
        return this.keyCode;
    }
}
