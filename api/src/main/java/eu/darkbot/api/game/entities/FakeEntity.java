package eu.darkbot.api.game.entities;

import eu.darkbot.api.config.types.BoxInfo;
import eu.darkbot.api.config.types.NpcInfo;
import eu.darkbot.api.game.other.Location;

/**
 * Fake entities help keeping virtual entities on the map even when they go out of visibility range.
 * It should be used to assist or predict the positions of these entities for active module.
 * Use cases: keeping mine locations to avoid pathing by them
 *            keeping box locations so the bot remembers going back for them
 */
public interface FakeEntity extends Entity {
    static boolean isFakeEntity(Entity entity) {
        return entity instanceof FakeEntity;
    }

    /**
     * Set timeout lifetime of a fake object in ms
     * @param keepAlive How long to keep the entity alive for in ms
     */
    void setTimeout(long keepAlive);

    /**
     * @param location Set the location of the fake entity
     */
    void setLocation(Location location);

    /**
     * Set the minimum distance to hero for the entity to be automatically removed
     * @param removeDistance if hero comes any closer than the distance, entity is removed
     */
    void setRemoveDistance(long removeDistance);

    /**
     * Is remove fake entity after attempt select it
     * @param remove true to remove if user attempts selection, false to keep it
     */
    void setRemoveOnSelect(boolean remove);

    /**
     * Fake box can be used to lure collector module
     */
    interface FakeBox extends Box, FakeEntity {
    }

    /**
     * Fake mine is an obstacle that avoided by the path builder
     */
    interface FakeMine extends Mine, FakeEntity {
    }

    /**
     * Fake ship can be used for players or pets
     */
    interface FakeShip extends Ship, FakeEntity {
    }

    /**
     * Fake Npc can be used to lure loot module
     */
    interface FakeNpc extends FakeShip, Npc {
    }

    interface Builder {
        Builder keepAlive(Long keepAlive);
        Builder location(Location location);
        Builder removeDistance(Long removeDistance);
        Builder removeOnSelect(boolean remove);

        FakeEntity.FakeMine mine(int typeId);
        default FakeEntity.FakeMine mine(Mine.Type type) {
            return mine(type.ordinal());
        }

        FakeEntity.FakeNpc npc(NpcInfo npcInfo);
        FakeEntity.FakeNpc npc(String npcName);

        FakeEntity.FakeBox box(BoxInfo box);
        FakeEntity.FakeBox box(String boxName);

        abstract class Impl implements Builder {
            private Long keepAlive;
            private Location location;
            private Long removeDistance;
            private boolean removeOnSelect = true;

            @Override
            public Builder keepAlive(Long keepAlive) {
                this.keepAlive = keepAlive;
                return this;
            }

            @Override
            public Builder location(Location location) {
                this.location = location;
                return this;
            }

            @Override
            public Builder removeDistance(Long removeDistance) {
                this.removeDistance = removeDistance;
                return this;
            }

            @Override
            public Builder removeOnSelect(boolean removeOnSelect) {
                this.removeOnSelect = removeOnSelect;
                return this;
            }

            protected <T extends FakeEntity> T apply(T entity) {
                if (keepAlive != null) entity.setTimeout(keepAlive);
                if (location != null) entity.setLocation(location);
                if (removeDistance != null) entity.setRemoveDistance(removeDistance);
                entity.setRemoveOnSelect(removeOnSelect);
                return entity;
            }
        }
    }
}
