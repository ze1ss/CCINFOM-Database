import java.sql.*;
import java.util.ArrayList;

public class staffDAO {

    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO staff (staff_id, name, email, contact, position, manager_pin) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staff.getStaffId());
            stmt.setString(2, staff.getName());
            stmt.setString(3, staff.getEmail());
            stmt.setString(4, staff.getContact());
            stmt.setString(5, staff.getPosition());
            stmt.setString(6, staff.getManagerPin());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Staff getStaffById(int staffId) {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String contact = rs.getString("contact");
                String position = rs.getString("position");
                String managerPin = rs.getString("manager_pin");

                return new Staff(staffId, name, email, contact, position, managerPin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET name = ?, email = ?, contact = ?, position = ?, manager_pin = ? WHERE staff_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getEmail());
            stmt.setString(3, staff.getContact());
            stmt.setString(4, staff.getPosition());
            stmt.setString(5, staff.getManagerPin());
            stmt.setInt(6, staff.getStaffId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteStaff(int staffId) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Staff> getAllStaff() {
        ArrayList<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY staff_id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Staff s = new Staff();
                    s.setStaffId(rs.getInt("staff_id"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setContact(rs.getString("contact"));
                    s.setPosition(rs.getString("position"));
                    s.setManagerPin(rs.getString("manager_pin"));
                    staffList.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staffList;
    }
}
