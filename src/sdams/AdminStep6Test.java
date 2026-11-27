package sdams;

import java.util.ArrayList;
import sdams.model.Payment;
import sdams.model.PaymentStatus;
import sdams.service.PaymentRecordService;

/** Temporary test for Administrator Step 6 (payment records). Read-only: no file is changed. */
public class AdminStep6Test {

    public static void main(String[] args) {
        PaymentRecordService service = new PaymentRecordService();

        System.out.println("--- All payments ---");
        printList(service, service.getAllPayments());

        System.out.println("\n--- Only REFUNDED ---");
        printList(service, service.filterPayments("", PaymentStatus.REFUNDED, "", ""));

        System.out.println("\n--- Keyword 'john' (student name), only PAID ---");
        printList(service, service.filterPayments("john", PaymentStatus.PAID, "", ""));

        System.out.println("\n--- November 2026 only ---");
        printList(service, service.filterPayments("", null, "2026-11-01", "2026-11-30"));

        System.out.println("\n--- Bad filters ---");
        System.out.println("  " + service.validateFilter("2026-13-01", ""));
        System.out.println("  " + service.validateFilter("2026-11-30", "2026-11-01"));
        System.out.println("  Good filter: " + service.validateFilter("2026-11-01", "2026-11-30"));
    }

    private static void printList(PaymentRecordService service, ArrayList<Payment> list) {
        if (list.isEmpty()) {
            System.out.println("  (no payments)");
        }
        for (Payment p : list) {
            System.out.println("  " + p + "  " + service.getStudentName(p.getStudentId()));
        }
        System.out.printf("  Total paid: RM%.2f   Total refunded: RM%.2f%n",
                service.getTotalPaid(list), service.getTotalRefunded(list));
    }
}