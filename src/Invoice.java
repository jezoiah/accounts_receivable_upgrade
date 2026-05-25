package src;
public class Invoice {
    private String invoiceNum;
    private int invoiceAmt;
    private String customerName;
    private int payment;

    public void setInvNo(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }
    public void setInvAmt(int invoiceAmt) {
        this.invoiceAmt = invoiceAmt;
    }
    public void setCustomer(String customerName) {
        this.customerName = customerName;
    }
    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getInvNum() {
        return invoiceNum;
    }
    public int getInvAmt() {
        return invoiceAmt;
    }
    public String getCustomer() {
        return customerName;
    }
    public int getPayment() {
        return payment;
    }

    public int calculateBalance() {
        int balance = getInvAmt() - getPayment();
        return balance;
    }
}