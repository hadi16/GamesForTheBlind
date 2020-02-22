package gamesforblind.codebreaker.action;

import gamesforblind.adapter.PointAdapter;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlRootElement(name = "CodebreakerMouseAction")
@XmlAccessorType(XmlAccessType.NONE)
public class CodebreakerMouseAction extends CodebreakerAction {
    @XmlElement
    @XmlJavaTypeAdapter(PointAdapter.class)
    private final Point selectedPoint;

    /**
     * Warning: DO NOT call this constructor directly.
     * Needed to allow JAXB (XML serializer) to work, since it needs a zero-argument constructor.
     */
    @SuppressWarnings({"unused"})
    private CodebreakerMouseAction() {
        this(null);
    }

    public CodebreakerMouseAction(@Nullable Point selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public Point getSelectedPoint() {
        return this.selectedPoint;
    }
}
