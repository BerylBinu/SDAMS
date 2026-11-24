package sdams.service;

import java.util.ArrayList;
import sdams.model.Administrator;
import sdams.model.Role;
import sdams.util.FileManager;
import sdams.util.Validator;

/**
 * Manager feature: Manage Administrators (add, view, search, update, delete).
 * All administrators are stored in users.txt with role ADMINISTRATOR.
 *
 * add/update/delete return an error message, or null if it worked.
 * The GUI shows the message in a JOptionPane.
 */
public class AdminService {

    private static final String USERS_FILE = "users.txt";

    // ===================== VIEW =====================

    public ArrayList<Administrator> getAllAdmins() {
        ArrayList<Administrator> admins = new ArrayList<>();

        for (String line : FileManager.readLines(USERS_FILE)) {
            String[] f = line.split("\\|");

            if (f.length >= 7 && f[6].equals(Role.ADMINISTRATOR.name())) {
                admins.add(new Administrator(f[0], f[1], f[2], f[3], f[4], f[5]));
            }
        }
        return admins;
    }

    /** Returns the administrator with this ID, or null if not found. */
    public Administrator findById(String id) {
        for (Administrator a : getAllAdmins()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    // ===================== SEARCH =====================

    /** Finds admins whose ID, name, username or email contains the keyword (not case-sensitive). */
    public ArrayList<Administrator> searchAdmins(String keyword) {
        if (Validator.isEmpty(keyword)) {
            return getAllAdmins();
        }

        String k = keyword.trim().toLowerCase();
        ArrayList<Administrator> results = new ArrayList<>();

        for (Administrator a : getAllAdmins()) {
            if (a.getId().toLowerCase().contains(k)
                    || a.getName().toLowerCase().contains(k)
                    || a.getUsername().toLowerCase().contains(k)
                    || a.getEmail().toLowerCase().contains(k)) {
                results.add(a);
            }
        }
        return results;
    }

    // ===================== ADD =====================

    public String addAdmin(String name, String username, String password,
                           String email, String phone) {
        String error = Validator.validateUser(name, username, password, email, phone);
        if (error != null) {
            return error;
        }
        if (isUsernameTaken(username, null)) {
            return "Username '" + username + "' is already taken.";
        }

        String id = FileManager.generateId(USERS_FILE, Role.ADMINISTRATOR.getIdPrefix());
        Administrator admin = new Administrator(id, name.trim(), username.trim(),
                password, email.trim(), phone.trim());

        FileManager.appendLine(USERS_FILE, admin.toFileLine());
        return null;
    }

    // ===================== UPDATE =====================

    public String updateAdmin(String id, String name, String username, String password,
                              String email, String phone) {
        Administrator admin = findById(id);
        if (admin == null) {
            return "Administrator " + id + " not found.";
        }

        String error = Validator.validateUser(name, username, password, email, phone);
        if (error != null) {
            return error;
        }
        // Pass this admin's own ID so keeping the same username is allowed
        if (isUsernameTaken(username, id)) {
            return "Username '" + username + "' is already taken.";
        }

        admin.setName(name.trim());
        admin.setUsername(username.trim());
        admin.setPassword(password);
        admin.setEmail(email.trim());
        admin.setPhone(phone.trim());

        // Text files can't edit one line: read all, replace the matching line, write all back
        ArrayList<String> lines = FileManager.readLines(USERS_FILE);
        for (int i = 0; i < lines.size(); i++) {
            if (getId(lines.get(i)).equals(id)) {
                lines.set(i, admin.toFileLine());
                break;
            }
        }
        FileManager.writeLines(USERS_FILE, lines);
        return null;
    }

    // ===================== DELETE =====================

    public String deleteAdmin(String id) {
        if (findById(id) == null) {
            return "Administrator " + id + " not found.";
        }

        // Copy every line EXCEPT the one being deleted, then save the copy
        ArrayList<String> kept = new ArrayList<>();
        for (String line : FileManager.readLines(USERS_FILE)) {
            if (!getId(line).equals(id)) {
                kept.add(line);
            }
        }
        FileManager.writeLines(USERS_FILE, kept);
        return null;
    }

    // ===================== HELPERS =====================

    /**
     * True if any user (any role) already has this username.
     * excludeId: the user being updated, so they don't clash with themselves. Use null for add.
     */
    public boolean isUsernameTaken(String username, String excludeId) {
        for (String line : FileManager.readLines(USERS_FILE)) {
            String[] f = line.split("\\|");
            if (f.length < 3) {
                continue;
            }
            boolean sameUsername = f[2].equalsIgnoreCase(username.trim());
            boolean isSelf = f[0].equals(excludeId);
            if (sameUsername && !isSelf) {
                return true;
            }
        }
        return false;
    }

    /** The ID is always the first field of a line. */
    private String getId(String line) {
        return line.split("\\|")[0];
    }
}
