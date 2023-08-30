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
        NOVA_ENERGY
    }

    public enum Bot implements BotKey {
        PING,
        TICK_TIME,
        MEMORY,
        CPU,
        RUNTIME
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
