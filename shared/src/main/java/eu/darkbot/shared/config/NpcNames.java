package eu.darkbot.shared.config;

import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.config.annotations.Dropdown;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.managers.ConfigAPI;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A dropdown options implementation that gives simplified NPC names as options.
 * Example:
 * <pre>
 * <code>
 * class Config {
 *     {@literal @}Dropdown(options = NpcNames.class)
 *     public String NPC_NAME = "";
 *     {@literal @}Dropdown(options = NpcNames.class, multi=true)
 *     public Set<String> NPC_NAMES = new HashSet<>();
 * }
 * class Feature {
 *     private Config conf; // Assume populated
 *
 *     boolean isSelectedInConfig(Npc npc) {
 *         String simpleName = NpcNames.getSimpleName(npc);
 *         return conf.NPC_NAME.equals(simpleName) || conf.NPC_NAMES.contains(simpleName);
 *     }
 * }
 * </code>
 * </pre>
 * In this example, the feature has a config with two dropdowns, one for a single npc, and another for multi-selection.
 * The isSelectedInConfig function returns true if the npc is selected anywhere in the config.
 */
public class NpcNames implements Dropdown.Options<String> {

    private final ConfigSetting<Map<String, ?>> npcInfos;
    private boolean dirty = true;
    private List<String> options;

    public NpcNames(ConfigAPI configAPI) {
        npcInfos = configAPI.requireConfig("loot.npc_infos");
        npcInfos.addListener(val -> dirty = true);
    }

    @Override
    public Collection<String> options() {
        if (dirty) {
            options = npcInfos.getValue().keySet().stream()
                    .map(NpcNames::simplifyName)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            dirty = false;
        }
        return options;
    }

    public static String simplifyName(String name) {
        if (!name.matches("^\\D+\\d{1,3}$")) return name;
        return name.replaceAll("\\d{1,3}$", " *");
    }

    public static String getSimpleName(Npc npc) {
        if (npc == null) return null;
        return simplifyName(npc.getEntityInfo().getUsername());
    }

}
