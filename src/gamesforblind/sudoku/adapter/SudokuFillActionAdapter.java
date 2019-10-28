package gamesforblind.sudoku.adapter;

import gamesforblind.sudoku.action.SudokuFillAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SudokuFillActionAdapter
        extends XmlAdapter<AdaptedSudokuFillAction, SudokuFillAction> {
    @Override
    public SudokuFillAction unmarshal(AdaptedSudokuFillAction adapted) {
        return new SudokuFillAction(adapted.getNumberToFill());
    }

    @Override
    public AdaptedSudokuFillAction marshal(SudokuFillAction action) {
        AdaptedSudokuFillAction adapted = new AdaptedSudokuFillAction();
        adapted.setNumberToFill(action.getNumberToFill());
        return adapted;
    }
}
