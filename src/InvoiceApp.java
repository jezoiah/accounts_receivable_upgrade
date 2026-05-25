package src;

import java.util.ArrayList;
import java.util.Scanner;

public class InvoiceApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        InvoiceDatabase db = new InvoiceDatabase();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n--- Invoice Menu ---");
            System.out.println("1] Add Invoice");
            System.out.println("2] Pay Invoice");
            System.out.println("3] Exit");

            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter Invoice Number: ");
                    String invoiceNum = input.nextLine();
                    System.out.print("Enter Customer Name: ");
                    String customerName = input.nextLine().trim();
                    System.out.print("Enter Invoice Amount: ");
                    int invoiceAmt = input.nextInt();
                    input.nextLine();

                    if (db.addInvoice(invoiceNum, customerName, invoiceAmt)) {
                        System.out.println("Invoice added successfully.");
                    } else {
                        System.out.println("Failed to add invoice.");
                    }
                    break;

                case "2":

                    System.out.print("Enter Customer Name: ");
                    String chosenUser = input.nextLine();

                    ArrayList<Invoice> invoices = db.getInvoices();
                    System.out.println("\n--- Invoices with Balance ---");
                    int pendingCounter = 0;
                    for (Invoice invoice : invoices) {
                        if (invoice.calculateBalance() > 0 && invoice.getCustomer().equalsIgnoreCase(chosenUser)) {
                            System.out.println("Invoice: " + invoice.getInvNum() + " | Customer: "
                                    + invoice.getCustomer() + " | Paid: " + invoice.getPayment() + " | Balance: "
                                    + invoice.calculateBalance());
                            pendingCounter++;
                        }
                    }
                    if (pendingCounter == 0) {
                        System.out.println("All balances have been paid.");

                    } else {

                        System.out.print("Enter total payment amount: ");
                        int payment = input.nextInt();
                        input.nextLine();

                        if (db.payInvoice(chosenUser, payment)) {
                            System.out.println("Payment distributed successfully.");

                            invoices = db.getInvoices();

                            int totalBalance = 0;

                            for (Invoice invoice : invoices) {

                                if (invoice.getCustomer().equalsIgnoreCase(chosenUser)) {
                                    totalBalance += invoice.calculateBalance();
                                }

                            }

                            System.out.println("Updated Total Balance for " + chosenUser + ": " + totalBalance);

                        } else {
                            System.out.println("Failed to record payment.");
                        }
                    }
                    break;

                case "3":
                    System.out.println("Exiting...");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1 to 3.");
            }
        }
        input.close();
    }
}