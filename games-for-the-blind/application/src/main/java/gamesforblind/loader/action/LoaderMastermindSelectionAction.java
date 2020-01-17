package gamesforblind.loader.action;

import com.sun.istack.NotNull;
import gamesforblind.enums.MastermindType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user selects "PLAY MASTERMIND"
 */
@XmlRootElement(name = "LoaderMastermindSelectionAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderMastermindSelectionAction extends LoaderAction {

    /**
     * Which Mastermind game the user has selected (e.g. 4x4 or 9x9).
     */
    @XmlElement
    private final MastermindType mastermindType;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings({"unused", "ConstantConditions"})
    private LoaderMastermindSelectionAction() {
        this(null);
    }

    /**
     * Creates a new LoaderMastermindSelectionAction
     *
     * @param mastermindType The type of Mastermind game that the user has selected (e.g. 4,5,6).
     */
    public LoaderMastermindSelectionAction(@NotNull MastermindType mastermindType) {
        this.mastermindType = mastermindType;
    }

    /**
     * Getter for the mastermindType instance variable.
     *
     * @return Which Mastermind game the user has selected (e.g. 4,5,6).
     */
    public MastermindType getMastermindType() {
        return this.mastermindType;
    }
}