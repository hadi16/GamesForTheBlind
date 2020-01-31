package gamesforblind.codebreaker.action;

import gamesforblind.enums.ArrowKeyDirection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CodebreakerArrowKeyAction")
@XmlAccessorType(XmlAccessType.NONE)
public class CodebreakerArrowKeyAction extends CodebreakerAction {
    @XmlElement
    private final ArrowKeyDirection arrowKeyDirection;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private CodebreakerArrowKeyAction() {
        this(null);
    }

    public CodebreakerArrowKeyAction(ArrowKeyDirection arrowKeyDirection) {
        this.arrowKeyDirection = arrowKeyDirection;
    }

    public ArrowKeyDirection getArrowKeyDirection() {
        return this.arrowKeyDirection;
    }
}
