import java.util.*;

public class InvoiceApp {
    public static void main(String[] args) {
        InvoiceDatabase dbManager = new InvoiceDatabase();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n--- Invoice Menu ---");
            System.out.println("1. Add Invoice");
            System.out.println("2. Display Invoices with Balance");
            System.out.println("3. Pay Invoice");
            System.out.println("4. Delete Invoice");
            System.out.println("5. Exit Program");
            System.out.print("Select an option (1-5): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Invoice Number: ");
                    String invno = scanner.nextLine();
                    System.out.print("Enter Customer Name: ");
                    String customer = scanner.nextLine();
                    System.out.print("Enter Invoice Amount: ");
                    int amount = scanner.nextInt();
                    scanner.nextLine();

                    if (dbManager.addInv(invno, customer, amount)) {
                        System.out.println("Invoice Added Succesfully.");
                    } else {
                        System.out.println("Failed to add invoice.");
                    }
                    break;

                case "2":
                    ArrayList<Invoice> invoices = dbManager.showInv();
                    System.out.println("\n--- Invoices with Balance ---");
                    int pendingCounter = 0;
                    for (Invoice invoice : invoices) {
                        if (invoice.calculateBalance() > 0) {
                            System.out.println("Invoice: " + invoice.getInvno() + " | Customer: "
                                    + invoice.getCustomer() + " | Balance: " + invoice.calculateBalance());
                            pendingCounter++;
                        }
                    }
                    if (pendingCounter == 0) {
                        System.out.println("All balances have been paid");
                    }
                    break;

                case "3":
                    System.out.print("Enter Invoice Number to pay: ");
                    invno = scanner.nextLine();

                    if (dbManager.existInvo(invno)) {
                        System.out.print("Enter payment amount: ");
                        int payment = scanner.nextInt();
                        if (dbManager.updateCustomerBalance(invno, payment)) {
                            System.out.println("Payment Recorded!");
                        } else {
                            System.out.println("Failed to recored payment");
                        }
                    } else {
                        System.out.println("Invoice numner does not exist.");
                    }
                    break;

                case "4":
                    System.out.print("Enter invoice number to delete: ");
                    String delInv = scanner.nextLine();
                    if (dbManager.existInvo(delInv)) {
                        if (dbManager.confirmDel(delInv)) {
                            if (dbManager.deleteInv(delInv)) {
                                System.out.println("Invoice deleted successfully.");
                            } else {
                                System.out.println("Failed to delete invoice.");
                            }
                        } else {
                            System.out.println("Cannot delete invoice");
                        }
                    } else {
                        System.out.println("Invoice not found.");
                    }

                    break;

                case "5":
                    System.out.println("Exiting program. Goodbye!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 5.");
            }
        }
        scanner.close();
    }
}