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
 * Highlight class used for highlighting the cells and boxes upon keyboard and mouse navigation
 */
@XmlRootElement(name = "SudokuHighlightAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuHighlightAction extends SudokuAction {
    @XmlElement
    @XmlJavaTypeAdapter(PointAdapter.class)
    private final Point pointToHighlight;

    @XmlElement
    private final InputType inputType;

    @SuppressWarnings("unused")
    private SudokuHighlightAction() {
        this(null, null);
    }

    public SudokuHighlightAction(Point pointToHighlight, InputType inputType) {
        this.pointToHighlight = pointToHighlight;
        this.inputType = inputType;
    }

    public Point getPointToHighlight() {
        return this.pointToHighlight;
    }

    public InputType getInputType() {
        return this.inputType;
    }
}
