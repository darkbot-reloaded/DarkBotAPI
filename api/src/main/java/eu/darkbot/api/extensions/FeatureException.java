package eu.darkbot.api.extensions;

/**
 * Throwing this exception in a constructor of Type annotated with {@link Feature}
 * will prevent feature from further loading &amp; will show an issue next to feature in plugins tab
 * <p>
 * Also, you can throw this exception in [Module, Behavior, Task] ...tick() methods to achieve similar gaol
 */
public class FeatureException extends RuntimeException {
    private static final long serialVersionUID = -1057251719252896909L;

    private final String description;

    /**
     * @param message the message to be shown as issue
     * @param cause   cause of the exception, will be shown as description of the issue
     */
    public FeatureException(String message, Throwable cause) {
        this(message, (String) null);
        initCause(cause);
    }

    /**
     * @param message     the message to be shown as issue
     * @param description description of the issue
     */
    public FeatureException(String message, String description) {
        super(message);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
