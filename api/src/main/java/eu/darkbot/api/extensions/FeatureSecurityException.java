package eu.darkbot.api.extensions;

/**
 * Throw this exception in {@code Feature} constructor to create custom issue in plugins tab
 */
public class FeatureSecurityException extends SecurityException {
    private static final long serialVersionUID = -1057251719252896909L;

    private final String description;

    public FeatureSecurityException(String message, String description) {
        super(message);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
