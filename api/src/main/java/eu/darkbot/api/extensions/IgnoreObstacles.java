package eu.darkbot.api.extensions;

import eu.darkbot.api.managers.MovementAPI;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link MovementAPI} will ignore in pathfinding selected obstacle types
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IgnoreObstacles {

    Type[] ignore();

    enum Type {
        MINES,
        RADIATION,
        AVOID_ZONES,
        BATTLE_STATIONS
    }
}
