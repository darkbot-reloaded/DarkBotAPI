package eu.darkbot.api.managers;

import eu.darkbot.api.API;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

/**
 * API which let you open links in native browser supported by DarkBot
 */
@ApiStatus.AvailableSince("0.8.4")
public interface NativeBrowserAPI extends API.Singleton {

    boolean isSupported();

    /**
     * May open any link you provide with potentially dosid cookie set
     *
     * @param link link to open
     * @return process of the browser if was open successfully, null otherwise
     */
    @Nullable Process openLink(String link);

    @Nullable default Process openLink(URI uri) {
        return openLink(uri.toString());
    }

    /**
     * Opens game link with host and set dosid cookie
     *
     * @param path path of the url to open
     * @return process of the browser if was open successfully, null otherwise
     */
    @Nullable Process openGameLink(String path);
}
