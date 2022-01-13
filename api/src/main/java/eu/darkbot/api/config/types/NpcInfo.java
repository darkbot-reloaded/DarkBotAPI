package eu.darkbot.api.config.types;

import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.items.SelectableItem;

import java.util.Optional;

/**
 * Predefined settings for {@link Npc} customized by user.
 */
public interface NpcInfo {

    /**
     * Sets whether to kill the npc or not.
     */
    void setShouldKill(boolean shouldKill);

    /**
     * @return whether to kill the npc or not.
     */
    boolean shouldKill();

    /**
     * Sets how important this box is to the collector, the lower number is more important
     */
    void setPriority(int priority);

    /**
     * @return How important this box is to the user, the lower number is more important
     */
    int getPriority();

    /**
     * Sets how far away the ship will stand from this npc, in in-game distance units
     */
    void setRadius(double radius);

    /**
     * @return How far away the user wants to stand from this npc, in in-game distance units
     */
    double getRadius();

    /**
     * @return The type of ammo the user wants to attack this npc with.
     */
    Optional<SelectableItem.Laser> getAmmo();

    /**
     * @return The formation the user wants to use for this npc.
     */
    Optional<SelectableItem.Formation> getFormation();

    /**
     * If the user has selected the additional configuration flag.
     * @param flag The flag to check
     * @return If the user has selected this flag or not
     */
    boolean hasExtraFlag(Enum<?> flag);

    /**
     * This will modify the configuration of the user, be extremely
     * cautious when using this as it can confuse users.
     *
     * @param flag The flag to set
     * @param active if the flag should be set to active or inactive
     */
    void setExtraFlag(Enum<?> flag, boolean active);

}
