package sdams.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads and writes the system's .txt files. Shared by the whole team.
 * All files live in the "data" folder inside the NetBeans project folder.
 */
public class FileManager {

    public static final String DATA_FOLDER = "data";

    /** Reads every non-empty line of a file. Returns an empty list if the file does not exist yet. */
    public static ArrayList<String> readLines(String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(DATA_FOLDER, fileName);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        }
        return lines;
    }

    /** Replaces the whole file with these lines. Used for update and delete. */
    public static void writeLines(String fileName, ArrayList<String> lines) {
        File file = new File(DATA_FOLDER, fileName);
        file.getParentFile().mkdirs(); // creates the data folder if missing

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing " + fileName + ": " + e.getMessage());
        }
    }

    /** Adds one line to the end of the file. Used for add. */
    public static void appendLine(String fileName, String line) {
        File file = new File(DATA_FOLDER, fileName);
        file.getParentFile().mkdirs();

        // "true" means append instead of overwrite
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Makes the next ID for a prefix (rule 5: highest existing number + 1).
     * Example: file has A0001 and A0003, prefix "A"  ->  returns "A0004".
     */
    public static String generateId(String fileName, String prefix) {
        int highest = 0;

        for (String line : readLines(fileName)) {
            String id = line.split("\\|")[0];   // first field is always the ID

            if (id.startsWith(prefix)) {
                String numberPart = id.substring(prefix.length());
                try {
                    int number = Integer.parseInt(numberPart);
                    if (number > highest) {
                        highest = number;
                    }
                } catch (NumberFormatException e) {
                    // ignore IDs with a different prefix like "AB0001"
                }
            }
        }
        return prefix + String.format("%04d", highest + 1);
    }
}
