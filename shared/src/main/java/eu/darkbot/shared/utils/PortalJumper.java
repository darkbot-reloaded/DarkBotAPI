package eu.darkbot.shared.utils;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.game.other.Location;
import eu.darkbot.api.managers.BotAPI;
import eu.darkbot.api.managers.GroupAPI;
import eu.darkbot.api.managers.MovementAPI;
import eu.darkbot.api.utils.Inject;
import eu.darkbot.util.Timer;
import lombok.NonNull;

public class PortalJumper {

    protected final BotAPI bot;
    protected final MovementAPI movement;
    protected final GroupAPI group;

    protected Portal last;
    @Deprecated
    protected long nextMoveClick;

    protected Timer nextTravelMove = Timer.get();
    protected Timer tryingToJumpSince = Timer.get(90_000);
    protected Timer lastJumpAttempt = Timer.get(2_500);

    @Inject
    public PortalJumper(BotAPI bot, MovementAPI movement, GroupAPI group) {
        this.bot = bot;
        this.movement = movement;
        this.group = group;
    }

    public PortalJumper(PluginAPI api) {
        this(api.requireAPI(BotAPI.class), api.requireAPI(MovementAPI.class), api.requireAPI(GroupAPI.class));
    }

    public void reset() {
        this.last = null;
        this.tryingToJumpSince.disarm();
    }

    public void travelAndJump(@NonNull Portal target) {
        if (travel(target)) jump(target);
    }

    public boolean travel(@NonNull Portal target) {
        // if location is not initialized, cannot travel there
        if (!target.getLocationInfo().isInitialized()) return true;
        double leniency = Math.min(200 + movement.getClosestDistance(target), 600);

        if (movement.getDestination().distanceTo(target) > leniency
                || (movement.getCurrentLocation().distanceTo(target) > leniency
                && nextTravelMove.isInactive())) {
            movement.moveTo(Location.of(target, Math.random() * Math.PI * 2, Math.random() * 200));
            nextTravelMove.activate(2_000); // move to a little different location near to port every 2 seconds

        } else if (movement.getCurrentLocation().distanceTo(target) <= leniency
                && nextTravelMove.tryActivate(target.isJumping() ? 8_000 : 5_000)) {
            movement.moveTo(Location.of(target, Math.random() * Math.PI * 2, Math.random() * 200));
        }

        return movement.getCurrentLocation().distanceTo(target) <= leniency
                && (!movement.isMoving() || target.isSelectable());
    }

    public void jump(@NonNull Portal target) {
        // Low & hades, wait for group before trying to jump
        // This prevents the J key being written while typing out player names for invites
        int minGroupSize = target.getTargetMap().map(GameMap::getId)
                .map(id -> id == 200 ? 3 // LoW
                        : id == 203 ? 4  // Hades
                        : 0).orElse(0);

        if (minGroupSize > 0 && (!group.hasGroup() || group.getSize() < minGroupSize))
            return;

        if (lastJumpAttempt.activate()) tryingToJumpSince.activate();

        movement.jumpPortal(target);

        if (!target.equals(last)) {
            last = target;
            nextTravelMove.activate(10_000); // first jump attempt, it may take a while
            tryingToJumpSince.activate();
        }

        if (tryingToJumpSince.tryDisarm()) {
            System.out.println("Triggering refresh: jumping portal took too long");
            bot.handleRefresh();
        }
    }
}
