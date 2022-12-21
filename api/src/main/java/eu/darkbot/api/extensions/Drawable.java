package eu.darkbot.api.extensions;

public interface Drawable {

    default void onDraw(MapGraphics mg) {}
    default void onDrawRadiation(MapGraphics mg, MapGraphics radiationScaled) {}
}
