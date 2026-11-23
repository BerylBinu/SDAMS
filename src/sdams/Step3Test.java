package sdams;

import sdams.util.Validator;

/** Temporary test for Step 3. */
public class Step3Test {
    public static void main(String[] args) {
        System.out.println("--- Single checks ---");
        System.out.println("Name 'Ali bin Abu'      : " + Validator.isValidName("Ali bin Abu"));
        System.out.println("Name 'Ali123'           : " + Validator.isValidName("Ali123"));
        System.out.println("Email 'ali@sda.com'     : " + Validator.isValidEmail("ali@sda.com"));
        System.out.println("Email 'ali.com'         : " + Validator.isValidEmail("ali.com"));
        System.out.println("Phone '0123456789'      : " + Validator.isValidPhone("0123456789"));
        System.out.println("Phone '012-3456789'     : " + Validator.isValidPhone("012-3456789"));
        System.out.println("Password 'abc|123'      : " + Validator.isValidPassword("abc|123"));
        System.out.println("Date '2026-12-01'       : " + Validator.isValidDate("2026-12-01"));
        System.out.println("Date '2026-02-30'       : " + Validator.isValidDate("2026-02-30"));
        System.out.println("Amount '-5'             : " + Validator.isPositiveNumber("-5"));

        System.out.println("\n--- Whole form ---");
        String ok = Validator.validateUser("Ali bin Abu", "ali01", "pass123",
                "ali@sda.com", "0123456789");
        System.out.println("Good form : " + ok);

        String bad = Validator.validateUser("Ali bin Abu", "ali01", "pass123",
                "ali.com", "0123456789");
        System.out.println("Bad email : " + bad);
    }
}
