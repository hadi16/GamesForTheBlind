package gamesforblind.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalTime;

/**
 * An adapted {@link LocalTime} to have a zero-argument constructor to work with JAXB (XML serializer for logging).
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedLocalTime {
    /**
     * Maps to the hour field in {@link LocalTime}
     */
    @XmlElement
    private byte hour;

    /**
     * Maps to the minute field in {@link LocalTime}
     */
    @XmlElement
    private byte minute;

    /**
     * Maps to the second field in {@link LocalTime}
     */
    @XmlElement
    private byte second;

    /**
     * Maps to the nano field in {@link LocalTime} (amount of nanoseconds).
     */
    @XmlElement
    private int nano;

    /**
     * Getter for hour
     *
     * @return The amount of hours as a byte.
     */
    public byte getHour() {
        return this.hour;
    }

    /**
     * Setter for hour
     *
     * @param hour The amount of hours to set (a byte).
     */
    public void setHour(byte hour) {
        this.hour = hour;
    }

    /**
     * Getter for minute
     *
     * @return The amount of minutes as a byte.
     */
    public byte getMinute() {
        return this.minute;
    }

    /**
     * Setter for minute
     *
     * @param minute The amount of minutes to set (a byte).
     */
    public void setMinute(byte minute) {
        this.minute = minute;
    }

    /**
     * Getter for second
     *
     * @return The amount of seconds as a byte
     */
    public byte getSecond() {
        return this.second;
    }

    /**
     * Setter for second
     *
     * @param second The amount of seconds to set (a byte)
     */
    public void setSecond(byte second) {
        this.second = second;
    }

    /**
     * Getter for nano
     *
     * @return The amount of nanoseconds as an int.
     */
    public int getNano() {
        return this.nano;
    }

    /**
     * Setter for nano
     *
     * @param nano The amount of nanoseconds to set (an int).
     */
    public void setNano(int nano) {
        this.nano = nano;
    }
}
