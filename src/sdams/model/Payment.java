package sdams.model;

/**
 * One payment made by a student for a booking. One line of payments.txt:
 *
 *   paymentId|bookingId|studentId|amount|paymentDate|status
 *   P0001|B0001|S0001|50.00|2026-12-01|PAID
 *
 * Written by the Student feature (pay for a booking).
 * Read by the Administrator (payment records) and the Manager (revenue report).
 */
public class Payment {

    public static final String FILE_NAME = "payments.txt";
    public static final String ID_PREFIX = "P";

    private String paymentId;
    private String bookingId;
    private String studentId;
    private double amount;
    private String paymentDate;     // yyyy-MM-dd (rule 3)
    private PaymentStatus status;

    public Payment(String paymentId, String bookingId, String studentId,
                   double amount, String paymentDate, PaymentStatus status) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    /** Converts this payment into one line of payments.txt. Amount always has 2 decimals. */
    public String toFileLine() {
        return paymentId + "|" + bookingId + "|" + studentId + "|"
                + String.format("%.2f", amount) + "|" + paymentDate + "|" + status.name();
    }

    /**
     * Turns one line of payments.txt into a Payment object.
     * Returns null if the line is broken (wrong number of fields, bad amount or bad status).
     */
    public static Payment fromFileLine(String line) {
        String[] f = line.split("\\|");
        if (f.length < 6) {
            return null;
        }

        try {
            double amount = Double.parseDouble(f[3]);              // "50.00" -> 50.0
            PaymentStatus status = PaymentStatus.valueOf(f[5]);    // "PAID"  -> PaymentStatus.PAID
            return new Payment(f[0], f[1], f[2], amount, f[4], status);
        } catch (IllegalArgumentException e) {
            // NumberFormatException (bad amount) is a kind of IllegalArgumentException,
            // so this one catch handles a bad amount AND a bad status
            return null;
        }
    }

    // ---------- getters ----------
    // No setters for ID, booking, student, amount or date: a payment record should not change.

    public String getPaymentId() { return paymentId; }
    public String getBookingId() { return bookingId; }
    public String getStudentId() { return studentId; }
    public double getAmount() { return amount; }
    public String getPaymentDate() { return paymentDate; }

    public PaymentStatus getStatus() { return status; }

    /** The only change allowed: e.g. PAID -> REFUNDED when a booking is cancelled. */
    public void setStatus(PaymentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return paymentId + " - RM" + String.format("%.2f", amount) + " by " + studentId
                + " on " + paymentDate + " (" + status + ")";
    }
}