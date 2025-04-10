package eu.darkbot.api.extensions;

import eu.darkbot.api.utils.Version;

import java.net.URL;

/**
 * The information of a plugin, usually specified in a plugin.json file inside the jar
 */
public interface PluginInfo extends IssueHandler {

    /**
     * Used as both an identifier and the base for features &amp; resources
     * @return the plugin base package where code &amp; resources are located
     */
    String getBasePackage();

    /**
     * @return The name of the plugin
     */
    String getName();

    /**
     * @return The name of the author
     */
    String getAuthor();

    /**
     * @return A version number for this plugin
     */
    Version getVersion();

    /**
     * @return The minimum bot version required to run this plugin.
     *         This can be used to avoid users trying to use the plugin
     *         in outdated versions that not have the required API extensions.
     */
    Version getMinimumVersion();

    /**
     * @return The latest version this plugin has been tested and known to work with
     *         The bot will not prevent loading outdated plugins, but it may warn the user
     *         about the plugin potentially being too outdated to support.
     */
    Version getSupportedVersion();

    /**
     * @return A URL pointing to a new plugin info JSON for an updated version of this plugin
     */
    URL getUpdateURL();

    /**
     * @return A link that directs users to a way to donate to the plugin author
     */
    URL getDonationURL();

    /**
     * @return A link that can download this version of the plugin. This is used in
     *         conjunction with {@link #getUpdateURL} to automatically update plugins.
     */
    URL getDownloadURL();

    /**
     * @return An array of all the defined feature ids
     */
    String[] getFeatureIds();

}
