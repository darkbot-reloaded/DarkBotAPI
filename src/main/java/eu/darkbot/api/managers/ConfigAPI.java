package eu.darkbot.api.managers;

public interface ConfigAPI {

    <T> T getConfig(String path, Class<T> type);

}
