package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.util.List;
import java.util.Locale;

/**
 * Provides access to data for the eternal blacklight galaxy gate in-game.
 * TODO: add a generic interface for both eternal gate APIs extended by both
 */
public interface EternalBlacklightGateAPI extends API.Singleton {
    /**
     * @return true if EBG event is enabled, false otherwise
     */
    boolean isEventEnabled();
    /**
     * Making the gate appear on the map requires the use of one CPU (acts as a key).
     *
     * @return Amount of CPUs available.
     */
    int getCpuCount();

    /**
     * Booster points can be spent on boosters.
     * @see #getActiveBoosters()
     * @see #getBoosterOptions()
     *
     * @return Amount of booster points available
     */
    int getBoosterPoints();

    /**
     * @return The current wave displayed in-game
     */
    int getCurrentWave();

    /**
     * @return The furthest achieved wave displayed in-game
     */
    int getFurthestWave();

    /**
     * Selects provided boosters, booster have to be an instance from {@link #getBoosterOptions()}!<br>
     * Call this method every tick, method checks internally {@link #getBoosterPoints()}
     *
     * @param booster the booster to select from {@link #getBoosterOptions()}
     * @return true if is in process of the selection and {@link #getBoosterPoints()} > 0, false otherwise
     */
    boolean selectBooster(Booster booster);

    /**
     * The list of currently active boosters affecting the
     * ship if inside the eternal blacklight galaxy gate.
     *
     * @return The list of all currently active boosters
     */
    List<? extends Booster> getActiveBoosters();

    /**
     * The list of booster options to pick from, by spending booster points.
     * Once picked they will get added up to your active boosters.
     *
     * @see #getBoosterPoints()
     * @see #getActiveBoosters()
     *
     * @return The list of available options to exchange
     */
    List<? extends Booster> getBoosterOptions();

    UserRank getOwnRank();
    List<? extends UserRank> getLeaderboard();

    /**
     * Booster for eternal black light
     */
    interface Booster {
        /**
         * @return Percentage of boost in plain number. 5% = 5
         */
        int getPercentage();

        /**
         * The category is an in-game id, usually fully capitalized with underscores.
         *
         * @return Booster category id
         */
        String getCategory();

        /**
         * @return {@link Category} of the booster
         */
        Category getCategoryType();
    }

    interface UserRank {
        int getRank();
        int getWave();

        String getUsername();
        String getUpdateTimeText();
    }

    enum Category {
        ABILITY_COOLDOWN_TIME,
        DAMAGE,
        DAMAGE_LASER,
        DAMAGE_ROCKETS,
        HITCHANCE_LASER,
        HITPOINTS,
        SHIELD,
        SPEED;

        private static final Category[] VALUES = values();

        private final String categoryName;

        Category() {
            this.categoryName = name().toLowerCase(Locale.ROOT);
        }

        public static Category of(String category) {
            for (Category value : VALUES) {
                if (value.categoryName.equals(category))
                    return value;
            }
            return null;
        }
    }
}
