package eu.darkbot.api.extensions.drawers;

import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

public interface RectDrawer extends BaseDrawer {

    /**
     * Draws Rect
     */
    void drawRect(double x, double y, double width, double height, boolean fill);

    default void drawRect(double x, double y, double size, boolean fill) {
        drawRect(x, y, size, size, fill);
    }

    default void drawRect(Point point, double width, double height, boolean fill) {
        drawRect(point.getX(), point.getY(), width, height, fill);
    }

    default void drawRect(Locatable loc, double width, double height, boolean fill) {
        drawRect(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()), width, height, fill);
    }

    default void drawRect(Locatable loc, double size, boolean fill) {
        drawRect(loc, size, size, fill);
    }

    default void drawRectCentered(Locatable loc, double width, double height, boolean fill) {
        drawRect(toScreenPointX(loc.getX()) - (width / 2), toScreenPointY(loc.getY()) - (height / 2), width, height, fill);
    }

    default void drawRectCentered(Locatable loc, double size, boolean fill) {
        drawRectCentered(loc, size, size, fill);
    }

}
