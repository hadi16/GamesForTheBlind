package gamesforblind;

import gamesforblind.adapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class ProgramAction {
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    protected final LocalDateTime localDateTime;

    public ProgramAction() {
        this.localDateTime = LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }
}
