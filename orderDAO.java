import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class orderDAO {

    public boolean addOrder(Order order) {
        String sql = "INSERT INTO orders (order_id, table_id, staff_id, member_id, order_datetime, order_status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getOrderId());
            stmt.setInt(2, order.getTableId());
            stmt.setInt(3, order.getStaffId());
            stmt.setInt(4, order.getMemberId());
            stmt.setTimestamp(5, Timestamp.valueOf(order.getOrderDatetime())); 
            stmt.setString(6, order.getOrderStatus());

            int affectedRows = stmt.executeUpdate();

            // saves the order items for the order
            if (affectedRows > 0 && order.getOrderItems() != null) {
                orderitemDAO orderitemDAO = new orderitemDAO();
                for (OrderItem item : order.getOrderItems()) {
                    orderitemDAO.addOrderItem(item);
                }
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int tableId = rs.getInt("table_id");
                int staffId = rs.getInt("staff_id");
                int memberId = rs.getInt("member_id");
                LocalDateTime orderDatetime = rs.getTimestamp("order_datetime").toLocalDateTime();
                String orderStatus = rs.getString("order_status");

                // retrieves the order items
                orderitemDAO orderitemDAO = new orderitemDAO();
                List<OrderItem> orderItems = orderitemDAO.getOrderItemsByOrderId(orderId);

                return new Order(orderId, tableId, staffId, memberId, orderDatetime, orderStatus, orderItems);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET table_id = ?, staff_id = ?, member_id = ?, order_datetime = ?, order_status = ? WHERE order_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getTableId());
            stmt.setInt(2, order.getStaffId());
            stmt.setInt(3, order.getMemberId());
            stmt.setTimestamp(4, Timestamp.valueOf(order.getOrderDatetime()));
            stmt.setString(5, order.getOrderStatus());
            stmt.setInt(6, order.getOrderId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";

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
        String sql = "SELECT * FROM orders ORDER BY order_id";

        try (Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            orderitemDAO orderitemDAO = new orderitemDAO();

            while (rs.next()) {
                Order o = new Order();

                o.setOrderId(rs.getInt("order_id"));
                o.setTableId(rs.getInt("table_id"));
                o.setStaffId(rs.getInt("staff_id"));
                o.setMemberId(rs.getInt("member_id"));
                o.setOrderDatetime(rs.getTimestamp("order_datetime").toLocalDateTime());
                o.setOrderStatus(rs.getString("order_status"));

                List<OrderItem> orderItems = orderitemDAO.getOrderItemsByOrderId(order.getOrderId());
                order.setOrderItems(orderItems);

                orders.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}
