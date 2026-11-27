package sdams.service;

import java.util.ArrayList;
import sdams.model.AttendanceOfficer;
import sdams.model.FacilitiesOfficer;
import sdams.model.Instructor;
import sdams.model.Role;
import sdams.model.User;
import sdams.util.FileManager;
import sdams.util.Validator;

/**
 * Administrator feature: Manage Staff.
 * Staff here means Instructors, Facilities Officers and Attendance Officers
 * (the Manager manages Administrators, so they are not included).
 * All of them are stored in users.txt.
 */
public class StaffService {

    private static final String USERS_FILE = "users.txt";

    // Reuse the shared code that turns a users.txt line into the right subclass
    private final UserService userService = new UserService();

    /** True for the three roles the Administrator manages. */
    public boolean isManagedRole(Role role) {
        return role == Role.INSTRUCTOR
                || role == Role.FACILITIES_OFFICER
                || role == Role.ATTENDANCE_OFFICER;
    }

    // ===================== VIEW =====================

    /** Every Instructor, Facilities Officer and Attendance Officer. */
    public ArrayList<User> getAllStaff() {
        ArrayList<User> staff = new ArrayList<>();

        for (User u : userService.getAllUsers()) {
            if (isManagedRole(u.getRole())) {
                staff.add(u);
            }
        }
        return staff;
    }

    /** Returns the staff member with this ID, or null if not found. */
    public User findById(String id) {
        for (User u : getAllStaff()) {
            if (u.getId().equalsIgnoreCase(id.trim())) {
                return u;
            }
        }
        return null;
    }

    // ===================== SEARCH =====================

    /**
     * Finds staff whose ID, name, username, email or phone contains the keyword
     * (not case-sensitive).
     * role: only this role, or null for all three staff roles.
     * An empty keyword matches everyone, so ("", null) returns all staff.
     */
    public ArrayList<User> searchStaff(String keyword, Role role) {
        String k = "";
        if (!Validator.isEmpty(keyword)) {
            k = keyword.trim().toLowerCase();
        }

        ArrayList<User> results = new ArrayList<>();

        for (User u : getAllStaff()) {
            // Skip people of a different role if a role was chosen
            if (role != null && u.getRole() != role) {
                continue;
            }

            if (u.getId().toLowerCase().contains(k)
                    || u.getName().toLowerCase().contains(k)
                    || u.getUsername().toLowerCase().contains(k)
                    || u.getEmail().toLowerCase().contains(k)
                    || u.getPhone().contains(k)) {
                results.add(u);
            }
        }
        return results;
    }

    // ===================== ADD =====================

    /** Adds a new staff member. Returns an error message, or null if it worked. */
    public String addStaff(Role role, String name, String username, String password,
                           String email, String phone) {
        if (role == null || !isManagedRole(role)) {
            return "Please choose Instructor, Facilities Officer or Attendance Officer.";
        }

        String error = Validator.validateUser(name, username, password, email, phone);
        if (error != null) {
            return error;
        }
        if (isUsernameTaken(username, null)) {
            return "Username '" + username.trim() + "' is already taken.";
        }

        // The prefix comes from the role: I, F or T
        String id = FileManager.generateId(USERS_FILE, role.getIdPrefix());

        User staff = createStaffObject(role, id, name.trim(), username.trim(),
                password, email.trim(), phone.trim());

        FileManager.appendLine(USERS_FILE, staff.toFileLine());
        return null;
    }

    // ===================== UPDATE =====================

    /**
     * Updates a staff member's details. The role and ID cannot change
     * (the ID prefix belongs to the role). Returns an error message, or null if it worked.
     */
    public String updateStaff(String id, String name, String username, String password,
                              String email, String phone) {
        User staff = findById(id);
        if (staff == null) {
            return "Staff member " + id + " not found.";
        }

        String error = Validator.validateUser(name, username, password, email, phone);
        if (error != null) {
            return error;
        }
        // Pass this person's own ID so keeping the same username is allowed
        if (isUsernameTaken(username, staff.getId())) {
            return "Username '" + username.trim() + "' is already taken.";
        }

        staff.setName(name.trim());
        staff.setUsername(username.trim());
        staff.setPassword(password);
        staff.setEmail(email.trim());
        staff.setPhone(phone.trim());

        // Read all lines, replace the matching one, write all back
        ArrayList<String> lines = FileManager.readLines(USERS_FILE);
        for (int i = 0; i < lines.size(); i++) {
            if (getIdFromLine(lines.get(i)).equals(staff.getId())) {
                lines.set(i, staff.toFileLine());   // works for any subclass
                break;
            }
        }
        FileManager.writeLines(USERS_FILE, lines);
        return null;
    }

    // ===================== DELETE =====================

    /**
     * Deletes a staff member. Blocked if other records still point to their ID,
     * otherwise those records would be left pointing at nobody (rule 4: link by ID).
     * Returns an error message, or null if it worked.
     */
    public String deleteStaff(String id) {
        User staff = findById(id);
        if (staff == null) {
            return "Staff member " + id + " not found.";
        }

        String usedIn = findRecordsUsingStaff(staff);
        if (usedIn != null) {
            return staff.getName() + " cannot be deleted because they still have records in "
                    + usedIn + ".";
        }

        // Copy every line EXCEPT the one being deleted, then save the copy
        ArrayList<String> kept = new ArrayList<>();
        for (String line : FileManager.readLines(USERS_FILE)) {
            if (!getIdFromLine(line).equals(staff.getId())) {
                kept.add(line);
            }
        }
        FileManager.writeLines(USERS_FILE, kept);
        return null;
    }

    // ===================== HELPERS =====================

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

    /** Makes the right subclass for the chosen role. */
    private User createStaffObject(Role role, String id, String name, String username,
                                   String password, String email, String phone) {
        switch (role) {
            case INSTRUCTOR:
                return new Instructor(id, name, username, password, email, phone);
            case FACILITIES_OFFICER:
                return new FacilitiesOfficer(id, name, username, password, email, phone);
            default: // ATTENDANCE_OFFICER (addStaff already checked the role)
                return new AttendanceOfficer(id, name, username, password, email, phone);
        }
    }

    /**
     * Returns the name of a file that still uses this staff member's ID, or null if none.
     * Field positions follow the agreed formats in the README.
     */
    private String findRecordsUsingStaff(User staff) {
        String id = staff.getId();

        if (staff.getRole() == Role.INSTRUCTOR) {
            if (isIdInFile("classes.txt", 3, id)) {    // classId|className|discipline|instructorId|...
                return "classes.txt";
            }
            if (isIdInFile("problems.txt", 1, id)) {   // problemId|instructorId|...
                return "problems.txt";
            }
            if (isIdInFile("ratings.txt", 2, id)) {    // ratingId|studentId|instructorId|...
                return "ratings.txt";
            }
        }
        if (staff.getRole() == Role.ATTENDANCE_OFFICER) {
            if (isIdInFile("attendance.txt", 4, id)) { // attendanceId|classId|studentId|status|recordedBy|...
                return "attendance.txt";
            }
        }
        return null;
    }

    /** True if any line of the file has this ID at the given field position. */
    private boolean isIdInFile(String fileName, int fieldIndex, String id) {
        for (String line : FileManager.readLines(fileName)) {
            String[] f = line.split("\\|");
            if (f.length > fieldIndex && f[fieldIndex].equals(id)) {
                return true;
            }
        }
        return false;
    }

    /** The ID is always the first field of a line. */
    private String getIdFromLine(String line) {
        return line.split("\\|")[0];
    }
}