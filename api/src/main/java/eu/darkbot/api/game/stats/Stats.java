package eu.darkbot.api.game.stats;

import eu.darkbot.api.managers.StatsAPI;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Stats {

    public enum General implements BotKey {
        CREDITS,
        URIDIUM,
        EXPERIENCE,
        HONOR,
        CARGO,
        MAX_CARGO,
        NOVA_ENERGY,
        TELEPORT_BONUS_AMOUNT
    }

    public enum Bot implements BotKey {
        PING,
        TICK_TIME,
        MEMORY,
        CPU,
        RUNTIME
    }

    public enum BootyKey implements BotKey {
        GREEN,
        BLUE,
        RED,
        SILVER,
        APOCALYPSE,
        PROMETHEUS,
        OBSIDIAN_MICROCHIP,
        BLACK_LIGHT_CODE,
        BLACK_LIGHT_DECODER,
        PROSPEROUS_FRAGMENT,
        ASTRAL,
        ASTRAL_SUPREME,
        EMPYRIAN,
        LUCENT,
        PERSEUS
    }

    private interface BotKey extends StatsAPI.Key {
        @Override
        default String namespace() {
            return null;
        }

        @Override
        default String category() {
            return getClass().getSimpleName();
        }
    }

}
