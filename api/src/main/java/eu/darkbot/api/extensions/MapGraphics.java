package eu.darkbot.api.extensions;

import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Collection;

/**
 * Bot map graphics
 */
public interface MapGraphics {

    /**
     * @return {@link Graphics2D}
     */
    Graphics2D getGraphics2D();

    int getWidth();
    int getHeight();
    int getWidthMiddle();
    int getHeightMiddle();

    Color getColor(String color);

    default void setColor(String color) {
        setColor(getColor(color));
    }

    default void setColor(Color color) {
        getGraphics2D().setColor(color);
    }

    Font getFont(String font);

    default void setFont(String font) {
        setFont(getFont(font));
    }

    default void setFont(Font font) {
        getGraphics2D().setFont(font);
    }

    int toScreenPointX(double gameX);
    int toScreenPointY(double gameY);

    double toGameLocationX(int screenX);
    double toGameLocationY(int screenY);

    default Point toScreenPoint(double gameX, double gameY) {
        return Point.of(toScreenPointX(gameX), toScreenPointY(gameY));
    }

    default Point toScreenPoint(Locatable pos) {
        return toScreenPoint(pos.getX(), pos.getY());
    }

    default Locatable toGameLocation(int screenX, int screenY) {
        return Locatable.of(toGameLocationX(screenX), toGameLocationY(screenY));
    }

    default Locatable toGameLocation(Point point) {
        return toGameLocation(point.x(), point.y());
    }

    /**
     * Draws Rect
     */
    default void drawRect(int x, int y, int width, int height, boolean fill) {
        if (fill) getGraphics2D().fillRect(x, y, width, height);
        else getGraphics2D().drawRect(x, y, width, height);
    }

    default void drawRect(int x, int y, int size, boolean fill) {
        drawRect(x, y, size, size, fill);
    }

    default void drawRect(Point point, int width, int height, boolean fill) {
        drawRect(point.x(), point.y(), width, height, fill);
    }

