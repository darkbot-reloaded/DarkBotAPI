package eu.darkbot.api.managers;

import eu.darkbot.api.API;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides access to in-game booster data (eg: active boosters or time remaining).
 */
public interface BoosterAPI extends API.Singleton {
    /**
     * @return The {@code List} of all Boosters currently active on ship
     */
    List<? extends Booster> getBoosters();

    /**
     * In-game booster representation, includes type of boost, amount &amp; remaining time.
     * <br>
     * Keep in mind one booster may have many sub-boosters, some may last longer
     * and some may last shorter.
     * <br>
     * The sub-boosters are not exposed in the API, instead the Booster is responsible
     * for representing the whole booster, amount will be the sum of all boosters and
     * remaining time will be the shortest of the sub-boosters.
     */
    interface Booster {
        /**
         * Amount of boost in percentage, +10% = 0.1
         * @return current amount of boost
         */
        double getAmount();

        /**
         * Remaining time until the booster expires, in seconds.
         * <br>
         * Keep in mind that the time running out doesn't necessarily mean {@link #getAmount} drops to 0.
         * <br>
         * Example: amount could be 15% (combination of 10% + 5%) and remaining time be 10s.
         * <br>    After the 10 seconds, it could be that amount is 10% &amp; remaining time is hours.
         *
         * @return time until current booster expires, in seconds.
         */
        double getRemainingTime();

        /**
         * In-game displayed name for this booster type
         * @return in-game name
         */
        String getName();

        /**
         * Short name, typically about 3 characters, for this booster type
         * @return small name for this booster type
         * @see Type#getSmall(String)
         */
        default String getSmall() {
            return getType().getSmall(getCategory());
        }

        /**
         * Color typically used for this booster type
         * @return color for this booster type
         * @see Type#getColor()
         */
        default Color getColor() {
            return getType().getColor();
        }

        /**
         * Type of booster this is categorized as
         * @return type of booster
         */
        default Type getType() {
            return Type.of(getCategory());
        }

        /**
         * The string version of booster category, prefer using {@link #getType()} instead
         *
         * @return The string version of category type of this booster
         * @see #getType()
         */
        String getCategory();
    }

    /**
     * The types of all boosters available
     */
    enum Type {
        ABILITY_COOLDOWN_TIME   ("CD"    , new Color(0xFFC000)),
        DAMAGE                  ("DMG"   , new Color(0xFD0400)),
        EXPERIENCE_POINTS       ("EXP"   , new Color(0xF77800)),
        HITPOINTS               ("HP"    , new Color(0x049104)),
        HONOUR_POINTS           ("HON"   , new Color(0xFF8080)),
        REPAIR                  ("REP"   , new Color(0xA93DE4)),
        COLLECT_RESOURCES       ("RES"   , new Color(0xEAD215)),
        SHIELD                  ("SHD"   , new Color(0x69EBFF)),
        SHIELD_REGENERATION     ("SHDR"  , new Color(0x3B64BD)),
        AMOUNT                  ("AMT"   , new Color(0xFFCC00)),
        COLLECT_RESOURCES_NEWBIE("DBL"   , new Color(0xFFF3CF)),
        CHANCE                  ("CHN"   , new Color(0xFFD100)),
        EVENT_AMOUNT            ("EVT AM", new Color(0x05B6E3)),
        EVENT_CHANCE            ("EVT CH", new Color(0x00C6EE)),
        SPECIAL_AMOUNT          ("SP AM" , new Color(0xFFFFFF)),
        UNKNOWN                 ("?"     , new Color(0x808080)) {
            @Override
            String getSmall(String category) {
                return Arrays.stream(category.split("_"))
                        .map(str -> str.length() <= 3 ? str : str.substring(0, 3))
                        .collect(Collectors.joining(" "));
            }
        };

        private final String small;
        private final Color color;

        Type(String small, Color color) {
            this.small = small;
            this.color = color;
        }

        String getSmall(String category) {
            return this.small;
        }

        Color getColor() {
            return this.color;
        }

        private static Type of(String category) {
            for (Type cat : Type.values()) {
                if (cat.name().equalsIgnoreCase(category)) return cat;
            }
            return UNKNOWN;
        }
    }
}
