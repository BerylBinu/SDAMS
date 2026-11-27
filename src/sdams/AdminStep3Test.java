package sdams;

import java.util.ArrayList;
import sdams.model.Role;
import sdams.model.User;
import sdams.service.StaffService;

/**
 * Temporary test for Administrator Step 3 (add, update, delete staff).
 * It adds a test instructor, updates them, then deletes them, so users.txt ends unchanged.
 */
public class AdminStep3Test {

    public static void main(String[] args) {
        StaffService service = new StaffService();

        System.out.println("--- Add with no role chosen ---");
        System.out.println("  " + service.addStaff(null, "Test Staff", "teststaff",
                "pass123", "t@sda.com", "0123456789"));

        System.out.println("\n--- Add as ADMINISTRATOR (not allowed for this feature) ---");
        System.out.println("  " + service.addStaff(Role.ADMINISTRATOR, "Test Staff", "teststaff",
                "pass123", "t@sda.com", "0123456789"));

        System.out.println("\n--- Add with bad phone ---");
        System.out.println("  " + service.addStaff(Role.INSTRUCTOR, "Test Staff", "teststaff",
                "pass123", "t@sda.com", "12345"));

        System.out.println("\n--- Add with username 'ALI01' (taken by an Administrator) ---");
        System.out.println("  " + service.addStaff(Role.INSTRUCTOR, "Test Staff", "ALI01",
                "pass123", "t@sda.com", "0123456789"));

        System.out.println("\n--- Add a valid Instructor ---");
        System.out.println("  Result: " + service.addStaff(Role.INSTRUCTOR, "Test Staff", "teststaff",
                "pass123", "t@sda.com", "0123456789"));

        // Find the new person's ID by searching for their username
        ArrayList<User> found = service.searchStaff("teststaff", null);
        String newId = found.get(0).getId();
        System.out.println("  New staff: " + found.get(0) + "  -> " + found.get(0).toFileLine());

        System.out.println("\n--- Update " + newId + ": new name, same username ---");
        System.out.println("  Result: " + service.updateStaff(newId, "Updated Staff", "teststaff",
                "pass123", "t@sda.com", "0123456789"));
        System.out.println("  Now: " + service.findById(newId));

        System.out.println("\n--- Update " + newId + " to username 'raj' (taken) ---");
        System.out.println("  " + service.updateStaff(newId, "Updated Staff", "raj",
                "pass123", "t@sda.com", "0123456789"));

        System.out.println("\n--- Delete " + newId + " ---");
        System.out.println("  Result: " + service.deleteStaff(newId));

        System.out.println("\n--- Delete again (should be not found) ---");
        System.out.println("  " + service.deleteStaff(newId));

        System.out.println("\n--- All staff at the end (same as before the test) ---");
        for (User u : service.getAllStaff()) {
            System.out.println("  " + u);
        }
    }
}