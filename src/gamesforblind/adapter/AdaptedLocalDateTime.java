package gamesforblind.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * An adapted {@link LocalDateTime} to have a zero-argument constructor to work with JAXB (XML serializer for logging).
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedLocalDateTime {
    /**
     * Maps to the date field in {@link LocalDateTime}. Uses the adapter {@link LocalDateAdapter},
     * since {@link LocalDate} doesn't have a zero-argument constructor required by JAXB.
     */
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;

    /**
     * Maps to the time field in {@link LocalDateTime}. Uses the adapter {@link LocalTimeAdapter},
     * since {@link LocalTime} doesn't have a zero-argument constructor required by JAXB.
     */
    @XmlElement
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime time;

    /**
     * Getter for date
     *
     * @return The date as a {@link LocalDate}
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Setter for date
     *
     * @param date The date to set (a {@link LocalDate}).
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Getter for time
     *
     * @return The time as a {@link LocalTime}.
     */
    public LocalTime getTime() {
        return this.time;
    }

    /**
     * Setter for time
     *
     * @param time The time to set (a {@link LocalTime}).
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }
}
