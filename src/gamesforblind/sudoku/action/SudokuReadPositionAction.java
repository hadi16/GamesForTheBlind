package gamesforblind.sudoku.action;

import gamesforblind.sudoku.adapter.SudokuReadPositionActionAdapter;
import gamesforblind.sudoku.enums.SudokuSection;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlRootElement
public class SudokuReadPositionAction extends SudokuAction {
    private final SudokuSection sudokuSection;

    public SudokuReadPositionAction(SudokuSection sudokuSection) {
        this.sudokuSection = sudokuSection;
    }

    public SudokuSection getSudokuSection() {
        return this.sudokuSection;
    }

    @Override
    public XmlAdapter getJaxbAdapter() {
        return new SudokuReadPositionActionAdapter();
    }
}
