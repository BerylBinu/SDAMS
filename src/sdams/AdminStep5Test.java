package sdams;

import sdams.model.Payment;
import sdams.model.PaymentStatus;

/** Temporary test for Administrator Step 5 (Payment model). Does not touch any file. */
public class AdminStep5Test {

    public static void main(String[] args) {
        System.out.println("--- Object -> file line ---");
        Payment p = new Payment("P0001", "B0001", "S0001", 50, "2026-12-01", PaymentStatus.PAID);
        System.out.println("  toString  : " + p);
        System.out.println("  toFileLine: " + p.toFileLine());

        System.out.println("\n--- File line -> object ---");
        Payment back = Payment.fromFileLine("P0002|B0003|S0001|120.5|2026-12-03|REFUNDED");
        System.out.println("  " + back);
        System.out.println("  amount as a number + 10 = " + (back.getAmount() + 10));

        System.out.println("\n--- Broken lines (all should be null) ---");
        System.out.println("  too few fields : " + Payment.fromFileLine("P0003|B0004|S0002"));
        System.out.println("  bad amount     : " + Payment.fromFileLine("P0003|B0004|S0002|abc|2026-12-03|PAID"));
        System.out.println("  bad status     : " + Payment.fromFileLine("P0003|B0004|S0002|50|2026-12-03|DONE"));

        System.out.println("\n--- Refund: the only allowed change ---");
        p.setStatus(PaymentStatus.REFUNDED);
        System.out.println("  " + p.toFileLine());
    }
}