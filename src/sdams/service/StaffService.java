package sdams.service;

import java.util.ArrayList;
import sdams.model.AttendanceOfficer;
import sdams.model.FacilitiesOfficer;
import sdams.model.Instructor;
import sdams.model.Role;
import sdams.model.User;
import sdams.util.FileManager;

/**
 * Administrator feature: Manage Staff.
 * Staff here means Instructors, Facilities Officers and Attendance Officers
 * (the Manager manages Administrators, so they are not included).
 * All of them are stored in users.txt.
 *
 * Shared code (checks, update, delete) is inherited from AccountService.
 */
public class StaffService extends AccountService {

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
        ArrayList<User> results = new ArrayList<>();

        for (User u : getAllStaff()) {
            // Skip people of a different role if a role was chosen
            if (role != null && u.getRole() != role) {
                continue;
            }
            if (matchesKeyword(u, keyword)) {
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

        String error = checkDetails(name, username, password, email, phone, null);
        if (error != null) {
            return error;
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
        return updateAccount(staff, name, username, password, email, phone);
    }

    // ===================== DELETE =====================

    /** Deletes a staff member if no records use their ID. Returns an error message, or null. */
    public String deleteStaff(String id) {
        User staff = findById(id);
        if (staff == null) {
            return "Staff member " + id + " not found.";
        }
        return deleteAccount(staff);
    }

    // ===================== HELPERS =====================

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
     * Staff version of the abstract method from AccountService.
     * Field positions follow the agreed formats in the README.
     */
    @Override
    protected String findRecordsUsing(User staff) {
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
}