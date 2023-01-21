package eu.darkbot.api.extensions;

import eu.darkbot.api.extensions.drawers.BaseDrawer;
import eu.darkbot.api.extensions.drawers.LegacyDrawer;
import eu.darkbot.api.extensions.drawers.LineDrawer;
import eu.darkbot.api.extensions.drawers.OvalDrawer;
import eu.darkbot.api.extensions.drawers.RectDrawer;
import eu.darkbot.api.extensions.drawers.ShapeDrawer;
import eu.darkbot.api.extensions.drawers.StringDrawer;

/**
 * Bot map graphics
 */
@SuppressWarnings("deprecation")
public interface MapGraphics extends
        BaseDrawer, LineDrawer, RectDrawer, OvalDrawer, ShapeDrawer, StringDrawer, LegacyDrawer {

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
