package eu.darkbot.api.config.util;

import eu.darkbot.api.config.ConfigSetting;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface ValueHandler<T> {

    /**
     * Validate the input is a valid value
     * eg: if it's a number check it doesn't exceed min/max limits
     *
     * @param t the value to validate
     * @return the value, corrected if invalid
     */
    T validate(T t);

    /**
     * After a value has been set, this method must be called in order
     * to update the parent field for this child with the new value
     *
     * This method will also call {@link #updateChildren} in each of the
     * children nodes in the setting.
     *
     * @param setting The config setting being updated
     */
    void updateParent(ConfigSetting<T> setting);

    /**
     * When a parent configuration has been updated via {@link ValueHandler#updateParent(ConfigSetting)},
     * children will receive a call to this method to update their value based on the new parent.
     *
     * @param setting The config setting this belongs to
     */
    void updateChildren(ConfigSetting<T> setting);

    /**
     * Get metadata from the value handler
     * @param key the key used for this data
     * @param <V> the datatype for the data
     * @return the metadata value for the key if present, null otherwise
     */
    <V> @Nullable V getMetadata(String key);

    /**
     * Get metadata from the value handler, or create it if it doesn't exist
     * @param key the key used for this data
     * @param builder the builder to generate the type of metadata
     * @param <V> the datatype for the data
     * @return the metadata value for the key if present, null otherwise
     */
    <V> V getOrCreateMetadata(String key, Supplier<V> builder);

}
