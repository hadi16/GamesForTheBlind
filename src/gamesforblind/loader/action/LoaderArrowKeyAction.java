package gamesforblind.loader.action;

import gamesforblind.enums.ArrowKeyDirection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user presses an arrow key (up/down/left/right) while in the loader screen.
 */
@XmlRootElement(name = "LoaderArrowKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderArrowKeyAction extends LoaderAction {
    /**
     * Signifies which arrow key was pressed in the loader (up/down/left/right).
     */
    @XmlElement
    private final ArrowKeyDirection arrowKeyDirection;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private LoaderArrowKeyAction() {
        this(null);
    }

    /**
     * Creates a new LoaderArrowKeyAction
     *
     * @param arrowKeyDirection Which arrow key was pressed in the loader.
     */
    public LoaderArrowKeyAction(ArrowKeyDirection arrowKeyDirection) {
        this.arrowKeyDirection = arrowKeyDirection;
    }

    /**
     * Getter for the arrowKeyDirection instance variable.
     *
     * @return Which arrow key was pressed in the loader (up/down/left/right).
     */
    public ArrowKeyDirection getArrowKeyDirection() {
        return this.arrowKeyDirection;
    }
}
