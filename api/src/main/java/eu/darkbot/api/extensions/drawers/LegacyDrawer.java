package eu.darkbot.api.extensions.drawers;

import eu.darkbot.api.extensions.MapGraphics;
import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

import java.awt.*;

@Deprecated
@SuppressWarnings("PMD.TooManyMethods")
public interface LegacyDrawer extends BaseDrawer, LineDrawer, RectDrawer, OvalDrawer, StringDrawer {

    @Deprecated
    default double toGameLocationX(int screenX) {
        return toGameLocationX((double) screenX);
    }

    @Deprecated
    default double toGameLocationY(int screenY) {
        return toGameLocationY((double) screenY);
    }

    @Deprecated
    default Locatable toGameLocation(int screenX, int screenY) {
        return toGameLocation((double) screenX, screenY);
    }

    @Deprecated
    default void drawRect(int x, int y, int width, int height, boolean fill) {
        drawRect((double) x, y, width, height, fill);
    }

    @Deprecated
    default void drawRect(Point point, int width, int height, boolean fill) {
        drawRect(point.getX(), point.getY(), width, height, fill);
    }

    @Deprecated
    default void drawRect(Locatable loc, int width, int height, boolean fill) {
        drawRect(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()), width, height, fill);
    }

    @Deprecated
    default void drawRect(Locatable loc, int size, boolean fill) {
        drawRect(loc, size, size, fill);
    }

    @Deprecated
    default void drawRectCentered(Locatable loc, int width, int height, boolean fill) {
        drawRect(toScreenPointX(loc.getX()) - (width >> 1), toScreenPointY(loc.getY()) - (height >> 1), width, height, fill);
    }

    @Deprecated
    default void drawRectCentered(Locatable loc, int size, boolean fill) {
        drawRectCentered(loc, size, size, fill);
    }

    @Deprecated
    default void drawOval(int x, int y, int width, int height, boolean fill) {
        drawOval((double) x, y, width, height, fill);
    }

    @Deprecated
    default void drawOval(int x, int y, int size, boolean fill) {
        drawOval(x, y, size, size, fill);
    }

    @Deprecated
    default void drawOval(Point point, int width, int height, boolean fill) {
        drawOval(point.getX(), point.getY(), width, height, fill);
    }

    @Deprecated
    default void drawOval(Locatable loc, int width, int height, boolean fill) {
        drawOval(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()), width, height, fill);
    }

    @Deprecated
    default void drawOval(Locatable loc, int size, boolean fill) {
        drawOval(loc, size, size, fill);
    }

    @Deprecated
    default void drawOvalCentered(Locatable loc, int width, int height, boolean fill) {
        drawOval(toScreenPointX(loc.getX()) - (width >> 1), toScreenPointY(loc.getY()) - (height >> 1), width, height, fill);
    }

    @Deprecated
    default void drawOvalCentered(Locatable loc, int size, boolean fill) {
        drawOvalCentered(loc, size, size, fill);
    }

    @Deprecated
    default void drawLine(int x1, int y1, int x2, int y2) {
        drawLine((double) x1, y1, x2, y2);
    }

    @Deprecated
    default void drawString(int x, int y, String str, MapGraphics.StringAlign stringAlign) {
        drawString((double) x, y, str, stringAlign);
    }

    @Deprecated
    default void drawBackgroundedText(int x, int y, String str, Color backgroundColor, MapGraphics.StringAlign stringAlign) {
        drawBackgroundedText((double) x, y, str, backgroundColor, stringAlign);
    }
}
