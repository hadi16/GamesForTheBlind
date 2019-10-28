package gamesforblind.sudoku.adapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdaptedSudokuFillAction {
    @XmlElement
    private int numberToFill;

    public int getNumberToFill() {
        return this.numberToFill;
    }

    public void setNumberToFill(int numberToFill) {
        this.numberToFill = numberToFill;
    }
}
