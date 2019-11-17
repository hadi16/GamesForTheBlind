package gamesforblind.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;

/**
 * An adapted {@link Point} to have a zero-argument constructor to work with JAXB (XML serializer for logging).
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedPoint {
    /**
     * Maps to x in {@link Point}.
     */
    @XmlElement
    private int x;

    /**
     * Maps to y in {@link Point}
     */
    @XmlElement
    private int y;

    /**
     * Getter for x
     *
     * @return The x position as an int.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Setter for x
     *
     * @param x The x position to set (an int).
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter for y
     *
     * @return The y position as an int.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Setter for y
     *
     * @param y The y position to set (an int).
     */
    public void setY(int y) {
        this.y = y;
    }
}
