package gamesforblind.sudoku.action;

import gamesforblind.sudoku.adapter.SudokuFillActionAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Fill action class used for inputting numbers into the proper cells
 */
@XmlRootElement
public class SudokuFillAction extends SudokuAction {
    private final int numberToFill;

    public SudokuFillAction(int numberToFill) {
        this.numberToFill = numberToFill;
    }

    public int getNumberToFill() {
        return this.numberToFill;
    }

    @Override
    public XmlAdapter getJaxbAdapter() {
        return new SudokuFillActionAdapter();
    }
}
