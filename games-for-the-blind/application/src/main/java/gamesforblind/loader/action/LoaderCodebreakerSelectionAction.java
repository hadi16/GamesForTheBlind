package gamesforblind.loader.action;

import com.sun.istack.NotNull;
import gamesforblind.enums.CodebreakerType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Action for when the user selects "PLAY CODEBREAKER"
 */
@XmlRootElement(name = "LoaderMCodebreakerSelectionAction")
@XmlAccessorType(XmlAccessType.NONE)
public class LoaderCodebreakerSelectionAction extends LoaderAction {

    /**
     * Which Codebreaker game the user has selected (e.g. 4x4 or 9x9).
     */
    @XmlElement
    private final CodebreakerType codebreakerType;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings({"unused"})
    private LoaderCodebreakerSelectionAction() {
        this(null);
    }

    /**
     * Creates a new LoaderCodebreakerSelectionAction
     *
     * @param codebreakerType The type of Codebreaker game that the user has selected (e.g. 4,5,6).
     */
    public LoaderCodebreakerSelectionAction(@NotNull CodebreakerType codebreakerType) {
        this.codebreakerType = codebreakerType;
    }

    /**
     * Getter for the codebreakerType instance variable.
     *
     * @return Which Codebreaker game the user has selected (e.g. 4,5,6).
     */
    public CodebreakerType getCodebreakerType() {
        return this.codebreakerType;
    }
}
