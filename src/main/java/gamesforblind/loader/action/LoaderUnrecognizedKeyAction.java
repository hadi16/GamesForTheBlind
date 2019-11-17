package gamesforblind.loader.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user presses an unrecognized key while in the loader GUI.
 */
@XmlRootElement(name = "LoaderUnrecognizedKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderUnrecognizedKeyAction extends LoaderAction {
    /**
     * The key code of the unrecognized key that was selected.
     */
    @XmlElement
    private final int keyCode;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private LoaderUnrecognizedKeyAction() {
        this(0);
    }

    /**
     * Creates a new LoaderUnrecognizedKeyAction
     *
     * @param keyCode The key code of the unrecognized key that was selected.
     */
    public LoaderUnrecognizedKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Getter for the keyCode instance variable.
     *
     * @return The key code of the unrecognized key that was selected.
     */
    public int getKeyCode() {
        return this.keyCode;
    }
}
