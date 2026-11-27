package sdams.model;

/**
 * Status of a payment. Stored in payments.txt in uppercase (rule 6).
 */
public enum PaymentStatus {
    PAID("Paid"),
    REFUNDED("Refunded");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Shown in JComboBoxes and JTables instead of PAID. */
    @Override
    public String toString() {
        return displayName;
    }
}