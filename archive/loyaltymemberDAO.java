import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class loyaltymemberDAO {

    public boolean addLoyaltyMember(LoyaltyMember member) {
        String sql = "INSERT INTO loyalty_members (member_id, name, contact, join_date, points, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, member.getMemberId());
            stmt.setString(2, member.getName());
            stmt.setString(3, member.getContact());
            stmt.setDate(4, Date.valueOf(member.getJoinDate())); 
            stmt.setInt(5, member.getPoints());
            stmt.setString(6, member.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public LoyaltyMember getLoyaltyMemberById(int memberId) {
        String sql = "SELECT * FROM loyalty_members WHERE member_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                LocalDate joinDate = rs.getDate("join_date").toLocalDate();
                int points = rs.getInt("points");
                String status = rs.getString("status");

                return new LoyaltyMember(memberId, name, contact, joinDate, points, status);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateLoyaltyMember(LoyaltyMember member) {
        String sql = "UPDATE loyalty_members SET name = ?, contact = ?, join_date = ?, points = ?, status = ? WHERE member_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setString(2, member.getContact());
            stmt.setDate(3, Date.valueOf(member.getJoinDate()));
            stmt.setInt(4, member.getPoints());
            stmt.setString(5, member.getStatus());
            stmt.setInt(6, member.getMemberId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteLoyaltyMember(int memberId) {
        String sql = "DELETE FROM loyalty_members WHERE member_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<LoyaltyMember> getAllLoyaltyMembers() {
        ArrayList<LoyaltyMember> members = new ArrayList<>();
        String sql = "SELECT * FROM loyalty_members ORDER BY member_id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoyaltyMember m = new LoyaltyMember();

                    m.setMemberId(rs.getInt("member_id"));
                    m.setName(rs.getString("name"));
                    m.setContact(rs.getString("contact"));
                    m.setJoinDate(rs.getDate("join_date").toLocalDate());
                    m.setPoints(rs.getInt("points"));
                    m.setStatus(rs.getString("status"));

                    members.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }
}
