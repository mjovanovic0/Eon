package hr.eon.client.window.validation;

/**
 * Validation utility class contains various values for validate user input
 * through GUI.
 * 
 * @author mjovanovic
 * @since 0.0.2
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    /**
     * Constants for LoginWindow validation
     */
    public static final int LOGIN_USERNAME_MIN_LENGTH = 4;
    public static final int LOGIN_USERNAME_MAX_LENGTH = 20;
    public static final int LOGIN_PASSWORD_MIN_LENGTH = 4;
    public static final int LOGIN_PASSWORD_MAX_LENGTH = 20;

    public static boolean assertInRange( String key, int min, int max ) {
        return key.length() > min && key.length() < max ? true : false;
    }
}
