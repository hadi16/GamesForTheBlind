package gamesforblind.loader.adapter;

import gamesforblind.loader.enums.SudokuType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class AdaptedLoaderSudokuSelectionAction {
    @XmlElement
    private SudokuType sudokuType;

    public SudokuType getSudokuType() {
        return this.sudokuType;
    }

    public void setSudokuType(SudokuType sudokuType) {
        this.sudokuType = sudokuType;
    }
}
