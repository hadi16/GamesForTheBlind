package gamesforblind.sudoku.adapter;

import gamesforblind.sudoku.action.SudokuHighlightAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SudokuHighlightActionAdapter
        extends XmlAdapter<AdaptedSudokuHighlightAction, SudokuHighlightAction> {
    @Override
    public SudokuHighlightAction unmarshal(AdaptedSudokuHighlightAction adapted) {
        return new SudokuHighlightAction(adapted.getPointToHighlight(), adapted.getInputType());
    }

    @Override
    public AdaptedSudokuHighlightAction marshal(SudokuHighlightAction action) {
        AdaptedSudokuHighlightAction adapted = new AdaptedSudokuHighlightAction();
        adapted.setInputType(action.getInputType());
        adapted.setPointToHighlight(action.getPointToHighlight());
        return adapted;
    }
}
