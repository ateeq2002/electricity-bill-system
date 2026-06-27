package dao;

import model.Consumer;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsumerDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addConsumer(Consumer c) {
        String sql = "INSERT INTO consumer (name, address, meter_number, connection_type, password, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getMeterNumber());
            ps.setString(4, c.getConnectionType());
            ps.setString(5, c.getPassword());
            ps.setString(6, c.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.addConsumer error: " + e.getMessage());
        }
        return false;
    }

    // ── READ ALL ──────────────────────────────────────────────────────────────
    public List<Consumer> getAllConsumers() {
        List<Consumer> list = new ArrayList<>();
        String sql = "SELECT * FROM consumer ORDER BY consumer_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.getAllConsumers error: " + e.getMessage());
        }
        return list;
    }

    // ── READ BY ID ────────────────────────────────────────────────────────────
    public Consumer getConsumerById(int id) {
        String sql = "SELECT * FROM consumer WHERE consumer_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.getConsumerById error: " + e.getMessage());
        }
        return null;
    }

    // ── READ BY METER ─────────────────────────────────────────────────────────
    public Consumer getConsumerByMeter(String meter) {
        String sql = "SELECT * FROM consumer WHERE meter_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, meter);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.getConsumerByMeter error: " + e.getMessage());
        }
        return null;
    }

    // ── LOGIN (consumer portal) ───────────────────────────────────────────────
    public Consumer login(String meterNumber, String password) {
        String sql = "SELECT * FROM consumer WHERE meter_number = ? AND password = ? AND status = 'Active'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, meterNumber);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.login error: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean updateConsumer(Consumer c) {
        String sql = "UPDATE consumer SET name=?, address=?, meter_number=?, "
                   + "connection_type=?, status=? WHERE consumer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getMeterNumber());
            ps.setString(4, c.getConnectionType());
            ps.setString(5, c.getStatus());
            ps.setInt(6, c.getConsumerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.updateConsumer error: " + e.getMessage());
        }
        return false;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean deleteConsumer(int id) {
        String sql = "DELETE FROM consumer WHERE consumer_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.deleteConsumer error: " + e.getMessage());
        }
        return false;
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────
    public List<Consumer> searchConsumers(String keyword) {
        List<Consumer> list = new ArrayList<>();
        String sql = "SELECT * FROM consumer WHERE name LIKE ? OR meter_number LIKE ? OR address LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ConsumerDAO.searchConsumers error: " + e.getMessage());
        }
        return list;
    }

    // ── HELPER ────────────────────────────────────────────────────────────────
    private Consumer mapRow(ResultSet rs) throws SQLException {
        return new Consumer(
            rs.getInt("consumer_id"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getString("meter_number"),
            rs.getString("connection_type"),
            rs.getString("password"),
            rs.getString("status")
        );
    }
}
