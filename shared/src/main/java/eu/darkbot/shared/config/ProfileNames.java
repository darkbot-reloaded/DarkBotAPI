package eu.darkbot.shared.config;

import eu.darkbot.api.config.annotations.Dropdown;
import eu.darkbot.api.managers.ConfigAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A dropdown options implementation that gives bot config profiles as options.
 */
public class ProfileNames implements Dropdown.Options<String> {

    private final ConfigAPI configAPI;

    public ProfileNames(ConfigAPI configAPI) {
        this.configAPI = configAPI;
    }

    @Override
    public Collection<String> options() {
        return configAPI.getConfigProfiles();
    }

    @Override
    public @NotNull String getText(@Nullable String option) {
        return option == null || option.isEmpty() ? "(none)" : option;
    }

}
