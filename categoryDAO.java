import java.sql.*;
import java.util.ArrayList;

public class categoryDAO {
    private Connection connection;

    public boolean addCategory(Category category){
        String sql = "INSERT INTO categories (category_id, name) VALUES (?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, category.getCategoryId());
            stmt.setString(2, category.getName());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
        
    }

    public Category getCategoryById(int categoryId){
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                return new Category(categoryId, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public boolean updateCategory (Category category){
        String sql = "UPDATE categories SET name = ? WHERE category_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getCategoryId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean deleteCategory(int categoryId){
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY category_id";
        

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();

                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryName(rs.getString("name"));
                categories.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;

    }
}