package gamesforblind.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.*;

public class PointAdapter extends XmlAdapter<AdaptedPoint, Point> {
    public Point unmarshal(AdaptedPoint adaptedPoint) {
        if (adaptedPoint == null) {
            return null;
        }

        return new Point(adaptedPoint.getX(), adaptedPoint.getY());
    }

    public AdaptedPoint marshal(Point point) {
        if (point == null) {
            return null;
        }

        AdaptedPoint adaptedPoint = new AdaptedPoint();
        adaptedPoint.setX(point.x);
        adaptedPoint.setY(point.y);
        return adaptedPoint;
    }
}
