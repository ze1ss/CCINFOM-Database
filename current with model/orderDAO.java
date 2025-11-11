import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class orderDAO {

    public boolean addOrder(Order order) {
        String sql = "INSERT INTO order_header (table_id, staff_id, order_time, total_cost, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getTableId());
            stmt.setInt(2, order.getStaffId());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderTime()));
            stmt.setDouble(4, order.getTotalCost()); 
            stmt.setString(5, order.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int generatedOrderId = keys.getInt(1);
                    order.setOrderId(generatedOrderId);

                    // saves the order items for the order with the generated orderId
                    if (order.getOrderItems() != null) {
                        orderitemDAO orderitemDAO = new orderitemDAO();
                        for (OrderItem item : order.getOrderItems()) {
                            item.setOrderId(generatedOrderId); // ensures that orderId is set in each order 
                            orderitemDAO.addOrderItem(item);
                        }
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM order_header WHERE order_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int tableId = rs.getInt("table_id");
                int staffId = rs.getInt("staff_id");
                LocalDateTime orderTime = rs.getTimestamp("order_time").toLocalDateTime();
                double totalCost = rs.getDouble("total_cost"); 
                String status = rs.getString("status");

                // retrieves the order items
                orderitemDAO orderitemDAO = new orderitemDAO(); 
                List<OrderItem> orderItems = orderitemDAO.getOrderItemsByOrderId(orderId);

                return new Order(orderId, tableId, staffId, orderTime, totalCost, status, orderItems);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateOrder(Order order) {
        String sql = "UPDATE order_header SET table_id = ?, staff_id = ?, order_time = ?, total_cost = ?, status = ? WHERE order_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getTableId());
            stmt.setInt(2, order.getStaffId());
            stmt.setTimestamp(3, Timestamp.valueOf(order.getOrderTime()));
            stmt.setDouble(4, order.getTotalCost()); 
            stmt.setString(5, order.getStatus());
            stmt.setInt(6, order.getOrderId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM order_header WHERE order_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM order_header ORDER BY order_id";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            orderitemDAO orderitemDAO = new orderitemDAO();

            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setTableId(rs.getInt("table_id"));
                o.setStaffId(rs.getInt("staff_id"));
                o.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
                o.setTotalCost(rs.getDouble("total_cost")); 
                o.setStatus(rs.getString("status"));

                List<OrderItem> orderItems = orderitemDAO.getOrderItemsByOrderId(o.getOrderId());
                o.setOrderItems(orderItems);

                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
