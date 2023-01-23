package eu.darkbot.util;


/**
 * Util to make timing easier.
 * <br>
 * The naming is modeled after the idea of a bomb timer. You can prime (activate) it with
 * a certain fuse, and it defuses after that time.
 */
public final class Timer {
    private long time;
    private final long defaultFuse;
    private final long randomRange;

    /**
     * Get a default instance of timer, with no default fuse
     *
     * @return A new timer with no default fuse
     */
    public static Timer get() {
        return new Timer(-1, -1);
    }

    /**
     * Get an instance of timer, with a specified default fuse
     *
     * @param defaultFuse the default fuse time
     * @return A new timer with a specified fuse
     * @throws IllegalArgumentException if defaultFuse is negative or 0
     */
    public static Timer get(long defaultFuse) {
        if (defaultFuse <= 0)
            throw new IllegalArgumentException("defaultFuse must be greater than 0");
        return new Timer(defaultFuse, -1);
    }

    /**
     * Get a default instance of timer, with specified additional random range fuse.
     * -> time = currentTime + fuse + random() * randomRange
     *
     * @param randomRange range of additional random fuse
     * @return A new timer with a specified random range
     * @throws IllegalArgumentException if randomRange is negative or 0
     */
    public static Timer getRandom(long randomRange) {
        if (randomRange <= 0)
            throw new IllegalArgumentException("randomRange must be greater than 0");

        return new Timer(-1, randomRange);
    }

    public static Timer getRandom(long defaultFuse, long randomRange) {
        if (defaultFuse <= 0)
            throw new IllegalArgumentException("defaultFuse must be greater than 0");
        if (randomRange <= 0)
            throw new IllegalArgumentException("randomRange must be greater than 0");

        return new Timer(defaultFuse, randomRange);
    }

    private Timer(long defaultFuse, long randomRange) {
        this.defaultFuse = defaultFuse;
        this.randomRange = randomRange;
    }

    /**
     * Set the timer to active with a certain fuse time, only if it is not already active.
     *
     * @param fuse the time it will take for timer to activate
     * @return true if timer was defused, false if timer was already active
     */
    public boolean tryActivate(long fuse) {
        if (isActive()) return false;
        setTime(fuse);
        return true;
    }

    /**
     * Set the timer to active with the default fuse time, only if it is not already active.
     *
     * @return true if timer was defused, false if timer was already active
     * @throws UnsupportedOperationException if the timer has no default fuse
     */
    public boolean tryActivate() {
        if (defaultFuse <= 0)
            throw new UnsupportedOperationException("A timer without default fuse must specify one when calling");
        return this.tryActivate(defaultFuse);
    }

    /**
     * Set the timer to active with a certain fuse time.
     *
     * @param fuse the time it will take for timer to activate
     * @return true if the timer was already defused, false otherwise
     */
    public boolean activate(long fuse) {
        boolean isInactive = isInactive();
        setTime(fuse);
        return isInactive;
    }

    /**
     * Set the timer to active with the default fuse time.
     *
     * @return true if the timer was already defused, false otherwise
     * @throws UnsupportedOperationException if the timer has no default fuse
     */
    public boolean activate() {
        if (defaultFuse <= 0)
            throw new UnsupportedOperationException("A timer without default fuse must specify one when calling");
        return this.activate(defaultFuse);
    }

    /**
     * Check if the timer is active (in cooldown)
     * @return true if the timer is ongoing, false otherwise
     */
    public boolean isActive() {
        return time >= System.currentTimeMillis();
    }

    /**
     * Check if the timer has already passed (not in cooldown)
     * @return true if the timer is over, false otherwise
     */
    public boolean isInactive() {
        return time < System.currentTimeMillis();
    }

    /**
     * If timer is set to any value, can be reset with {@link #disarm()}
     * @return true if timer is armed, false otherwise
     */
    public boolean isArmed() {
        return time > 0;
    }

    /**
     * Trying to disarm timer only when is armed and is inactive
     *
     * @return true if timer was successfully disarmed, false otherwise
     */
    public boolean tryDisarm() {
        if (isArmed() && isInactive()) {
            disarm();
            return true;
        }
        return false;
    }

    /**
     * Reset the time completely to 0
     */
    public void disarm() {
        time = 0;
    }

    /**
     * Get the remaining time until the timer runs out
     * @return amount of remaining time in milliseconds. Negative if the timer is already over.
     */
    public long getRemainingFuse() {
        return time - System.currentTimeMillis();
    }

    private void setTime(long fuse) {
        long random = randomRange <= 0 ? 0 : (long) (Math.random() * randomRange);
        this.time = System.currentTimeMillis() + fuse + random;
    }
}
