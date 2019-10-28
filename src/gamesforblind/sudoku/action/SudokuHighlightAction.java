package gamesforblind.sudoku.action;

import gamesforblind.sudoku.adapter.SudokuHighlightActionAdapter;
import gamesforblind.sudoku.enums.InputType;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.*;

/**
 * Highlight class used for highlighting the cells and boxes upon keyboard and mouse navigation
 */
@XmlRootElement
public class SudokuHighlightAction extends SudokuAction {
    private final Point pointToHighlight;
    private final InputType inputType;

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

    @Override
    public XmlAdapter getJaxbAdapter() {
        return new SudokuHighlightActionAdapter();
    }
}
