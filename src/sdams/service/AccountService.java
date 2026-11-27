package sdams.service;

import java.util.ArrayList;
import sdams.model.User;
import sdams.util.FileManager;
import sdams.util.Validator;

/**
 * Shared parent of the Administrator's account services
 * (StaffService and StudentAccountService).
 * Holds the code both need, so it is written only once.
 *
 * It is abstract: you never create an AccountService itself,
 * only a StaffService or a StudentAccountService.
 */
public abstract class AccountService {

    protected static final String USERS_FILE = "users.txt";

    // Reuse the shared code that turns a users.txt line into the right subclass
    protected final UserService userService = new UserService();

    /**
     * Each subclass says which other files still use this person's ID.
     * Returns the file name, or null if none (then the person can be deleted).
     */
    protected abstract String findRecordsUsing(User user);

    // ===================== CHECKS =====================

    /**
     * Checks a user form: valid fields and a unique username.
     * excludeId: the user being updated, or null when adding.
     * Returns the first error message, or null if everything is OK.
     */
    protected String checkDetails(String name, String username, String password,
                                  String email, String phone, String excludeId) {
        String error = Validator.validateUser(name, username, password, email, phone);
        if (error != null) {
            return error;
        }
        if (isUsernameTaken(username, excludeId)) {
            return "Username '" + username.trim() + "' is already taken.";
        }
        return null;
    }

    /**
     * True if any user (any role) already has this username.
     * excludeId: the user being updated, or null when adding.
     */
    public boolean isUsernameTaken(String username, String excludeId) {
        for (User u : userService.getAllUsers()) {
            boolean sameUsername = u.getUsername().equalsIgnoreCase(username.trim());
            boolean isSelf = u.getId().equals(excludeId);
            if (sameUsername && !isSelf) {
                return true;
            }
        }
        return false;
    }

    // ===================== UPDATE / DELETE =====================

    /** Validates the new details, changes the object and saves it. Returns an error or null. */
    protected String updateAccount(User user, String name, String username, String password,
                                   String email, String phone) {
        String error = checkDetails(name, username, password, email, phone, user.getId());
        if (error != null) {
            return error;
        }

        user.setName(name.trim());
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setEmail(email.trim());
        user.setPhone(phone.trim());

        // Read all lines, replace the matching one, write all back
        ArrayList<String> lines = FileManager.readLines(USERS_FILE);
        for (int i = 0; i < lines.size(); i++) {
            if (getIdFromLine(lines.get(i)).equals(user.getId())) {
                lines.set(i, user.toFileLine());   // works for any subclass
                break;
            }
        }
        FileManager.writeLines(USERS_FILE, lines);
        return null;
    }

    /**
     * Deletes the user, unless other records still point to their ID
     * (rule 4: link by ID). Returns an error message, or null if it worked.
     */
    protected String deleteAccount(User user) {
        // Calls the subclass's version: polymorphism
        String usedIn = findRecordsUsing(user);
        if (usedIn != null) {
            return user.getName() + " cannot be deleted because they still have records in "
                    + usedIn + ".";
        }

        // Copy every line EXCEPT the one being deleted, then save the copy
        ArrayList<String> kept = new ArrayList<>();
        for (String line : FileManager.readLines(USERS_FILE)) {
            if (!getIdFromLine(line).equals(user.getId())) {
                kept.add(line);
            }
        }
        FileManager.writeLines(USERS_FILE, kept);
        return null;
    }

    // ===================== HELPERS =====================

    /** True if the keyword is found in the user's ID, name, username, email or phone. */
    protected boolean matchesKeyword(User u, String keyword) {
        if (Validator.isEmpty(keyword)) {
            return true;   // empty search shows everyone
        }
        String k = keyword.trim().toLowerCase();
        return u.getId().toLowerCase().contains(k)
                || u.getName().toLowerCase().contains(k)
                || u.getUsername().toLowerCase().contains(k)
                || u.getEmail().toLowerCase().contains(k)
                || u.getPhone().contains(k);
    }

    /** True if any line of the file has this ID at the given field position. */
    protected boolean isIdInFile(String fileName, int fieldIndex, String id) {
        for (String line : FileManager.readLines(fileName)) {
            String[] f = line.split("\\|");
            if (f.length > fieldIndex && f[fieldIndex].equals(id)) {
                return true;
            }
        }
        return false;
    }

    /** The ID is always the first field of a line. */
    protected String getIdFromLine(String line) {
        return line.split("\\|")[0];
    }
}