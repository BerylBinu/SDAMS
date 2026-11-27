package sdams.service;

import java.util.ArrayList;
import sdams.model.Role;
import sdams.model.User;
import sdams.util.Validator;

/**
 * Administrator feature: Manage Staff.
 * Staff here means Instructors, Facilities Officers and Attendance Officers
 * (the Manager manages Administrators, so they are not included).
 * All of them are stored in users.txt.
 */
public class StaffService {

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
}