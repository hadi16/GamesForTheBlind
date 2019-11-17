package gamesforblind.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

/**
 * An adapted {@link LocalDate} to have a zero-argument constructor to work with JAXB (XML serializer for logging).
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedLocalDate {
    /**
     * Maps to the year field in {@link LocalDate}.
     */
    @XmlElement
    private int year;

    /**
     * Maps to the month field in {@link LocalDate}.
     */
    @XmlElement
    private short month;

    /**
     * Maps to the day field in {@link LocalDate}.
     */
    @XmlElement
    private short day;

    /**
     * Getter for year
     *
     * @return The year as an integer.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Setter for year
     *
     * @param year The year to set (an integer).
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Getter for month
     *
     * @return The month as a short.
     */
    public short getMonth() {
        return this.month;
    }

    /**
     * Setter for month
     *
     * @param month The month to set (a short).
     */
    public void setMonth(short month) {
        this.month = month;
    }

    /**
     * Getter for day
     *
     * @return The day as a short.
     */
    public short getDay() {
        return this.day;
    }

    /**
     * Setter for day
     *
     * @param day The day to set (a short).
     */
    public void setDay(short day) {
        this.day = day;
    }
}
