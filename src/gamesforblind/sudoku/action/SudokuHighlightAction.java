package gamesforblind.sudoku.action;

import gamesforblind.adapter.PointAdapter;
import gamesforblind.enums.InputType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

/**
 * Action that is sent when a user wishes to highlight a point using either the keyboard or mouse.
 */
@XmlRootElement(name = "SudokuHighlightAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuHighlightAction extends SudokuAction {
    /**
     * The point that the user wishes to highlight.
     *
     * For MOUSE inputs, this is just the Point that the user wishes to select (e.g. (3, 2) for 4th column & 3rd row).
     *
     * For KEYBOARD inputs, this is the Point that the triggered key maps to, which differs between the interface
     * types in the game. For example, in the block selection interface, a "W" key in the 9x9 maps to (0, 0),
     * while a "V" key maps to (2, 2).
     */
    @XmlElement
    @XmlJavaTypeAdapter(PointAdapter.class)
    private final Point pointToHighlight;

    /** Whether the action was triggered by the mouse or keyboard. */
    @XmlElement
    private final InputType inputType;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private SudokuHighlightAction() {
        this(null, null);
    }

    /**
     * Creates a new SudokuHighlightAction
     * @param pointToHighlight The point that the user wishes to highlight.
     * @param inputType Whether the action was triggered by the mouse or keyboard.
     */
    public SudokuHighlightAction(Point pointToHighlight, InputType inputType) {
        this.pointToHighlight = pointToHighlight;
        this.inputType = inputType;
    }

    /**
     * Getter for pointToHighlight
     * @return The point that the user wishes to highlight.
     */
    public Point getPointToHighlight() {
        return this.pointToHighlight;
    }

    /**
     * Getter for inputType
     * @return Whether the action was triggered by the mouse or keyboard.
     */
    public InputType getInputType() {
        return this.inputType;
    }
}
