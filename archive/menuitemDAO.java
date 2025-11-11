import java.sql.*;
import java.util.ArrayList;

public class menuitemDAO {

    public boolean addMenuItem(MenuItem menuItem) {
        String sql = "INSERT INTO menu_items (menu_id, category_id, menu_name, price, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menuItem.getMenuId());
            stmt.setInt(2, menuItem.getCategoryId());
            stmt.setString(3, menuItem.getMenuName());
            stmt.setDouble(4, menuItem.getPrice());
            stmt.setBoolean(5, menuItem.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public MenuItem getMenuItemById(int menuId) {
        String sql = "SELECT * FROM menu_items WHERE menu_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String menuName = rs.getString("menu_name");
                double price = rs.getDouble("price");
                boolean status = rs.getBoolean("status");

                return new MenuItem(menuId, categoryId, menuName, price, status);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateMenuItem(MenuItem menuItem) {
        String sql = "UPDATE menu_items SET category_id = ?, menu_name = ?, price = ?, status = ? WHERE menu_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menuItem.getCategoryId());
            stmt.setString(2, menuItem.getMenuName());
            stmt.setDouble(3, menuItem.getPrice());
            stmt.setBoolean(4, menuItem.getStatus());
            stmt.setInt(5, menuItem.getMenuId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteMenuItem(int menuId) {
        String sql = "DELETE FROM menu_items WHERE menu_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menuId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<MenuItem> getAllMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu_items ORDER BY menu_id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MenuItem i = new MenuItem();
                    i.setMenuId(rs.getInt("menu_id"));
                    i.setCategoryId(rs.getInt("category_id"));
                    i.setMenuName(rs.getString("menu_name"));
                    i.setPrice(rs.getDouble("price"));
                    i.setStatus(rs.getBoolean("status"));

                menuItems.add(i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuItems;
    }
}
