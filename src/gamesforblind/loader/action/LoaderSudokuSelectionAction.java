package gamesforblind.loader.action;

import gamesforblind.enums.SudokuType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user makes a selection between a 4x4 and 9x9 Sudoku game.
 */
@XmlRootElement(name = "LoaderSudokuSelectionAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderSudokuSelectionAction extends LoaderAction {
    /**
     * Which Sudoku game the user has selected (e.g. 4x4 or 9x9).
     */
    @XmlElement
    private final SudokuType sudokuType;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private LoaderSudokuSelectionAction() {
        this(null);
    }

    /**
     * Creates a new LoaderSudokuSelectionAction
     *
     * @param sudokuType The type of Sudoku game that the user has selected (e.g. 4x4 or 9x9).
     */
    public LoaderSudokuSelectionAction(SudokuType sudokuType) {
        this.sudokuType = sudokuType;
    }

    /**
     * Getter for the sudokuType instance variable.
     *
     * @return Which Sudoku game the user has selected (e.g. 4x4 or 9x9).
     */
    public SudokuType getSudokuType() {
        return this.sudokuType;
    }
}
