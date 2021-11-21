package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.game.other.Area;
import eu.darkbot.api.game.other.Gui;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Provides general view and controls of the in-game window.
 * E.g: guis, memory usage, focusing chat or toggling category bar
 * <br>
 * Should be avoided in favor of specific APIs providing higher-order
 * functions, that don't rely on a specific implementation of some
 * things here, like GUIs, view areas or FPS.
 */
public interface GameScreenAPI extends API.Singleton {

    /**
     * The specific area of the map that is currently visible on the screen
     * @return bounds of game screen
     */
    Area.Rectangle getViewBounds();

    /**
     * @return {@link Collection} of in-game guis
     */
    Collection<? extends Gui> getGuis();

    /**
     * Get a specific gui by its in-game registered key
     * @param key the in-game key for the gui
     * @return the gui if registered to the bot, null otherwise
     */
    @Nullable Gui getGui(String key);

    /**
     * @return in-game FPS.
     */
    int getFps();

    /**
     * @return memory used by the game in MB
     */
    int getMemory();

    /**
     * Tries to zoom in view. Usually '+' key bind in-game
     */
    void zoomIn();

    /**
     * Tries to zoom out view. Usually '-' key bind in-game
     */
    void zoomOut();

    /**
     * Focuses the in-game chat window via hotkey. Usually 'ENTER' key bind in-game
     *
     * Note: This does not seem to work at all in-game.
     */
    void focusOnChat();

    /**
     * Toggles FPS monitoring in-game. Usually 'F' key bind in-game
     */
    void toggleMonitoring();

    /**
     * Will try to toggle visibility for all windows in-game. Usually 'H' key bind in-game
     */
    void toggleWindows();

    /**
     * Open or close the category bar in-game
     * @param visible true to make it visible, false for invisible
     */
    void toggleCategoryBar(boolean visible);

    /**
     * Open or close the pro action bar in-game
     * @param visible true to make it visible, false for invisible
     */
    void toggleProActionBar(boolean visible);
}
