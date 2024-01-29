package eu.darkbot.api.game.other;

import eu.darkbot.api.managers.HeroAPI;

public interface EntityInfo {

    /**
     * @return true if ship is considered as enemy for {@link HeroAPI}.
     * <p>
     * Is <b>not</b> in ally clan, group?
     * Is from other faction or enemy clan.
     */
    boolean isEnemy();

    /**
     * @return {@link Faction}
     */
    Faction getFaction();

    /**
     * @return ship username.
     */
    String getUsername();

    /**
     * @return ship's clan tag
     */
    String getClanTag();

    /**
     * @return clan id
     */
    int getClanId();

    /**
     * @return {@link Diplomacy}
     */
    Diplomacy getClanDiplomacy();

    /**
     * Represents in-game fractions.
     */
    enum Faction {
        NONE,
        MMO,
        EIC,
        VRU,
        SATURN;

        private static final Faction[] VALUES = values();

        public static Faction of(int factionId) {
            if (factionId >= VALUES.length || factionId < 0) return NONE;
            return VALUES[factionId];
        }
    }

    /**
     * Represents in-game clans diplomacy types.
     */
    enum Diplomacy {
        NONE,
        ALLIED,
        NOT_ATTACK_PACT,
        WAR;

        private static final Diplomacy[] VALUES = values();

        public static Diplomacy of(int diplomacyId) {
            if (diplomacyId >= VALUES.length || diplomacyId < 0) return NONE;
            return VALUES[diplomacyId];
        }
    }
}
