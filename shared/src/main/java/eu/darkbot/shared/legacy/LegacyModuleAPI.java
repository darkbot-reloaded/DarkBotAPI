package eu.darkbot.shared.legacy;

import eu.darkbot.api.API;
import eu.darkbot.api.extensions.Module;

/**
 * This API exists to close the gap between legacy written modules that have not yet
 * been adapted to work with the new API, without adding a need for a strong dependency
 * for API users.
 *
 * Essentially what this means is you can get instances of the legacy modules,
 * without needing to import the legacy bot package.
 *
 * As modules develop and migrate over to {@link eu.darkbot.shared} they will be re-routed to
 * the new implementations.
 */
@Deprecated
public interface LegacyModuleAPI extends API.Singleton {

    Module getDisconnectModule(Long pauseTime, String reason);

}
