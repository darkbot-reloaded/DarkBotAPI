package eu.darkbot.api.config.types;

/**
 * Represents a range between a min and a max percentage.
 * Percentage is represented between 0 and 1.
 */
public interface PercentRange {

    /**
     * @return the minimum value
     */
    double getMin();

    /**
     * @return the maximum value
     */
    double getMax();

    /**
     * Create a percent range with a min &amp; max
     * @param min the minimum value
     * @param max the maximum value
     * @return a percent range with the given min &amp; max
     */
    static PercentRange of(double min, double max) {
        return new PercentRangeImpl(min, max);
    }

    class PercentRangeImpl implements PercentRange {
        private final double min;
        private final double max;

        public PercentRangeImpl(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public double getMin() {
            return min;
        }

        @Override
        public double getMax() {
            return max;
        }
    }

}
