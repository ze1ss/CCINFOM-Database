import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class paymentDAO {

    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, amount_paid, payment_method, payment_date, staff_id, customer_id, customer_name) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getOrderId());
            stmt.setDouble(2, payment.getAmountPaid());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setInt(5, payment.getStaffId());
            stmt.setInt(6, payment.getCustomerId());
            stmt.setString(7, payment.getCustomerName());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";

        try (Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                double amountPaid = rs.getDouble("amount_paid");
                String method = rs.getString("payment_method");
                LocalDateTime date = rs.getTimestamp("payment_date").toLocalDateTime();
                int staffId = rs.getInt("staff_id");
                int customerId = rs.getInt("customer_id");
                String customerName = rs.getString("customer_name");

                return new Payment(paymentId, orderId, amountPaid, method, date, staffId, customerId, customerName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payments SET order_id = ?, amount_paid = ?, payment_method = ?, payment_date = ?, staff_id = ?, customer_id = ?, customer_name = ? WHERE payment_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getOrderId());
            stmt.setDouble(2, payment.getAmountPaid());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setInt(5, payment.getStaffId());
            stmt.setInt(6, payment.getCustomerId());
            stmt.setString(7, payment.getCustomerName());
            stmt.setInt(8, payment.getPaymentId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paymentId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

        public ArrayList<Payment> getAllPayments() {
        ArrayList<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Payment p = new Payment(
                    rs.getInt("payment_id"),
                    rs.getInt("order_id"),
                    rs.getDouble("amount_paid"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("payment_date").toLocalDateTime(),
                    rs.getInt("staff_id"),
                    rs.getInt("customer_id"),
                    rs.getString("customer_name")
                );
                payments.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    public ArrayList<Payment> getPaymentsByOrderId(int orderId) {
        ArrayList<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE order_id = ? ORDER BY payment_date";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payment p = new Payment(
                    rs.getInt("payment_id"),
                    rs.getInt("order_id"),
                    rs.getDouble("amount_paid"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("payment_date").toLocalDateTime(),
                    rs.getInt("staff_id"),
                    rs.getInt("customer_id"),
                    rs.getString("customer_name")
                );
                payments.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }
}
