package eu.darkbot.api.extensions.drawers;

import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

public interface OvalDrawer extends BaseDrawer {

    void drawOval(double x, double y, double width, double height, boolean fill);

    default void drawOval(double x, double y, double size, boolean fill) {
        drawOval(x, y, size, size, fill);
    }

    default void drawOval(Point point, double width, double height, boolean fill) {
        drawOval(point.getX(), point.getY(), width, height, fill);
    }

    default void drawOval(Locatable loc, double width, double height, boolean fill) {
        drawOval(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()), width, height, fill);
    }

    default void drawOval(Locatable loc, double size, boolean fill) {
        drawOval(loc, size, size, fill);
    }

    default void drawOvalCentered(Locatable loc, double width, double height, boolean fill) {
        drawOval(toScreenPointX(loc.getX()) - (width / 2), toScreenPointY(loc.getY()) - (height / 2), width, height, fill);
    }

    default void drawOvalCentered(Locatable loc, double size, boolean fill) {
        drawOvalCentered(loc, size, size, fill);
    }

}
