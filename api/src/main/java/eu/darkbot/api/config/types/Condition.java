package eu.darkbot.api.config.types;

import eu.darkbot.api.PluginAPI;
import org.jetbrains.annotations.NotNull;

public interface Condition {

    @NotNull Result get(PluginAPI api);

    enum Result {
        ALLOW, DENY, ABSTAIN;

        public static Result fromBoolean(Boolean state) {
            return state == null ? ABSTAIN : state ? ALLOW : DENY;
        }

        public boolean toBoolean() {
            return !this.equals(DENY);
        }

        public boolean allows() {
            return this == ALLOW;
        }

        public boolean denies() {
            return this == DENY;
        }

        public boolean abstains() {
            return this == ABSTAIN;
        }

        public boolean hasResult() {
            return this != ABSTAIN;
        }
    }

}