    default void drawRect(Locatable loc, int width, int height, boolean fill) {
        drawRect(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()), width, height, fill);
    }

    default void drawRect(Locatable loc, int size, boolean fill) {
        drawRect(loc, size, size, fill);
    }

    default void drawRectCentered(Locatable loc, int width, int height, boolean fill) {
        drawRect((int) (toScreenPointX(loc.getX()) - Math.round(width / 2d)),
                (int) (toScreenPointY(loc.getY()) - Math.round(height / 2d)), width, height, fill);
    }

    default void drawRectCentered(Locatable loc, int size, boolean fill) {
        drawRectCentered(loc, size, size, fill);
    }

    /**
     * Draws Oval
     */
    default void drawOval(int x, int y, int width, int height, boolean fill) {
        if (fill) getGraphics2D().fillOval(x, y, width, height);
        else getGraphics2D().drawOval(x, y, width, height);
    }

    default void drawOval(int x, int y, int size, boolean fill) {
        drawOval(x, y, size, size, fill);
    }

    default void drawOval(Point point, int width, int height, boolean fill) {
        drawOval(point.x(), point.y(), width, height, fill);
    }

    default void drawOval(Locatable loc, int width, int height, boolean fill) {
        drawOval(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()), width, height, fill);
    }

    default void drawOval(Locatable loc, int size, boolean fill) {
        drawOval(loc, size, size, fill);
    }

    default void drawOvalCentered(Locatable loc, int width, int height, boolean fill) {
        drawOval((int) (toScreenPointX(loc.getX()) - Math.round(width / 2d)),
                (int) (toScreenPointY(loc.getY()) - Math.round(height / 2d)), width, height, fill);
    }

    default void drawOvalCentered(Locatable loc, int size, boolean fill) {
        drawOvalCentered(loc, size, size, fill);
    }

    /**
     * Draws polygon
     */
    default void drawPoly(PolyType type, @NotNull Point... points) {
        if (points.length == 0) return;

        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            xPoints[i] = points[i].x();
            yPoints[i] = points[i].y();
        }

        if (type == PolyType.DRAW_POLYGON) getGraphics2D().drawPolygon(xPoints, yPoints, points.length);
        else if (type == PolyType.FILL_POLYGON) getGraphics2D().fillPolygon(xPoints, yPoints, points.length);
        else if (type == PolyType.DRAW_POLYLINE) getGraphics2D().drawPolyline(xPoints, yPoints, points.length);
    }

    default void drawPoly(PolyType type, @NotNull Locatable... positions) {
        Point[] points = new Point[positions.length];
        for (int i = 0; i < positions.length; i++)
            points[i] = toScreenPoint(positions[i]);

        drawPoly(type, points);
    }

    default void drawPoly(PolyType type, Collection<Point> points) {
        drawPoly(type, points.toArray(new Point[0]));
    }

    default void drawPoly(Collection<Locatable> positions, PolyType type) {
        drawPoly(type, positions.toArray(new Locatable[0]));
    }

    /**
     * Draws a line
     *
     * @param x1 the first point's <i>x</i> coordinate.
     * @param y1 the first point's <i>y</i> coordinate.
     * @param x2 the second point's <i>x</i> coordinate.
     * @param y2 the second point's <i>y</i> coordinate.
     */
    default void drawLine(int x1, int y1, int x2, int y2) {
        getGraphics2D().drawLine(x1, y1, x2, y2);
    }

    /**
     * Draws a line
     *
     * @param a start
     * @param b end
     */
    default void drawLine(Point a, Point b) {
        drawLine(a.x(), a.y(), b.x(), b.y());
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

    /**
     * @return string width using current font
     */
    default int getStringWidth(String str) {
        return getGraphics2D().getFontMetrics().stringWidth(str);
    }

    /**
     * Draws a String at given position
     *
     * @param str         String to draw
     * @param loc         where string will be drawn
     * @param yOffset     y offset added after translate to screen point
     * @param stringAlign String align
     */
    default void drawString(Locatable loc, String str, int yOffset, StringAlign stringAlign) {
        drawString(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()) + yOffset, str, stringAlign);
    }

    /**
     * Draws a String at given position
     *
     * @param str         String to draw
     * @param point       where string will be drawn
     * @param stringAlign String align
     */
    default void drawString(Point point, String str, StringAlign stringAlign) {
        drawString(point.x(), point.y(), str, stringAlign);
    }

    default void drawString(int x, int y, String str, StringAlign stringAlign) {
        if (str == null || str.isEmpty()) return;

        if (stringAlign != StringAlign.LEFT)
            x -= getStringWidth(str) >> (stringAlign == StringAlign.MID ? 1 : 0);

        getGraphics2D().drawString(str, x, y);
    }

    default void drawBackgroundedText(int x, int y, String str, Color backgroundColor, StringAlign stringAlign) {
        if (str == null || str.isEmpty()) return;

        if (stringAlign != StringAlign.LEFT)
            x -= getStringWidth(str) << (stringAlign == StringAlign.MID ? 1 : 0);

        AttributedString attrString = new AttributedString(str);
        attrString.addAttribute(TextAttribute.BACKGROUND, backgroundColor);
        attrString.addAttribute(TextAttribute.FONT, getGraphics2D().getFont());

        getGraphics2D().drawString(attrString.getIterator(), x, y);
    }

    default void drawBackgroundedText(Point point, String str, Color backgroundColor, StringAlign stringAlign) {
        drawBackgroundedText(point.x(), point.y(), str, backgroundColor, stringAlign);
    }

    default void drawBackgroundedText(Point point, String str, StringAlign stringAlign) {
        drawBackgroundedText(point, str, getColor("texts_background"), stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, Color backgroundColor, int heightAlign, StringAlign stringAlign) {
        drawBackgroundedText(toScreenPointX(loc.getX()),
                toScreenPointY(loc.getY()) + heightAlign, str, backgroundColor, stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, int heightAlign, StringAlign stringAlign) {
        drawBackgroundedText(loc, str, getColor("texts_background"), heightAlign, stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, Color backgroundColor, StringAlign stringAlign) {
        drawBackgroundedText(loc, str, backgroundColor, 0, stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, StringAlign stringAlign) {
        drawBackgroundedText(loc, str, 0, stringAlign);
    }

    /**
     * String align
     */
    enum StringAlign {
        LEFT, MID, RIGHT
    }

    enum PolyType {
        DRAW_POLYGON, FILL_POLYGON, DRAW_POLYLINE;
    }
}
