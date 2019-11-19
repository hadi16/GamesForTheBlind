package gamesforblind.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.*;

/**
 * Class to convert between {@link AdaptedPoint} and {@link Point}.
 */
public class PointAdapter extends XmlAdapter<AdaptedPoint, Point> {
    /**
     * Converts an {@link AdaptedPoint} to a {@link Point}.
     *
     * @param adaptedPoint The {@link AdaptedPoint} to convert.
     * @return A {@link Point}, which is equivalent to the passed {@link AdaptedPoint} object.
     */
    @Override
    public Point unmarshal(AdaptedPoint adaptedPoint) {
        // null points are used in SudokuHighlightAction
        if (adaptedPoint == null) {
            return null;
        }

        return new Point(adaptedPoint.getX(), adaptedPoint.getY());
    }

    /**
     * Converts an {@link Point} to a {@link AdaptedPoint}.
     *
     * @param point The {@link Point} to convert.
     * @return An {@link AdaptedPoint}, which is equivalent to the passed {@link Point} object.
     */
    @Override
    public AdaptedPoint marshal(Point point) {
        // null points are used in SudokuHighlightAction
        if (point == null) {
            return null;
        }

        AdaptedPoint adaptedPoint = new AdaptedPoint();
        adaptedPoint.setX(point.x);
        adaptedPoint.setY(point.y);
        return adaptedPoint;
    }
}
