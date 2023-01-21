package eu.darkbot.api.extensions.drawers;

import eu.darkbot.api.extensions.MapGraphics;
import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

public interface StringDrawer extends BaseDrawer {
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
    default void drawString(Locatable loc, String str, int yOffset, MapGraphics.StringAlign stringAlign) {
        drawString(toScreenPointX(loc.getX()), toScreenPointY(loc.getY()) + yOffset, str, stringAlign);
    }

    /**
     * Draws a String at given position
     *
     * @param str         String to draw
     * @param point       where string will be drawn
     * @param stringAlign String align
     */
    default void drawString(Point point, String str, MapGraphics.StringAlign stringAlign) {
        drawString(point.getX(), point.getY(), str, stringAlign);
    }

    default void drawString(double x, double y, String str, MapGraphics.StringAlign stringAlign) {
        if (str == null || str.isEmpty()) return;

        if (stringAlign != MapGraphics.StringAlign.LEFT)
            x -= getStringWidth(str) >> (stringAlign == MapGraphics.StringAlign.MID ? 1 : 0);

        getGraphics2D().drawString(str, (float) x, (float) y);
    }

    default void drawBackgroundedText(double x, double y, String str, Color backgroundColor, MapGraphics.StringAlign stringAlign) {
        if (str == null || str.isEmpty()) return;

        if (stringAlign != MapGraphics.StringAlign.LEFT)
            x -= getStringWidth(str) >> (stringAlign == MapGraphics.StringAlign.MID ? 1 : 0);

        AttributedString attrString = new AttributedString(str);
        attrString.addAttribute(TextAttribute.BACKGROUND, backgroundColor);
        attrString.addAttribute(TextAttribute.FONT, getGraphics2D().getFont());

        getGraphics2D().drawString(attrString.getIterator(), (float) x, (float) y);
    }

    default void drawBackgroundedText(Point point, String str, Color backgroundColor, MapGraphics.StringAlign stringAlign) {
        drawBackgroundedText(point.getX(), point.getY(), str, backgroundColor, stringAlign);
    }

    default void drawBackgroundedText(Point point, String str, MapGraphics.StringAlign stringAlign) {
        drawBackgroundedText(point, str, getColor("texts_background"), stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, Color backgroundColor, int yOffset, MapGraphics.StringAlign stringAlign) {
        drawBackgroundedText(toScreenPointX(loc.getX()),
                toScreenPointY(loc.getY()) + yOffset, str, backgroundColor, stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, int yOffset, MapGraphics.StringAlign stringAlign) {
        drawBackgroundedText(loc, str, getColor("texts_background"), yOffset, stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, Color backgroundColor, MapGraphics.StringAlign stringAlign) {
        drawBackgroundedText(loc, str, backgroundColor, 0, stringAlign);
    }

    default void drawBackgroundedText(Locatable loc, String str, MapGraphics.StringAlign stringAlign) {
        drawBackgroundedText(loc, str, 0, stringAlign);
    }

}
