package src;

import java.sql.*;
import java.util.ArrayList;

public class InvoiceDatabase {
    private String URL = "jdbc:mysql://localhost:8889/receivable ";
    private String USER = "root";
    private String PASSWORD = "root";

    public boolean addInvoice(String invoiceNum, String customerName, int invoiceAmt) {
        String insertQuery = "INSERT INTO receivable (invno, customer, amount, payment) VALUES (?, ?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, invoiceNum);
            preparedStatement.setString(2, customerName);
            preparedStatement.setInt(3, invoiceAmt);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoiceList = new ArrayList<>();
        String query = "SELECT * FROM receivable";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = conn.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Invoice invoice = new Invoice();

                    invoice.setInvNo(resultSet.getString(2));
                    invoice.setCustomer(resultSet.getString(3));
                    invoice.setInvAmt(resultSet.getInt(4));
                    invoice.setPayment(resultSet.getInt(5));

                    invoiceList.add(invoice);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoiceList;
    }

    public boolean payInvoice(String customerName, int payment) {
        String selectQuery = "SELECT * FROM receivable WHERE customer = ? AND amount > payment";
        String updateQuery = "UPDATE receivable SET payment = ? WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            int remaining = payment;

            ArrayList<String> invnos = new ArrayList<>();
            ArrayList<Integer> oldPayments = new ArrayList<>();
            ArrayList<Integer> amounts = new ArrayList<>();

            selectStmt.setString(1, customerName);
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                invnos.add(rs.getString("invno"));
                oldPayments.add(rs.getInt("payment"));
                amounts.add(rs.getInt("amount"));
            }

            for (int i = 0; i < invnos.size() && remaining > 0; i++) {
                int balance = amounts.get(i) - oldPayments.get(i);
                int paidAmount;

                if (remaining >= balance) {
                    paidAmount = balance;
                } else {
                    paidAmount = remaining;
                }

                remaining -= paidAmount;

                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, oldPayments.get(i) + paidAmount);
                    updateStmt.setString(2, invnos.get(i));
                    updateStmt.executeUpdate();
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean invoiceExists(String invoiceNum) {
        String query = "SELECT * FROM receivable WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, invoiceNum);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}