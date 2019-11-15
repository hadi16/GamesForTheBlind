package gamesforblind.sudoku.action;

import gamesforblind.enums.SudokuMenuItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action that is sent when a user wishes to use the menu.
 */
@XmlRootElement(name = "SudokuMenuAction")
@XmlAccessorType(XmlAccessType.NONE)
public class SudokuMenuAction extends SudokuAction {
    @XmlElement
    private final SudokuMenuItem sudokuMenuItem;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private SudokuMenuAction() {
        this(null);
    }

    /**
     * Creates a new SudokuMenuAction.
     *
     * @return
     */
    public SudokuMenuAction(SudokuMenuItem sudokuMenuItem) {
        this.sudokuMenuItem = sudokuMenuItem;
    }

    public SudokuMenuItem getSudokuMenuItem() {
        return this.sudokuMenuItem;
    }
}
