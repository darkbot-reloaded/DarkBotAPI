package eu.darkbot.api.config.util;

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

}
