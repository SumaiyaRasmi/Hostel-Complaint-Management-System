package dao;

import model.Complaint;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    public boolean addComplaint(Complaint c) {
        String sql = "INSERT INTO complaints (student_id, category_id, title, description, priority, status) VALUES (?, ?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getStudentId());
            ps.setInt(2, c.getCategoryId());
            ps.setString(3, c.getTitle());
            ps.setString(4, c.getDescription());
            ps.setString(5, c.getPriority());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Complaint> getComplaintsByStudent(int studentId) {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT c.*, cat.name AS category_name, u.full_name, u.room_number " +
                     "FROM complaints c " +
                     "JOIN categories cat ON c.category_id = cat.id " +
                     "JOIN users u ON c.student_id = u.id " +
                     "WHERE c.student_id = ? ORDER BY c.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapComplaint(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT c.*, cat.name AS category_name, u.full_name, u.room_number " +
                     "FROM complaints c " +
                     "JOIN categories cat ON c.category_id = cat.id " +
                     "JOIN users u ON c.student_id = u.id " +
                     "ORDER BY c.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapComplaint(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Complaint> getComplaintsByStatus(String status) {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT c.*, cat.name AS category_name, u.full_name, u.room_number " +
                     "FROM complaints c " +
                     "JOIN categories cat ON c.category_id = cat.id " +
                     "JOIN users u ON c.student_id = u.id " +
                     "WHERE c.status = ? ORDER BY c.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapComplaint(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateComplaintStatus(int id, String status, String adminRemarks) {
        String sql = "UPDATE complaints SET status = ?, admin_remarks = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, adminRemarks);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteComplaint(int id) {
        String sql = "DELETE FROM complaints WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM complaints WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int totalComplaints() {
        String sql = "SELECT COUNT(*) FROM complaints";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String[]> getCategories() {
        List<String[]> cats = new ArrayList<>();
        String sql = "SELECT id, name FROM categories ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cats.add(new String[]{rs.getString("id"), rs.getString("name")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cats;
    }

    private Complaint mapComplaint(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setId(rs.getInt("id"));
        c.setStudentId(rs.getInt("student_id"));
        c.setStudentName(rs.getString("full_name"));
        c.setStudentRoom(rs.getString("room_number"));
        c.setCategoryId(rs.getInt("category_id"));
        c.setCategoryName(rs.getString("category_name"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getString("status"));
        c.setPriority(rs.getString("priority"));
        c.setAdminRemarks(rs.getString("admin_remarks"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at"));
        return c;
    }
}