package gamesforblind;

import gamesforblind.adapter.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * The abstract class that all actions in the loader/games inherit from. Contains the
 * exact date & time in which the action took place, which allows for playback in realtime.
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ProgramAction {
    /**
     * The {@link LocalDateTime} when the action took place. For JAXB (XML serializer)
     * to work properly, the custom {@link LocalDateTimeAdapter} is used.
     */
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    protected final LocalDateTime localDateTime;

    /**
     * Creates a new {@link ProgramAction}.
     * Note: this is never called directly, since this class is marked as abstract.
     */
    public ProgramAction() {
        this.localDateTime = LocalDateTime.now();
    }

    /**
     * Getter for localDateTime
     *
     * @return The {@link LocalDateTime} when this particular action in the game took place.
     */
    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }
}
