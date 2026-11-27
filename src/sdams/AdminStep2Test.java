package sdams;

import java.util.ArrayList;
import sdams.model.Role;
import sdams.model.User;
import sdams.service.StaffService;

/** Temporary test for Administrator Step 2 (view + search staff). Read-only: users.txt is not changed. */
public class AdminStep2Test {

    public static void main(String[] args) {
        StaffService service = new StaffService();

        System.out.println("--- All staff ---");
        // One ArrayList<User> holds three different classes = polymorphism
        for (User u : service.getAllStaff()) {
            System.out.println("  " + u.getClass().getSimpleName() + " -> " + u);
        }

        System.out.println("\n--- Find by ID 'F0001' ---");
        System.out.println("  " + service.findById("F0001"));

        System.out.println("\n--- Find by ID 'A0001' (an Administrator, should be null) ---");
        System.out.println("  " + service.findById("A0001"));

        System.out.println("\n--- Search 'raj', all roles ---");
        printList(service.searchStaff("raj", null));

        System.out.println("\n--- Search '', only Attendance Officers ---");
        printList(service.searchStaff("", Role.ATTENDANCE_OFFICER));

        System.out.println("\n--- Search 'raj', only Facilities Officers (should be none) ---");
        printList(service.searchStaff("raj", Role.FACILITIES_OFFICER));
    }

    private static void printList(ArrayList<User> list) {
        if (list.isEmpty()) {
            System.out.println("  (no results)");
        }
        for (User u : list) {
            System.out.println("  " + u);
        }
    }
}