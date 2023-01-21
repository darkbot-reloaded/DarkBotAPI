package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import eu.darkbot.api.extensions.Task;
import eu.darkbot.util.TimeUtils;
import eu.darkbot.util.http.Http;
import eu.darkbot.util.http.Method;
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
     * @return true if sid, user id and instance uri are usable
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
     * Returns the ID of the current user. A value of 0 indicates that the user ID has not been initialized.
     * {@link #isInstanceValid()} checks if user ID is not 0
     */
    int getUserId();

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
     * Forces last request time to be updated to the current time
     */
    void updateLastRequestTime();

    /**
     * Returns connection with current {@link #getInstanceURI()} &amp; path, with the sessionID cookie.
     *
     * @param path URL path &amp; query parameters to append to {@link #getInstanceURI()}
     *
     * @deprecated Use {@link #getHttp(String)} instead
     */
    @Deprecated
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    HttpURLConnection getConnection(@NotNull String path) throws Exception;

    /**
     * Returns connection with current {@link #getInstanceURI()} &amp; path, with the sessionID cookie.
     *
     * @param path URL path &amp; query parameters to append to {@link #getInstanceURI()}
     * @param minWait Minimum time to wait since the last request in milliseconds.
     *                If the last request was over minWait ms ago, behavior
     *                is identical to {@link #getConnection(String)}, otherwise it will
     *                first sleep until enough time has passed.
     *
     * @deprecated Use {@link #getHttp(String, int)} instead
     */
    @Deprecated
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    default HttpURLConnection getConnection(@NotNull String path, int minWait) throws Exception {
        TimeUtils.sleepThread(getLastRequestTime().toEpochMilli() + minWait - System.currentTimeMillis());

        return getConnection(path);
    }

    /**
     * Creates an HTTP client that connects to {@link #getInstanceURI()} &amp; path, with the sessionID cookie set.
     * You may manipulate this further, adding parameters via the builder-style methods
     *
     * @param path The path to append at the end of the URI
     * @param method HTTP method to call with
     * @return An HTTP client to perform the connection with
     */
    default Http http(@NotNull String path, Method method) {
        if (!isInstanceValid()) throw new UnsupportedOperationException("Can't connect when instance is invalid");

        return Http.create(getInstanceURI() + path, method)
                .setRawHeader("Cookie", "dosid=" + getSid())
                .addSupplier(this::updateLastRequestTime);
    }

    /**
     * Creates an HTTP client that connects to {@link #getInstanceURI()} &amp; path, with the sessionID cookie set.
     * Will ensure at least {@code minWait} ms have elapsed since last request.
     *
     * @param path The path to append at the end of the URI
     * @param method HTTP method to call with
     * @param minWait minimum delay since last call, in milliseconds
     * @return An HTTP client to perform the connection with
     */
    default Http http(@NotNull String path, Method method, int minWait) {
        TimeUtils.sleepThread(getLastRequestTime().toEpochMilli() + minWait - System.currentTimeMillis());
        return http(path, method);
    }

    /**
     * Equivalent to {@link #http(String, Method)} with GET method.
     *
     * @param path The path to append at the end of the URI
     * @return An HTTP client to perform the connection with
     */
    default Http getHttp(@NotNull String path) {
        return http(path, Method.GET);
    }

    /**
     * Equivalent to {@link #http(String, Method, int)} with GET method.
     *
     * @param path The path to append at the end of the URI
     * @return An HTTP client to perform the connection with
     */
    default Http getHttp(@NotNull String path, int minWait) {
        return http(path, Method.GET, minWait);
    }

    /**
     * Equivalent to {@link #http(String, Method)} with POST method.
     *
     * @param path The path to append at the end of the URI
     * @return An HTTP client to perform the connection with
     */
    default Http postHttp(@NotNull String path) {
        return http(path, Method.POST);
    }

    /**
     * Equivalent to {@link #http(String, Method, int)} with POST method.
     *
     * @param path The path to append at the end of the URI
     * @return An HTTP client to perform the connection with
     */
    default Http postHttp(@NotNull String path, int minWait) {
        return http(path, Method.POST, minWait);
    }

    /**
     * Random string representing the reload-token of the loaded page
     *
     * @param body which will be searched for reload token
     * @return reload token or {@link Optional#empty()} if wasn't found
     */
    Optional<String> findReloadToken(@NotNull String body);
}
