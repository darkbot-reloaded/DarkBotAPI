package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.extensions.Task;
import eu.darkbot.util.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.net.HttpURLConnection;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;

/**
 * Provides access to connect &amp; manage the backpage of the game.
 * Should only be called from background thread, used in {@link Task}s.
 *
 * @see Task
 */
public interface BackpageAPI extends API.Singleton {

    /**
     * Check if any instance connection can be made,
     * however this doesn't ensure you that SID is valid/alive
     *
     * @return true if sid and instance uri are usable
     */
    boolean isInstanceValid();

    /**
     * Returns the value returned by the SID status request. 
     * 200 = SID is working correctly
     */
    String getSidStatus();

    @UnknownNullability("Check #isInstanceValid")
    String getSid();

    /**
     * Returns instance {@link URI}
     * for example: {@code https://int1.darkorbit.com/}
     */
    @UnknownNullability("Check #isInstanceValid")
    URI getInstanceURI();

    /**
     * @return last request time.
     */
    Instant getLastRequestTime();

    /**
     * Returns connection with current {@link #getInstanceURI()} &amp; path, with the sessionID cookie.
     *
     * @param path URL path &amp; query parameters to append to {@link #getInstanceURI()}
     */
    HttpURLConnection getConnection(@NotNull String path) throws Exception;

    /**
     * Returns connection with current {@link #getInstanceURI()} &amp; path, with the sessionID cookie.
     *
     *
     * @param path URL path &amp; query parameters to append to {@link #getInstanceURI()}
     * @param minWait Minimum time to wait since the last request in milliseconds.
     *                If the last request was over minWait ms ago, behavior
     *                is identical to {@link #getConnection(String)}, otherwise it will
     *                first sleep until enough time has passed.
     */
    default HttpURLConnection getConnection(@NotNull String path, int minWait) throws Exception {
        TimeUtils.sleepThread(getLastRequestTime().toEpochMilli() + minWait - System.currentTimeMillis());

        return getConnection(path);
    }

    /**
     * Random string representing the reload-token of the loaded page
     *
     * @param body which will be searched for reload token
     * @return reload token or {@link Optional#empty()} if wasn't found
     */
    Optional<String> findReloadToken(@NotNull String body);
}
