package gamesforblind.sudoku.adapter;

import gamesforblind.sudoku.action.SudokuReadPositionAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SudokuReadPositionActionAdapter
        extends XmlAdapter<AdaptedSudokuReadPositionAction, SudokuReadPositionAction> {
    @Override
    public SudokuReadPositionAction unmarshal(AdaptedSudokuReadPositionAction adapted) {
        return new SudokuReadPositionAction(adapted.getSudokuSection());
    }

    @Override
    public AdaptedSudokuReadPositionAction marshal(SudokuReadPositionAction action) {
        AdaptedSudokuReadPositionAction adapted = new AdaptedSudokuReadPositionAction();
        adapted.setSudokuSection(action.getSudokuSection());
        return adapted;
    }
}
