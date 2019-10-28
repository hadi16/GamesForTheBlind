package gamesforblind.sudoku.adapter;

import gamesforblind.sudoku.enums.InputType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;

@XmlRootElement
public class AdaptedSudokuHighlightAction {
    @XmlElement
    private Point pointToHighlight;

    @XmlElement
    private InputType inputType;

    public Point getPointToHighlight() {
        return this.pointToHighlight;
    }

    public void setPointToHighlight(Point pointToHighlight) {
        this.pointToHighlight = pointToHighlight;
    }

    public InputType getInputType() {
        return this.inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }
}
