package eu.darkbot.api.game.entities;

/**
 * In-game dropped mine entity
 */
public interface Mine extends Entity {

    /**
     * @return mine type id
     */
    int getTypeId();

    /**
     * @return The mine type of this mine entity
     */
    default Type getType() {
        return Type.of(getTypeId());
    }

    /**
     * Represents {@link Mine} types.
     */
    enum Type {
        UNKNOWN,
        // 18 -> bigger standard mine? with full pilot bio?
        STANDARD(1, 10, 11, 18),
        EMP(2),
        ANTI_SHIELD(3),
        DIRECT_DAMAGE(4),
        PIRATE(5),
        TITANIC(6), //emperor sibeleon's mine?
        SLOWDOWN(7),
        INFECTION(17),
        MEGA(19), //pet's mega mine
        CURCUBITOR(20),
        ICE(21), // ICE_MINE
        AGATUS_LURE(26);

        private static final Type[] VALUES = values();

        private final int[] ids;

        Type(int... ids) {
            this.ids = ids;
        }

        private static Type of(int typeId) {
            for (Type type : VALUES) {
                for (int id : type.ids) {
                    if (id == typeId) return type;
                }
            }

            return UNKNOWN;
        }
    }
}
