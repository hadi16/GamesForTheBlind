package gamesforblind.sudoku.adapter;

import gamesforblind.sudoku.enums.SudokuSection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdaptedSudokuReadPositionAction {
    @XmlElement
    private SudokuSection sudokuSection;

    public SudokuSection getSudokuSection() {
        return this.sudokuSection;
    }

    public void setSudokuSection(SudokuSection sudokuSection) {
        this.sudokuSection = sudokuSection;
    }
}
