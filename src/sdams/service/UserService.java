package sdams.service;

import java.util.ArrayList;
import sdams.model.Administrator;
import sdams.model.AttendanceOfficer;
import sdams.model.FacilitiesOfficer;
import sdams.model.Instructor;
import sdams.model.Manager;
import sdams.model.Role;
import sdams.model.Student;
import sdams.model.User;
import sdams.util.FileManager;

/**
 * Shared user logic: login, and turning lines of users.txt into the right User subclass.
 */
public class UserService {

    private static final String USERS_FILE = "users.txt";

    /**
     * Checks the username and password.
     * Returns the logged-in user (a Manager, Student, ...) or null if login failed.
     */
    public User login(String username, String password) {
        for (User user : getAllUsers()) {
            // Username is not case-sensitive, password is
            if (user.getUsername().equalsIgnoreCase(username.trim())
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /** Every user in users.txt, each as the correct subclass. Broken lines are skipped. */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (String line : FileManager.readLines(USERS_FILE)) {
            User user = createUser(line);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Turns one line of users.txt into the right kind of object, based on the role field.
     * Returns null if the line is broken or the role is unknown.
     */
    public User createUser(String line) {
        String[] f = line.split("\\|");
        if (f.length < 7) {
            return null;
        }

        Role role;
        try {
            role = Role.valueOf(f[6]);   // "STUDENT" -> Role.STUDENT
        } catch (IllegalArgumentException e) {
            return null;                 // role text not in the enum
        }

        switch (role) {
            case MANAGER:
                return new Manager(f[0], f[1], f[2], f[3], f[4], f[5]);
            case ADMINISTRATOR:
                return new Administrator(f[0], f[1], f[2], f[3], f[4], f[5]);
            case INSTRUCTOR:
                return new Instructor(f[0], f[1], f[2], f[3], f[4], f[5]);
            case STUDENT:
                return new Student(f[0], f[1], f[2], f[3], f[4], f[5]);
            case FACILITIES_OFFICER:
                return new FacilitiesOfficer(f[0], f[1], f[2], f[3], f[4], f[5]);
            case ATTENDANCE_OFFICER:
                return new AttendanceOfficer(f[0], f[1], f[2], f[3], f[4], f[5]);
            default:
                return null;
        }
    }
}
