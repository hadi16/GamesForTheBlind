package gamesforblind.sudoku.action;

import gamesforblind.enums.ArrowKeyDirection;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user triggers a hot key in the game.
 */
@XmlRootElement(name = "SudokuHotKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuHotKeyAction extends SudokuAction {
    /**
     * The {@link ArrowKeyDirection} that was pressed with this hot key (e.g. left arrow key).
     */
    @XmlElement
    private final ArrowKeyDirection arrowKeyDirection;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings({"unused", "ConstantConditions"})
    private SudokuHotKeyAction() {
        this(null);
    }

    /**
     * Creates a new SudokuHotKeyAction.
     *
     * @param arrowKeyDirection The {@link ArrowKeyDirection} that was pressed with this hot key (e.g. left arrow key).
     */
    public SudokuHotKeyAction(@NotNull ArrowKeyDirection arrowKeyDirection) {
        this.arrowKeyDirection = arrowKeyDirection;
    }

    /**
     * Getter for arrowKeyDirection
     *
     * @return The {@link ArrowKeyDirection} that was pressed with this hot key (e.g. left arrow key).
     */
    public ArrowKeyDirection getArrowKeyDirection() {
        return this.arrowKeyDirection;
    }
}
