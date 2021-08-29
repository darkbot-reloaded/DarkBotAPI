package eu.darkbot.api.extensions;

/**
 * A way for features to define extra npc flags for the npc table configuration
 *
 * The only thing required is to implement the interface with an enum as the generic
 * constant, annotate the enum with {@link eu.darkbot.api.config.annotations.Configuration} to set
 * a base translation key, and define names for all constants in your resources file.
 *
 * @param <T> enum type defining the different extra npc flags
 */
public interface NpcFlags<T extends Enum<T>> {
}
