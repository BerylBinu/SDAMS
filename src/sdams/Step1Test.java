package sdams;

import sdams.model.Administrator;
import sdams.model.Manager;
import sdams.model.User;

/** Temporary test for Step 1. Right-click > Run File in NetBeans. Delete later. */
public class Step1Test {
    public static void main(String[] args) {
        User m = new Manager("M0001", "Siti Aminah", "manager", "admin123",
                "siti@sda.com", "0121112222");
        User a = new Administrator("A0001", "Ali bin Abu", "ali01", "pass123",
                "ali@sda.com", "0123456789");

        // Polymorphism: both variables are type User,
        // but getRole() runs the subclass version.
        User[] users = { m, a };
        for (User u : users) {
            System.out.println(u);
            System.out.println("  file line: " + u.toFileLine());
            System.out.println("  is staff?  " + u.getRole().isStaff());
        }
    }
}
