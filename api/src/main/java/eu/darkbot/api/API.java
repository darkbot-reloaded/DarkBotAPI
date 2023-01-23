package eu.darkbot.api;

public interface API {

    /**
     * Indicates that only one instance of class can exist in PluginAPI context
     */
    interface Singleton extends API {
    }
}
