package sdams;

import java.util.ArrayList;
import sdams.model.Administrator;
import sdams.util.FileManager;

/** Temporary test for Step 2. Each run adds one new administrator to users.txt. */
public class Step2Test {
    public static void main(String[] args) {
        // 1. Make the next administrator ID
        String newId = FileManager.generateId("users.txt", "A");
        System.out.println("Next admin ID: " + newId);

        // 2. Create the object and save it as one line
        Administrator admin = new Administrator(newId, "Test Admin", "test" + newId,
                "pass123", "test@sda.com", "0120000000");
        FileManager.appendLine("users.txt", admin.toFileLine());

        // 3. Read the file back and print every line
        ArrayList<String> lines = FileManager.readLines("users.txt");
        System.out.println("users.txt now has " + lines.size() + " lines:");
        for (String line : lines) {
            System.out.println("  " + line);
        }
    }
}
