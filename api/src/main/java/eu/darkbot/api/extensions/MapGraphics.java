package eu.darkbot.api.extensions;

import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.function.Function;

/**
 * Bot map graphics
 */
public interface MapGraphics {

    int getWidth();
    int getHeight();

    /**
     * @return x-axis middle of bot map
     */
    int getMiddle();

    /**
     * @return {@link Graphics2D}
     */
    Graphics2D getGraphics2D();

    /**
     * Translates given in-game coordinates to {@link Point} on bot map
     *
     * @param x coordinate to translate
     * @param y coordinate to translate
     * @return position on bot map of given in-game coordinates
     */
    Point translate(double x, double y);

    default Point translate(Locatable pos) {
        return translate(pos.getX(), pos.getY());
    }

    /**
     * @param x coordinate to translate
     * @param y coordinate to translate
     * @return position in-game of given bot map coordinates
     */
    Locatable undoTranslate(double x, double y);

    default Locatable undoTranslate(Point point) {
        return undoTranslate(point.getX(), point.getY());
    }

    /**
     * Sets predefined color to use by user
     *
     * @param color new rendering color
     */
    void setColor(String color, Function<Color, Color> modifiers);

    default void setColor(String color) {
        setColor(color, null);
    }

    default void setColor(Color color) {
        getGraphics2D().setColor(color);
    }

    void setFont(String font, Function<Font, Font> modifiers);

    /**
     * Sets font to use
     *
     * @param font new rendering font
     */
    default void setFont(String font) {
        setFont(font, null);
    }

    default void setFont(Font font) {
        getGraphics2D().setFont(font);
    }

    default void drawRect(Point pos, boolean fill, int width, int height) {
        if (fill) getGraphics2D().fillRect(pos.x(), pos.y(), width, height);
        else getGraphics2D().drawRect(pos.x(), pos.y(), width, height);
    }

    default void drawRect(Locatable pos, boolean fill, int width, int height) {
        Point p = translate(pos);
        p = Point.of(p.x() - Math.round(width / 2d), p.y() - Math.round(height / 2d));

        drawRect(p, fill, width, height);
    }

    default void drawRect(Locatable pos, boolean fill, int size) {
        drawRect(pos, fill, size, size);
    }

    default void drawOval(Point pos, boolean fill, int width, int height) {
        if (fill) getGraphics2D().fillOval(pos.x(), pos.y(), width, height);
        else getGraphics2D().drawOval(pos.x(), pos.y(), width, height);
    }

    default void drawOval(Locatable pos, boolean fill, int size) {
        drawOval(pos, fill, size, size);
    }

    default void drawOval(Locatable pos, boolean fill, int width, int height) {
        Point p = translate(pos);
        p = Point.of(p.x() - Math.round(width / 2d), p.y() - Math.round(height / 2d));

        drawOval(p, fill, width, height);
    }

    /**
     * Draws a line
     *
     * @param a start
     * @param b end
     */
    default void drawLine(Locatable a, Locatable b) {
        drawLine(translate(a), translate(b));
    }

    /**
     * Draws a line
     *
     * @param a start
     * @param b end
     */
    default void drawLine(Point a, Point b) {
        getGraphics2D().drawLine(a.x(), a.y(), b.x(), b.y());
    }

    /**
     * Draws a String at given position
     *
     * @param str         String to draw
     * @param pos         where string will be drawn
     * @param heightAlign height align added after translate
     * @param align       String align
     */
    default void drawString(String str, Locatable pos, int heightAlign, Align align) {
        Point p = translate(pos);
        drawString(str, Point.of(p.x(), p.y() + heightAlign), align);
    }

    /**
     * Draws a String at given position
     *
     * @param str   String to draw
     * @param pos   where string will be drawn
     * @param align String align
     */
    default void drawString(String str, Point pos, Align align) {
        drawString(str, pos.x(), pos.y(), align);
    }

    default void drawString(String str, int x, int y, Align align) {
        if (str == null || str.isEmpty()) return;

        if (align != Align.LEFT) {
            int strWidth = getGraphics2D().getFontMetrics().stringWidth(str);
            x -= strWidth >> (align == Align.MID ? 1 : 0);
        }
        getGraphics2D().drawString(str, x, y);
    }

    /**
     * String align
     */
    enum Align {
        LEFT, MID, RIGHT
    }
}
