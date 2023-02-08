package eu.darkbot.api.extensions;

import lombok.Getter;

@Getter
public class FeatureException extends RuntimeException {
    private static final long serialVersionUID = -4649810460744079421L;

    private final String description;
    private final IssueHandler.Level level;

    /**
     * Creates critical {@link FeatureException}, adds the message and description to issues set.
     * This exception level will prevent any feature for further ticking, working.
     * User need to reload plugins to make feature work after throwing this exception.
     *
     * @param message     to be shown next to feature in plugins tab
     * @param description description of the message
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException critical(String message, String description) {
        return new FeatureException(message, description, IssueHandler.Level.ERROR);
    }

    /**
     * Creates critical {@link FeatureException}, adds the message and description to issues set.
     * This exception level will prevent any feature for further ticking, working.
     * User need to reload plugins to make feature work after throwing this exception.
     *
     * @param message to be shown next to feature in plugins tab
     * @param cause   cause which will be converted to description
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException critical(String message, Throwable cause) {
        return new FeatureException(message, IssueHandler.Level.ERROR, cause);
    }

    /**
     * Creates warning {@link FeatureException}, adds the message and description to issues set.
     * You can inform user about invalid state of you feature,
     * throwing this exception will add message next to the feature in plugins tab, however feature will be kept ticking.
     *
     * @param message     to be shown next to feature in plugins tab
     * @param description description of the message
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException warning(String message, String description) {
        return new FeatureException(message, description, IssueHandler.Level.WARNING);
    }

    /**
     * Creates warning {@link FeatureException}, adds the message and description to issues set.
     * You can inform user about invalid state of you feature,
     * throwing this exception will add message next to the feature in plugins tab, however feature will be kept ticking.
     *
     * @param message to be shown next to feature in plugins tab
     * @param cause   cause which will be converted to description
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException warning(String message, Throwable cause) {
        return new FeatureException(message, IssueHandler.Level.WARNING, cause);
    }

    /**
     * @return {@link FeatureException} if is present in cause-tree, otherwise null
     */
    public static FeatureException find(Throwable e) {
        if (e == null || e instanceof FeatureException) {
            return (FeatureException) e;
        }
        return find(e.getCause());
    }

    private FeatureException(String message, String description, IssueHandler.Level level) {
        super(message);
        this.level = level;
        this.description = description;
    }

    private FeatureException(String message, IssueHandler.Level level, Throwable cause) {
        super(message, cause);
        this.level = level;
        this.description = null;
    }
}
