import java.sql.*;
import java.util.*;

import com.mysql.cj.protocol.Resultset;

public class InvoiceDatabase {
    private String url = "jdbc:mysql://localhost:3306/javadb";
    private String dbUser = "root";
    private String dbPassword = "";

    public boolean existInvo(String invno) {
        String query = "SELECT * FROM credentials WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, invno);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addInv(String invno, String customer, int amount) {
        String insertQuery = "INSERT INTO receivable (invno, customer, amount, payment) VALUES (?, ?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
                PreparedStatement pst = conn.prepareStatement(insertQuery)) {

            pst.setString(1, invno);
            pst.setString(2, customer);
            pst.setInt(3, amount);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomerBalance(String invno, int payment) {
        String selectQuery = "SELECT invno, customer, amount, payment FROM receivable";
        String updateQuery = "UPDATE credentials SET amount = ? AND SET payment = ? WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pst = conn.prepareStatement(updateQuery);
             PreparedStatement pst2 = conn.prepareStatement(selectQuery)) {

            pst.setString(1, invno);
            int prevpayment = 0;
            try(ResultSet rs = pst.executeQuery()){
                if (rs.next()){
                    prevpayment = rs.getInt(5);                }
            }

            pst.setInt(1, (prevpayment + payment));
            pst.setString(2, (prevpayment + invno));

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean confirmDel(String invno) {
        String selectQuery = "SELECT * FROM receivable WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pst = conn.prepareStatement(selectQuery)) {

            pst.setString(1, invno);
            int payment = 0;

            try(ResultSet rs = pst.executeQuery()){
                if (rs.next()){
                    payment = rs.getInt(5);                
                }
                if (payment > 0){
                    return false;
                }else {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInv(String invno) {
        String deleteQuery = "DELETE FROM receivable WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pst = conn.prepareStatement(deleteQuery)) {

            pst.setString(1, invno);
            
            int rowsDeleetd = pst.executeUpdate();
            return rowsDeleetd > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<Invoice> showInv(){
        ArrayList<Invoice> invoices = new ArrayList<Invoice>();
        String selectQuery = "SELECT invno, customer, amount, payment FROM receivable";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            Statement st = conn.createStatement()) {

            try (ResultSet rs = st.executeQuery(selectQuery)) {
                while(rs.next()){
                    Invoice invoice = new Invoice();
                    invoice.setInvno(rs.getString(1));
                    invoice.setCustomer(rs.getString(1));
                    invoice.setAmount(rs.getInt(1));
                    invoice.setPayment(rs.getInt(1));
                    
                    invoices.add(invoice);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
}