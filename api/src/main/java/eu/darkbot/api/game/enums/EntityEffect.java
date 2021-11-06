package eu.darkbot.api.game.enums;

/**
 * Represents in-game {@link eu.darkbot.api.game.entities.Entity}'s effects.
 */
public enum EntityEffect {
    UNDEFINED(-1),
    LOCATOR(1),
    PET_SPAWN(2),
    ENERGY_LEECH(11),
    NPC_ISH(16),
    DRAW_FIRE(36),
    STICKY_BOMB(56),
    POLARITY_POSITIVE(65),
    POLARITY_NEGATIVE(66),
    REPAIR_BOT(76),
    ISH(84),
    INFECTION(85);

    private final int id;

    EntityEffect(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
