package gamesforblind.loader.action;

import gamesforblind.enums.SelectedGame;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user makes a menu selection in the loader, either with the keyboard or mouse.
 */
@XmlRootElement(name = "LoaderGameSelectionAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderGameSelectionAction extends LoaderAction {
    /**
     * Signifies which game the user has selected (e.g. Sudoku).
     * If the back button is selected, this is set to SelectedGame.NONE
     */
    @XmlElement
    private final SelectedGame selectedGame;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings({"unused", "ConstantConditions"})
    private LoaderGameSelectionAction() {
        this(null);
    }

    /**
     * Creates a new LoaderGameSelectionAction
     *
     * @param selectedGame Which game the user has selected. If the user wishes to go back, this is NONE.
     */
    public LoaderGameSelectionAction(@NotNull SelectedGame selectedGame) {
        this.selectedGame = selectedGame;
    }

    /**
     * Getter for the selectedGame instance variable.
     *
     * @return Which game the user has selected. If the user wishes to go back, this is NONE.
     */
    public SelectedGame getSelectedGame() {
        return this.selectedGame;
    }
}
