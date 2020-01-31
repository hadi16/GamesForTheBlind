package gamesforblind.codebreaker.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CodebreakerSetSingleNumberAction")
@XmlAccessorType(XmlAccessType.NONE)
public class CodebreakerSetSingleNumberAction extends CodebreakerAction {
    @XmlElement
    private final int numberToSet;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings("unused")
    private CodebreakerSetSingleNumberAction() {
        this(0);
    }

    public CodebreakerSetSingleNumberAction(int numberToSet) {
        this.numberToSet = numberToSet;
    }

    public int getNumberToSet() {
        return this.numberToSet;
    }
}
