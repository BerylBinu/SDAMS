package sdams.model;

/**
 * Every user role in SDAMS.
 * Stored in users.txt in uppercase (rule 6), e.g. ADMINISTRATOR.
 * Each role has its own ID prefix (rule 5), e.g. A0001 for administrators.
 */
public enum Role {
    MANAGER("M", "Manager"),
    ADMINISTRATOR("A", "Administrator"),
    INSTRUCTOR("I", "Instructor"),
    STUDENT("S", "Student"),
    FACILITIES_OFFICER("F", "Facilities Officer"),
    ATTENDANCE_OFFICER("T", "Attendance Officer");

    private final String idPrefix;
    private final String displayName;

    Role(String idPrefix, String displayName) {
        this.idPrefix = idPrefix;
        this.displayName = displayName;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** True for the four roles that appear in the Manager's Staff Report. */
    public boolean isStaff() {
        return this == ADMINISTRATOR || this == INSTRUCTOR
                || this == FACILITIES_OFFICER || this == ATTENDANCE_OFFICER;
    }

    /** Shown in JComboBoxes and JTables instead of FACILITIES_OFFICER. */
    @Override
    public String toString() {
        return displayName;
    }
}
