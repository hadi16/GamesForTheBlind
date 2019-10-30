package gamesforblind.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedLocalDate {
    @XmlElement
    private int year;

    @XmlElement
    private short month;

    @XmlElement
    private short day;

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public short getMonth() {
        return this.month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public short getDay() {
        return this.day;
    }

    public void setDay(short day) {
        this.day = day;
    }
}
