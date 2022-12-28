package eu.darkbot.api.extensions;

import eu.darkbot.api.config.types.DisplayFlag;
import eu.darkbot.api.game.other.Area;
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

    boolean hasDisplayFlag(DisplayFlag displayFlag);

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

    double getScaleX();
    double getScaleY();

    double toScreenPointX(double gameX);
    double toScreenPointY(double gameY);

    double toScreenSizeW(double gameW);
    double toScreenSizeH(double gameH);

    double toGameLocationX(double screenX);
    double toGameLocationY(double screenY);

    default Point toScreenPoint(double gameX, double gameY) {
        return Point.of(toScreenPointX(gameX), toScreenPointY(gameY));
    }

    default Point toScreenPoint(Locatable pos) {
        return toScreenPoint(pos.getX(), pos.getY());
    }

    default Locatable toGameLocation(double screenX, double screenY) {
        return Locatable.of(toGameLocationX(screenX), toGameLocationY(screenY));
    }

    default Locatable toGameLocation(Point point) {
        return toGameLocation(point.getX(), point.getY());
    }

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

    /**
     * Draws Oval
     */
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

    /**
     * Draws polygon
     */
    void drawPoly(PolyType type, @NotNull Point... points);

    default void drawPoly(PolyType type, @NotNull Locatable... positions) {
        Point[] points = new Point[positions.length];
        for (int i = 0; i < positions.length; i++)
            points[i] = toScreenPoint(positions[i]);

        drawPoly(type, points);
    }

    default void drawPolyPoints(PolyType type, Collection<? extends Point> points) {
        drawPoly(type, points.toArray(new Point[0]));
    }

    default void drawPolyLocs(PolyType type, Collection<? extends Locatable> positions) {
        drawPoly(type, positions.toArray(new Locatable[0]));
    }

    default void drawArea(Area area, boolean fill) {
        if (area instanceof Area.Circle) {
            Area.Circle circle = (Area.Circle) area;
            double size = (circle.getRadius() * 2);

            drawOval(circle, size / getScaleX(), size / getScaleY(), fill);
        } else if (area instanceof Area.Polygon) {
            Area.Polygon polygon = (Area.Polygon) area;
            drawPolyLocs(fill ? PolyType.FILL_POLYGON : PolyType.DRAW_POLYGON, polygon.getVertices());
        } else {
            Area.Rectangle rect = area.getBounds();
            drawRect(rect, rect.getWidth() / getScaleX(), rect.getHeight() / getScaleY(), fill);
        }
    }

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
        drawString(point.getX(), point.getY(), str, stringAlign);
    }

    default void drawString(double x, double y, String str, StringAlign stringAlign) {
        if (str == null || str.isEmpty()) return;

        if (stringAlign != StringAlign.LEFT)
            x -= getStringWidth(str) >> (stringAlign == StringAlign.MID ? 1 : 0);

        getGraphics2D().drawString(str, (float) x, (float) y);
    }

    default void drawBackgroundedText(double x, double y, String str, Color backgroundColor, StringAlign stringAlign) {
        if (str == null || str.isEmpty()) return;

        if (stringAlign != StringAlign.LEFT)
            x -= getStringWidth(str) >> (stringAlign == StringAlign.MID ? 1 : 0);

        AttributedString attrString = new AttributedString(str);
        attrString.addAttribute(TextAttribute.BACKGROUND, backgroundColor);
        attrString.addAttribute(TextAttribute.FONT, getGraphics2D().getFont());

        getGraphics2D().drawString(attrString.getIterator(), (float) x, (float) y);
    }

    default void drawBackgroundedText(Point point, String str, Color backgroundColor, StringAlign stringAlign) {
        drawBackgroundedText(point.getX(), point.getY(), str, backgroundColor, stringAlign);
    }

    default void drawBackgroundedText(Point point, String str, StringAlign stringAlign) {
        drawBackgroundedText(point, str, getColor("texts_background"), stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, Color backgroundColor, int yOffset, StringAlign stringAlign) {
        drawBackgroundedText(toScreenPointX(loc.getX()),
                toScreenPointY(loc.getY()) + yOffset, str, backgroundColor, stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, int yOffset, StringAlign stringAlign) {
        drawBackgroundedText(loc, str, getColor("texts_background"), yOffset, stringAlign);
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

    //deprecated functions

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
    default void drawString(int x, int y, String str, StringAlign stringAlign) {
        drawString((double) x, y, str, stringAlign);
    }

    @Deprecated
    default void drawBackgroundedText(int x, int y, String str, Color backgroundColor, StringAlign stringAlign) {
        drawBackgroundedText((double) x, y, str, backgroundColor, stringAlign);
    }
}
