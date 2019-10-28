package gamesforblind.sudoku.adapter;

import gamesforblind.sudoku.action.SudokuUnrecognizedKeyAction;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SudokuUnrecognizedKeyActionAdapter
        extends XmlAdapter<AdaptedSudokuUnrecognizedKeyAction, SudokuUnrecognizedKeyAction> {
    @Override
    public SudokuUnrecognizedKeyAction unmarshal(AdaptedSudokuUnrecognizedKeyAction adapted) {
        return new SudokuUnrecognizedKeyAction(adapted.getKeyCode());
    }

    @Override
    public AdaptedSudokuUnrecognizedKeyAction marshal(SudokuUnrecognizedKeyAction action) {
        AdaptedSudokuUnrecognizedKeyAction adapted = new AdaptedSudokuUnrecognizedKeyAction();
        adapted.setKeyCode(action.getKeyCode());
        return adapted;
    }
}
