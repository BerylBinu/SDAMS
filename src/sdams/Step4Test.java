package sdams;

import sdams.model.Administrator;
import sdams.service.AdminService;

/** Temporary test for Step 4. It adds, updates and then deletes a test admin, so users.txt ends unchanged. */
public class Step4Test {
    public static void main(String[] args) {
        AdminService service = new AdminService();

        System.out.println("--- All admins ---");
        printAll(service);

        System.out.println("\n--- Add with bad email ---");
        System.out.println(service.addAdmin("Test Admin", "testadmin", "pass123", "bad-email", "0123456789"));

        System.out.println("\n--- Add with taken username 'ali01' ---");
        System.out.println(service.addAdmin("Test Admin", "ali01", "pass123", "t@sda.com", "0123456789"));

        System.out.println("\n--- Add a valid admin ---");
        System.out.println("Result: " + service.addAdmin("Test Admin", "testadmin", "pass123", "t@sda.com", "0123456789"));
        printAll(service);

        // Find the new admin's ID by searching
        String newId = service.searchAdmins("testadmin").get(0).getId();

        System.out.println("\n--- Search 'tan' ---");
        for (Administrator a : service.searchAdmins("tan")) {
            System.out.println("  " + a);
        }

        System.out.println("\n--- Update " + newId + " name ---");
        System.out.println("Result: " + service.updateAdmin(newId, "Updated Admin", "testadmin",
                "pass123", "t@sda.com", "0123456789"));
        System.out.println("  " + service.findById(newId));

        System.out.println("\n--- Delete " + newId + " ---");
        System.out.println("Result: " + service.deleteAdmin(newId));
        printAll(service);
    }

    private static void printAll(AdminService service) {
        for (Administrator a : service.getAllAdmins()) {
            System.out.println("  " + a.toFileLine());
        }
    }
}
