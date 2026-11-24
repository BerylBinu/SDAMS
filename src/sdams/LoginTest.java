package sdams;

import sdams.model.User;
import sdams.service.UserService;

/** Temporary test for login. */
public class LoginTest {
    public static void main(String[] args) {
        UserService service = new UserService();

        test(service, "manager", "admin123");   // correct
        test(service, "MANAGER", "admin123");   // username not case-sensitive
        test(service, "manager", "wrong");      // wrong password
        test(service, "nobody", "pass123");     // no such user
        test(service, "john", "pass123");       // a student

        System.out.println("\nAll users loaded as their own class:");
        for (User u : service.getAllUsers()) {
            System.out.println("  " + u.getClass().getSimpleName() + " -> " + u);
        }
    }

    private static void test(UserService service, String username, String password) {
        User user = service.login(username, password);
        if (user == null) {
            System.out.println(username + " / " + password + "  ->  LOGIN FAILED");
        } else {
            System.out.println(username + " / " + password + "  ->  " + user);
        }
    }
}
