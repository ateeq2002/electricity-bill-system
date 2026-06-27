package dao;

import model.Bill;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addBill(Bill b) {
        String sql = "INSERT INTO bill (consumer_id, month, units_consumed, rate_per_unit, "
                   + "total_amount, due_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getConsumerId());
            ps.setString(2, b.getMonth());
            ps.setBigDecimal(3, b.getUnitsConsumed());
            ps.setBigDecimal(4, b.getRatePerUnit());
            ps.setBigDecimal(5, b.getTotalAmount());
            ps.setDate(6, Date.valueOf(b.getDueDate()));
            ps.setString(7, b.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("BillDAO.addBill error: " + e.getMessage());
        }
        return false;
    }

    // ── READ ALL ──────────────────────────────────────────────────────────────
    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill ORDER BY bill_id DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("BillDAO.getAllBills error: " + e.getMessage());
        }
        return list;
    }

    // ── READ BY CONSUMER ──────────────────────────────────────────────────────
    public List<Bill> getBillsByConsumer(int consumerId) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill WHERE consumer_id = ? ORDER BY bill_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consumerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("BillDAO.getBillsByConsumer error: " + e.getMessage());
        }
        return list;
    }

    // ── READ BY ID ────────────────────────────────────────────────────────────
    public Bill getBillById(int billId) {
        String sql = "SELECT * FROM bill WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("BillDAO.getBillById error: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE STATUS ─────────────────────────────────────────────────────────
    public boolean updateBillStatus(int billId, String status) {
        String sql = "UPDATE bill SET status = ? WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, billId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("BillDAO.updateBillStatus error: " + e.getMessage());
        }
        return false;
    }

    // ── UPDATE FULL ───────────────────────────────────────────────────────────
    public boolean updateBill(Bill b) {
        String sql = "UPDATE bill SET month=?, units_consumed=?, rate_per_unit=?, "
                   + "total_amount=?, due_date=?, status=? WHERE bill_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, b.getMonth());
            ps.setBigDecimal(2, b.getUnitsConsumed());
            ps.setBigDecimal(3, b.getRatePerUnit());
            ps.setBigDecimal(4, b.getTotalAmount());
            ps.setDate(5, Date.valueOf(b.getDueDate()));
            ps.setString(6, b.getStatus());
            ps.setInt(7, b.getBillId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("BillDAO.updateBill error: " + e.getMessage());
        }
        return false;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean deleteBill(int billId) {
        String sql = "DELETE FROM bill WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("BillDAO.deleteBill error: " + e.getMessage());
        }
        return false;
    }

    // ── PENDING BILLS COUNT ───────────────────────────────────────────────────
    public int countPendingBills() {
        String sql = "SELECT COUNT(*) FROM bill WHERE status = 'Pending'";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("BillDAO.countPendingBills error: " + e.getMessage());
        }
        return 0;
    }

    // ── HELPER ────────────────────────────────────────────────────────────────
    private Bill mapRow(ResultSet rs) throws SQLException {
        return new Bill(
            rs.getInt("bill_id"),
            rs.getInt("consumer_id"),
            rs.getString("month"),
            rs.getBigDecimal("units_consumed"),
            rs.getBigDecimal("rate_per_unit"),
            rs.getBigDecimal("total_amount"),
            rs.getDate("due_date").toLocalDate(),
            rs.getString("status")
        );
    }
}
