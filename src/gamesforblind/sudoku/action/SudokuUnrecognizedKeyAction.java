package gamesforblind.sudoku.action;

import gamesforblind.sudoku.adapter.SudokuUnrecognizedKeyActionAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlRootElement
public class SudokuUnrecognizedKeyAction extends SudokuAction {
    private final int keyCode;

    public SudokuUnrecognizedKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    @Override
    public XmlAdapter getJaxbAdapter() {
        return new SudokuUnrecognizedKeyActionAdapter();
    }
}
