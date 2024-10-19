package eu.darkbot.api.game.enums;

import lombok.AllArgsConstructor;

/**
 * In-game type of {@link eu.darkbot.api.game.entities.Portal}.
 */
@AllArgsConstructor
public enum PortalType {
    UNKNOWN(-1),

    STANDARD(1),
    TUTORIAL(55),
    GROUP_GATE(34),
    PIRATE(51), PIRATE_BROKEN(52),

    //GGs
    ALPHA(2), BETA(3), GAMMA(4), DELTA(5),
    EPSILON(53), ZETA(54), KAPPA(70), LAMBDA(71),
    KRONOS(72), HADES(74), KUIPER(82), QUARANTINE(84),

    //Event
    BIRTHDAY(11), SIXTH_BIRTHDAY(15), HIGHSCORE(14),
    INVASION_1(41), INVASION_2(42), INVASION_3(43),
    SOCCER_LEFT(61), SOCCER_RIGHT(62),
    PET_ATTACK_LOW(79), PET_ATTACK_HIGH(79),
    PAYLOAD(85), DOMINATION(75),
    GOP(24), GOP_EASY(235),  //Gauntlet of Plutus
    TOT(81), ZETA_FROST(83), //Tunnel of Terror

    ROUGE_LITE(86),
    ROUGE_LITE_WEAPON(87), ROUGE_LITE_WEAPON_BRUTAL(88),
    ROUGE_LITE_GENERATOR(89), ROUGE_LITE_GENERATOR_BRUTAL(90),
    ROUGE_LITE_AMMUNITION(91), ROUGE_LITE_AMMUNITION_BRUTAL(92),
    ROUGE_LITE_BOOSTER(93), ROUGE_LITE_BOOSTER_BRUTAL(94),
    ROUGE_LITE_MODULE(95), ROUGE_LITE_MODULE_BRUTAL(96),
    ROUGE_LITE_RESOURCE(97), ROUGE_LITE_RESOURCE_BRUTAL(98),
    ROUGE_LITE_HP_RECOVER(99),
    ROUGE_LITE_FINAL_BOSS(100),

    //?
    INVISIBLE(18), //4-5 center portal?
    BREACH(22),
    LOW_LEFT(77),
    HIGH_RIGHT(78);

    private static final PortalType[] VALUES = values();

    private final int id;

    /**
     * Get a portal type by its ID
     *
     * @param typeId the in-game id of the portal type
     * @return The portal type with the corresponding id, or null if not found
     */
    public static PortalType of(int typeId) {
        for (PortalType type : VALUES) {
            if (type.id == typeId) return type;
        }
        return UNKNOWN;
    }

    /**
     * @return the id of this portal in-game
     */
    public int getId() {
        return id;
    }
}
