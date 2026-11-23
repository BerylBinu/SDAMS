package sdams.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Checks user input before it is saved. Shared by the whole team.
 * Each method returns true if the input is OK.
 */
public class Validator {

    /** True if the text is null or only spaces. */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /** True if the text contains "|", which would break our text files. */
    public static boolean containsDelimiter(String text) {
        return text != null && text.contains("|");
    }

    /** Letters, spaces and . ' @ / - only. Allows names like "Ali bin Abu" or "Raj a/l Kumar". */
    public static boolean isValidName(String name) {
        return !isEmpty(name) && name.trim().matches("[A-Za-z .'@/-]+");
    }

    /** 3 to 20 letters, digits or underscores. No spaces. */
    public static boolean isValidUsername(String username) {
        return !isEmpty(username) && username.matches("[A-Za-z0-9_]{3,20}");
    }

    /** At least 6 characters and no "|". */
    public static boolean isValidPassword(String password) {
        return !isEmpty(password) && password.length() >= 6 && !containsDelimiter(password);
    }

    /** Simple email check: something@something.something */
    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && email.matches("[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    }

    /** Malaysian mobile number: starts with 01, 10 or 11 digits, no dashes. e.g. 0123456789 */
    public static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && phone.matches("01[0-9]{8,9}");
    }

    /** True if the text is a number greater than 0, e.g. a payment amount. */
    public static boolean isPositiveNumber(String text) {
        try {
            return Double.parseDouble(text) > 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    /** True if the text is a real date in yyyy-MM-dd format, e.g. 2026-12-01. */
    public static boolean isValidDate(String text) {
        try {
            LocalDate.parse(text);
            return true;
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Checks all the fields of a user form at once.
     * Returns the first error message found, or null if everything is OK.
     * The GUI shows the message in a JOptionPane.
     */
    public static String validateUser(String name, String username, String password,
                                      String email, String phone) {
        if (!isValidName(name)) {
            return "Name can only contain letters, spaces and . ' @ / -";
        }
        if (!isValidUsername(username)) {
            return "Username must be 3-20 letters, digits or _ (no spaces).";
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters and cannot contain |";
        }
        if (!isValidEmail(email)) {
            return "Please enter a valid email, e.g. ali@sda.com";
        }
        if (!isValidPhone(phone)) {
            return "Phone must be 10-11 digits starting with 01, e.g. 0123456789";
        }
        return null;
    }
}
