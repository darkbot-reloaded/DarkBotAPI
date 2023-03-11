package eu.darkbot.api.extensions;

import lombok.Getter;

/**
 * Implements a generic exception that {@link Feature}s can throw during their tick methods
 * to signal an issue (error, warning or info) that will be shown to the user in the plugins tab.
 * Critical feature exceptions will additionally make the feature become disabled until a plugin reload is triggered.
 *
 * @since 0.7.1
 */
@Getter
public final class FeatureException extends RuntimeException {
    private static final long serialVersionUID = -4649810460744079421L;

    private final String description;
    private final IssueHandler.Level level;

    /**
     * Creates critical {@link FeatureException}, adds the message and description to issues set.
     * This exception level will prevent any feature for further ticking.
     * User will need to reload plugins to make feature work after throwing this exception.
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
     * This exception level will prevent any feature for further ticking.
     * User will need to reload plugins to make feature work after throwing this exception.
     *
     * @param message to be shown next to feature in plugins tab
     * @param cause   cause which will be converted to description
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException critical(String message, Throwable cause) {
        return new FeatureException(message, cause, IssueHandler.Level.ERROR);
    }

    /**
     * Creates warning {@link FeatureException}, adds the message and description to issues set.
     * You can inform user about invalid state of you feature, throwing this exception will
     * add message next to the feature in plugins tab, however feature will be kept ticking.
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
     * You can inform user about invalid state of you feature, throwing this exception will
     * add message next to the feature in plugins tab, however feature will be kept ticking.
     *
     * @param message to be shown next to feature in plugins tab
     * @param cause   cause which will be converted to description
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException warning(String message, Throwable cause) {
        return new FeatureException(message, cause, IssueHandler.Level.WARNING);
    }

    /**
     * Creates warning {@link FeatureException}, adds the message and description to issues set.
     * You can inform user about invalid state of you feature, throwing this exception will
     * add message next to the feature in plugins tab, however feature will be kept ticking.
     *
     * @param message     to be shown next to feature in plugins tab
     * @param description description of the message
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException info(String message, String description) {
        return new FeatureException(message, description, IssueHandler.Level.INFO);
    }

    /**
     * Creates warning {@link FeatureException}, adds the message and description to issues set.
     * You can inform user about invalid state of you feature, throwing this exception will
     * add message next to the feature in plugins tab, however feature will be kept ticking.
     *
     * @param message to be shown next to feature in plugins tab
     * @param cause   cause which will be converted to description
     * @return new instance of {@link FeatureException}
     * @see IssueHandler
     */
    public static FeatureException info(String message, Throwable cause) {
        return new FeatureException(message, cause, IssueHandler.Level.INFO);
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

    private FeatureException(String message, Throwable cause, IssueHandler.Level level) {
        super(message, cause);
        this.level = level;
        this.description = null;
    }

    public boolean isCritical() {
        return level == IssueHandler.Level.ERROR;
    }
}
