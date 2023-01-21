package eu.darkbot.api.extensions.drawers;

import eu.darkbot.api.extensions.MapGraphics;
import eu.darkbot.api.game.other.Area;
import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ShapeDrawer extends BaseDrawer, OvalDrawer, RectDrawer {
    /**
     * Draws polygon
     */
    void drawPoly(MapGraphics.PolyType type, @NotNull Point... points);

    default void drawPoly(MapGraphics.PolyType type, @NotNull Locatable... positions) {
        Point[] points = new Point[positions.length];
        for (int i = 0; i < positions.length; i++) {
            points[i] = toScreenPoint(positions[i]);
        }

        drawPoly(type, points);
    }

    default void drawPolyPoints(MapGraphics.PolyType type, Collection<? extends Point> points) {
        drawPoly(type, points.toArray(new Point[0]));
    }

    default void drawPolyLocs(MapGraphics.PolyType type, Collection<? extends Locatable> positions) {
        drawPoly(type, positions.toArray(new Locatable[0]));
    }

    default void drawArea(Area area, boolean fill) {
        if (area instanceof Area.Circle) {
            Area.Circle circle = (Area.Circle) area;
            double size = circle.getRadius() * 2;

            drawOval(circle, toScreenSizeW(size), toScreenSizeH(size), fill);
        } else if (area instanceof Area.Polygon) {
            Area.Polygon polygon = (Area.Polygon) area;
            drawPolyLocs(fill ? MapGraphics.PolyType.FILL_POLYGON : MapGraphics.PolyType.DRAW_POLYGON, polygon.getVertices());
        } else {
            Area.Rectangle rect = area.getBounds();
            drawRect(rect, toScreenSizeW(rect.getWidth()), toScreenSizeH(rect.getHeight()), fill);
        }
    }

}
