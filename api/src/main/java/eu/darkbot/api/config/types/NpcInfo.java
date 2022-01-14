package eu.darkbot.api.config.types;

import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.items.SelectableItem;

import java.util.Optional;

/**
 * Predefined settings for {@link Npc} customized by user.
 */
public interface NpcInfo {

    /**
     * @return whether to kill the npc or not.
     */
    boolean getShouldKill();

    /**
     * Sets whether to kill the npc or not.
     * @param shouldKill whether to kill the npc or not
     */
    void setShouldKill(boolean shouldKill);

    /**
     * @return How important this box is to the user, the lower number is more important
     */
    int getPriority();

    /**
     * Sets how important this npc is, the lower number is more important
     * @param priority what priority to assign to this npc
     */
    void setPriority(int priority);

    /**
     * @return How far away the user wants to stand from this npc, in in-game distance units
     */
    double getRadius();

    /**
     * Sets how far away the ship will stand from this npc, in in-game distance units
     * @param radius what to set the radius to
     */
    void setRadius(double radius);

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
