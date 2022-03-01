package eu.darkbot.api.extensions;

import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

import java.awt.Graphics2D;

public interface MapGraphics {

    Graphics2D getGraphics();

    Point translate(Locatable locatable);
    Locatable undoTranslate(Point point);

    default void drawLine(Locatable a, Locatable b) {
        drawLine(translate(a), translate(b));
    }

    default void drawLine(Point a, Point b) {
        getGraphics().drawLine(a.x(), b.x(), a.y(), b.y());
    }

    default void drawString(String str, Locatable position, Align align) {
        drawString(str, translate(position), align);
    }

    default void drawString(String str, Point position, Align align) {
        if (str == null || str.isEmpty()) return;

        int x = position.x();
        if (align != Align.LEFT) {
            int strWidth = getGraphics().getFontMetrics().stringWidth(str);
            x -= strWidth >> (align == Align.MID ? 1 : 0);
        }
        getGraphics().drawString(str, x, position.y());
    }

    enum Align {
        LEFT, MID, RIGHT
    }
}
