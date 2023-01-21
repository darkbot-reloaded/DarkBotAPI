package eu.darkbot.api.extensions.drawers;

import eu.darkbot.api.config.types.DisplayFlag;
import eu.darkbot.api.game.other.Locatable;
import eu.darkbot.api.game.other.Point;

import java.awt.*;

public interface BaseDrawer extends DrawingSize {

    /**
     * @return {@link Graphics2D}
     */
    Graphics2D getGraphics2D();

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

}
