package app;

import java.util.regex.Pattern;

/**
 * The EmailValidator class provides functionality to validate email addresses
 * based on a regular expression pattern. It ensures that the given email
 * conforms to a general email format.
 */
public class EmailValidiator {

    // Regular expression pattern for validating email addresses
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    /**
     * Validates whether the provided email address is in a valid format.
     *
     * @param email The email address to validate.
     * @return {@code true} if the email is in a valid format, {@code false} otherwise.
     */
    public static boolean isValidEmail(String email) {
        // Check if email is not null and matches the defined pattern
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
