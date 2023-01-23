package eu.darkbot.api.extensions.drawers;

import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

public interface LineDrawer extends BaseDrawer {

    /**
     * Draws a line
     *
     * @param x1 the first point's <i>x</i> coordinate.
     * @param y1 the first point's <i>y</i> coordinate.
     * @param x2 the second point's <i>x</i> coordinate.
     * @param y2 the second point's <i>y</i> coordinate.
     */
    void drawLine(double x1, double y1, double x2, double y2);

    /**
     * Draws a line
     *
     * @param a start
     * @param b end
     */
    default void drawLine(Point a, Point b) {
        drawLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    /**
     * Draws a line
     *
     * @param a start
     * @param b end
     */
    default void drawLine(Locatable a, Locatable b) {
        drawLine(toScreenPoint(a), toScreenPoint(b));
    }

}
