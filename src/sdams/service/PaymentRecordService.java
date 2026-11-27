package sdams.service;

import java.time.LocalDate;
import java.util.ArrayList;
import sdams.model.Payment;
import sdams.model.PaymentStatus;
import sdams.model.Student;
import sdams.util.FileManager;
import sdams.util.Validator;

/**
 * Administrator feature: Payment Records.
 * View and filter every payment made by students. Read-only:
 * payments.txt is written by the Student feature, the Administrator only looks.
 */
public class PaymentRecordService {

    // Used to show the student's name next to their ID
    private final StudentAccountService studentService = new StudentAccountService();

    // ===================== VIEW =====================

    /** Every payment in payments.txt. Broken lines are skipped. */
    public ArrayList<Payment> getAllPayments() {
        ArrayList<Payment> payments = new ArrayList<>();

        for (String line : FileManager.readLines(Payment.FILE_NAME)) {
            Payment p = Payment.fromFileLine(line);
            if (p != null) {
                payments.add(p);
            }
        }
        return payments;
    }

    // ===================== FILTER =====================

    /**
     * Checks the filter's date boxes before filtering.
     * Empty dates are allowed (no limit). Returns an error message, or null if OK.
     */
    public String validateFilter(String fromDate, String toDate) {
        if (!Validator.isEmpty(fromDate) && !Validator.isValidDate(fromDate.trim())) {
            return "From date must be a real date in yyyy-MM-dd format, e.g. 2026-11-01";
        }
        if (!Validator.isEmpty(toDate) && !Validator.isValidDate(toDate.trim())) {
            return "To date must be a real date in yyyy-MM-dd format, e.g. 2026-11-30";
        }
        if (!Validator.isEmpty(fromDate) && !Validator.isEmpty(toDate)) {
            LocalDate from = LocalDate.parse(fromDate.trim());
            LocalDate to = LocalDate.parse(toDate.trim());
            if (from.isAfter(to)) {
                return "From date cannot be after To date.";
            }
        }
        return null;
    }

    /**
     * Returns the payments that match ALL the filters given.
     * Leave a filter empty (or null) to ignore it:
     *   keyword  - matches payment ID, booking ID, student ID or student name
     *   status   - PAID or REFUNDED, null for both
     *   fromDate / toDate - yyyy-MM-dd, both inclusive
     * Call validateFilter first so the dates are known to be valid.
     */
    public ArrayList<Payment> filterPayments(String keyword, PaymentStatus status,
                                             String fromDate, String toDate) {
        ArrayList<Payment> results = new ArrayList<>();

        for (Payment p : getAllPayments()) {
            if (!matchesKeyword(p, keyword)) {
                continue;
            }
            if (status != null && p.getStatus() != status) {
                continue;
            }
            if (!isInDateRange(p.getPaymentDate(), fromDate, toDate)) {
                continue;
            }
            results.add(p);   // passed every filter
        }
        return results;
    }

    // ===================== TOTALS =====================

    /** Total of the PAID payments in the list (refunded money is not counted). */
    public double getTotalPaid(ArrayList<Payment> payments) {
        double total = 0;
        for (Payment p : payments) {
            if (p.getStatus() == PaymentStatus.PAID) {
                total += p.getAmount();
            }
        }
        return total;
    }

    /** Total of the REFUNDED payments in the list. */
    public double getTotalRefunded(ArrayList<Payment> payments) {
        double total = 0;
        for (Payment p : payments) {
            if (p.getStatus() == PaymentStatus.REFUNDED) {
                total += p.getAmount();
            }
        }
        return total;
    }

    // ===================== HELPERS =====================

    /** The student's name for a table, or a note if the student no longer exists. */
    public String getStudentName(String studentId) {
        Student s = studentService.findById(studentId);
        if (s == null) {
            return "(unknown student)";
        }
        return s.getName();
    }

    private boolean matchesKeyword(Payment p, String keyword) {
        if (Validator.isEmpty(keyword)) {
            return true;
        }
        String k = keyword.trim().toLowerCase();
        return p.getPaymentId().toLowerCase().contains(k)
                || p.getBookingId().toLowerCase().contains(k)
                || p.getStudentId().toLowerCase().contains(k)
                || getStudentName(p.getStudentId()).toLowerCase().contains(k);
    }

    /** True if the date is between from and to (inclusive). An empty limit means no limit. */
    private boolean isInDateRange(String date, String fromDate, String toDate) {
        if (!Validator.isValidDate(date)) {
            return false;   // a payment with a broken date can't be placed in a range
        }
        LocalDate d = LocalDate.parse(date);

        if (!Validator.isEmpty(fromDate) && d.isBefore(LocalDate.parse(fromDate.trim()))) {
            return false;
        }
        if (!Validator.isEmpty(toDate) && d.isAfter(LocalDate.parse(toDate.trim()))) {
            return false;
        }
        return true;
    }
}