package gamesforblind.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedLocalTime {
    @XmlElement
    private byte hour;

    @XmlElement
    private byte minute;

    @XmlElement
    private byte second;

    @XmlElement
    private int nano;

    public byte getHour() {
        return this.hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMinute() {
        return this.minute;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public byte getSecond() {
        return this.second;
    }

    public void setSecond(byte second) {
        this.second = second;
    }

    public int getNano() {
        return this.nano;
    }

    public void setNano(int nano) {
        this.nano = nano;
    }
}
