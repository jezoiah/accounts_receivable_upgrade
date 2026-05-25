import java.util.Scanner;

public class Invoice {
    public String invno;
    public String customer;
    public int amount;
    public int payment;

    public void setInvno(String invno) {
        this.invno = invno;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getInvno() {
        return invno;
    }

    public String getCustomer() {
        return customer;
    }

    public int getAmount() {
        return amount;
    }

    public int getPayment() {
        return payment;
    }

    public int calculateBalance() {
        int balance = getAmount() - getPayment();
        return balance;
    }
}