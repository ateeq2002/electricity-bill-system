package dao;

import model.Payment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addPayment(Payment p) {
        String sql = "INSERT INTO payment (bill_id, payment_date, amount_paid, payment_method, transaction_id) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getBillId());
            ps.setDate(2, Date.valueOf(p.getPaymentDate()));
            ps.setBigDecimal(3, p.getAmountPaid());
            ps.setString(4, p.getPaymentMethod());
            ps.setString(5, p.getTransactionId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PaymentDAO.addPayment error: " + e.getMessage());
        }
        return false;
    }

    // ── READ ALL ──────────────────────────────────────────────────────────────
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment ORDER BY payment_id DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("PaymentDAO.getAllPayments error: " + e.getMessage());
        }
        return list;
    }

    // ── READ BY BILL ──────────────────────────────────────────────────────────
    public List<Payment> getPaymentsByBill(int billId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("PaymentDAO.getPaymentsByBill error: " + e.getMessage());
        }
        return list;
    }

    // ── TOTAL REVENUE ─────────────────────────────────────────────────────────
    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(amount_paid), 0) FROM payment";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("PaymentDAO.getTotalRevenue error: " + e.getMessage());
        }
        return 0;
    }

    // ── HELPER ────────────────────────────────────────────────────────────────
    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("payment_id"),
            rs.getInt("bill_id"),
            rs.getDate("payment_date").toLocalDate(),
            rs.getBigDecimal("amount_paid"),
            rs.getString("payment_method"),
            rs.getString("transaction_id")
        );
    }
}
