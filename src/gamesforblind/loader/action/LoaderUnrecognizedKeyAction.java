package gamesforblind.loader.action;

import gamesforblind.loader.adapter.LoaderUnrecognizedKeyActionAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Action for when the user presses an unrecognized key while in the loader GUI.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderUnrecognizedKeyAction extends LoaderAction {
    /**
     * The key code of the unrecognized key that was selected.
     */
    @XmlElement
    private final int keyCode;

    private LoaderUnrecognizedKeyAction() {
        this(0);
    }

    /**
     * Creates a new {@link LoaderUnrecognizedKeyAction}
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

    @Override
    public XmlAdapter getJaxbAdapter() {
        return new LoaderUnrecognizedKeyActionAdapter();
    }
}
