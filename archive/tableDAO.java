import java.sql.*;
import java.util.ArrayList;

public class TableDAO {

    public boolean addTable(Table table) {
        String sql = "INSERT INTO tables (table_id, capacity, table_status) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, table.getTableId());
            stmt.setInt(2, table.getCapacity());
            stmt.setString(3, table.getTableStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Table getTableById(int tableId) {
        String sql = "SELECT * FROM tables WHERE table_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                String tableStatus = rs.getString("table_status");

                return new Table(tableId, capacity, tableStatus);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateTable(Table table) {
        String sql = "UPDATE tables SET capacity = ?, table_status = ? WHERE table_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, table.getCapacity());
            stmt.setString(2, table.getTableStatus());
            stmt.setInt(3, table.getTableId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteTable(int tableId) {
        String sql = "DELETE FROM tables WHERE table_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Table> getAllTables() {
        ArrayList<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables ORDER BY table_id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Table t = new Table();
                    t.setTableId(rs.getInt("table_id"));
                    t.setCapacity(rs.getInt("capacity"));
                    t.setTableStatus(rs.getString("table_status"));
                tables.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;
    }
}
