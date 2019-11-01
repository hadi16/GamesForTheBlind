package gamesforblind.sudoku.action;

import gamesforblind.enums.SudokuSection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SudokuReadPositionAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuReadPositionAction extends SudokuAction {
    @XmlElement
    private final SudokuSection sudokuSection;

    @SuppressWarnings("unused")
    private SudokuReadPositionAction() {
        this(null);
    }

    public SudokuReadPositionAction(SudokuSection sudokuSection) {
        this.sudokuSection = sudokuSection;
    }

    public SudokuSection getSudokuSection() {
        return this.sudokuSection;
    }
}
