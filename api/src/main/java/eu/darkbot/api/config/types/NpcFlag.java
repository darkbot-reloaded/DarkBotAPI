package eu.darkbot.api.config.types;

import eu.darkbot.api.config.annotations.Configuration;

@Configuration("config.loot.npc_table.extra")
public enum NpcFlag {
    NO_CIRCLE,
    IGNORE_OWNERSHIP,
    IGNORE_ATTACKED,
    IGNORE_BOXES,
    AGGRESSIVE_FOLLOW,
    PASSIVE,
    ATTACK_SECOND,
    LEECH_ONLY,
    NO_SAB,
    USE_RSB,
    PET_LOCATOR
}
