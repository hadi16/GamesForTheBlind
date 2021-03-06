package gamesforblind.sudoku.action;

import gamesforblind.enums.SudokuSection;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when the user requests that the values of the current row, column, or block are read.
 */
@XmlRootElement(name = "SudokuReadPositionAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuReadPositionAction extends SudokuAction {
    /**
     * Whether the user wants the ROW, COLUMN, or BLOCK to be read.
     */
    @XmlElement
    private final SudokuSection sudokuSection;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings({"unused", "ConstantConditions"})
    private SudokuReadPositionAction() {
        this(null);
    }

    /**
     * Creates a new SudokuReadPositionAction
     *
     * @param sudokuSection Whether the user wants the ROW, COLUMN, or BLOCK to be read.
     */
    public SudokuReadPositionAction(@NotNull SudokuSection sudokuSection) {
        this.sudokuSection = sudokuSection;
    }

    /**
     * Getter for sudokuSection
     *
     * @return Whether the user wants the ROW, COLUMN, or BLOCK to be read.
     */
    public SudokuSection getSudokuSection() {
        return this.sudokuSection;
    }
}
