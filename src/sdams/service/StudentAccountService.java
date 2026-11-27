package sdams.service;

import java.util.ArrayList;
import sdams.model.Role;
import sdams.model.Student;
import sdams.model.User;
import sdams.util.FileManager;

/**
 * Administrator feature: Manage Students (add, view, search, update, delete).
 * Students are stored in users.txt with role STUDENT and ID prefix S.
 *
 * Shared code (checks, update, delete) is inherited from AccountService.
 */
public class StudentAccountService extends AccountService {

    // ===================== VIEW =====================

    /** Every student in users.txt. */
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();

        for (User u : userService.getAllUsers()) {
            if (u.getRole() == Role.STUDENT) {
                // Safe cast: a user with role STUDENT was created as a Student object
                students.add((Student) u);
            }
        }
        return students;
    }

    /** Returns the student with this ID, or null if not found. */
    public Student findById(String id) {
        for (Student s : getAllStudents()) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    // ===================== SEARCH =====================

    /** Finds students whose ID, name, username, email or phone contains the keyword. */
    public ArrayList<Student> searchStudents(String keyword) {
        ArrayList<Student> results = new ArrayList<>();

        for (Student s : getAllStudents()) {
            if (matchesKeyword(s, keyword)) {   // inherited from AccountService
                results.add(s);
            }
        }
        return results;
    }

    // ===================== ADD =====================

    /** Adds a new student. Returns an error message, or null if it worked. */
    public String addStudent(String name, String username, String password,
                             String email, String phone) {
        String error = checkDetails(name, username, password, email, phone, null);
        if (error != null) {
            return error;
        }

        String id = FileManager.generateId(USERS_FILE, Role.STUDENT.getIdPrefix());
        Student student = new Student(id, name.trim(), username.trim(),
                password, email.trim(), phone.trim());

        FileManager.appendLine(USERS_FILE, student.toFileLine());
        return null;
    }

    // ===================== UPDATE =====================

    /** Updates a student's details. Returns an error message, or null if it worked. */
    public String updateStudent(String id, String name, String username, String password,
                                String email, String phone) {
        Student student = findById(id);
        if (student == null) {
            return "Student " + id + " not found.";
        }
        return updateAccount(student, name, username, password, email, phone);
    }

    // ===================== DELETE =====================

    /** Deletes a student if no records use their ID. Returns an error message, or null. */
    public String deleteStudent(String id) {
        Student student = findById(id);
        if (student == null) {
            return "Student " + id + " not found.";
        }
        return deleteAccount(student);
    }

    // ===================== HELPERS =====================

    /**
     * Student version of the abstract method from AccountService.
     * Field positions follow the agreed formats in the README.
     */
    @Override
    protected String findRecordsUsing(User student) {
        String id = student.getId();

        if (isIdInFile("bookings.txt", 1, id)) {     // bookingId|studentId|...
            return "bookings.txt";
        }
        if (isIdInFile("payments.txt", 2, id)) {     // paymentId|bookingId|studentId|...
            return "payments.txt";
        }
        if (isIdInFile("ratings.txt", 1, id)) {      // ratingId|studentId|...
            return "ratings.txt";
        }
        if (isIdInFile("attendance.txt", 2, id)) {   // attendanceId|classId|studentId|...
            return "attendance.txt";
        }
        return null;
    }
}