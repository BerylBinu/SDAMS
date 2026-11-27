package sdams;

import sdams.model.Student;
import sdams.service.StudentAccountService;

/**
 * Temporary test for Administrator Step 4 (manage students).
 * It adds a test student, updates them, then deletes them, so users.txt ends unchanged.
 */
public class AdminStep4Test {

    public static void main(String[] args) {
        StudentAccountService service = new StudentAccountService();

        System.out.println("--- All students ---");
        printAll(service);

        System.out.println("\n--- Add with bad email ---");
        System.out.println("  " + service.addStudent("Test Student", "teststudent",
                "pass123", "bad-email", "0123456789"));

        System.out.println("\n--- Add with username 'raj' (taken by an Instructor) ---");
        System.out.println("  " + service.addStudent("Test Student", "raj",
                "pass123", "s@mail.com", "0123456789"));

        System.out.println("\n--- Add a valid student ---");
        System.out.println("  Result: " + service.addStudent("Test Student", "teststudent",
                "pass123", "s@mail.com", "0123456789"));

        Student added = service.searchStudents("teststudent").get(0);
        String newId = added.getId();
        System.out.println("  New student: " + added.toFileLine());

        System.out.println("\n--- Search 'john' ---");
        for (Student s : service.searchStudents("john")) {
            System.out.println("  " + s);
        }

        System.out.println("\n--- Update " + newId + ": new phone ---");
        System.out.println("  Result: " + service.updateStudent(newId, "Test Student", "teststudent",
                "pass123", "s@mail.com", "0198887777"));
        System.out.println("  Now: " + service.findById(newId).toFileLine());

        System.out.println("\n--- Find staff ID 'I0001' as a student (should be null) ---");
        System.out.println("  " + service.findById("I0001"));

        System.out.println("\n--- Delete " + newId + " ---");
        System.out.println("  Result: " + service.deleteStudent(newId));

        System.out.println("\n--- All students at the end (same as the start) ---");
        printAll(service);
    }

    private static void printAll(StudentAccountService service) {
        for (Student s : service.getAllStudents()) {
            System.out.println("  " + s.toFileLine());
        }
    }
}