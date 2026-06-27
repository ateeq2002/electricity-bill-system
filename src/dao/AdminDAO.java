package dao;

import model.Admin;
import util.DBConnection;

import java.sql.*;

public class AdminDAO {

    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(rs.getInt("admin_id"),
                                 rs.getString("username"),
                                 rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println("AdminDAO.login error: " + e.getMessage());
        }
        return null;
    }

    public boolean updatePassword(int adminId, String newPassword) {
        String sql = "UPDATE admin SET password = ? WHERE admin_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, adminId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AdminDAO.updatePassword error: " + e.getMessage());
        }
        return false;
    }
}
