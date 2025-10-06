package config.DAO;

import config.DatabaseManager;
import config.Models.Order;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public Order create(int employeeId, Integer clientId, double totalAmount) {
        String SQL = "INSERT INTO \"Order\" (employeeid, clientid, totalamount) VALUES (?, ?, ?)" +
                "RETURNING orderid, ordertimestamp";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, employeeId);
            pstmt.setObject(2, clientId, Types.INTEGER);
            pstmt.setDouble(3, totalAmount);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("orderid"),
                            rs.getTimestamp("ordertimestamp").toLocalDateTime(),
                            employeeId,
                            clientId,
                            totalAmount
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order getById(int orderId) {
        String SQL = "SELECT * FROM \"Order\" WHERE orderid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("orderid"),
                            rs.getTimestamp("ordertimestamp").toLocalDateTime(),
                            rs.getInt("employeeid"),
                            (Integer) rs.getObject("clientid"),
                            rs.getDouble("totalamount")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String SQL = "SELECT * FROM \"Order\"";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                orders.add(getById(rs.getInt("orderid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean update(Order order) {
        String SQL = "UPDATE \"Order\" SET employeeid = ?, clientid = ?, totalamount = ? WHERE orderid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, order.getEmployeeId());
            pstmt.setObject(2, order.getClientId(), Types.INTEGER);
            pstmt.setDouble(3, order.getTotalAmount());
            pstmt.setInt(4, order.getOrderId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int orderId) {
        String SQL = "DELETE FROM \"Order\" WHERE orderid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Середня сума замовлень на конкретний день
    public double getAverageOrderAmountByDate(LocalDate date) {
        String SQL = "SELECT COALESCE(AVG(totalamount), 0.0) FROM \"Order\" WHERE DATE(ordertimestamp) = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Інформація про найбільше або найбільші замовлення на конкретну дату
    public List<Order> getLargestOrdersByDate(LocalDate date) {
        List<Order> orders = new ArrayList<>();

        String SQL = """
            SELECT * FROM "Order"
            WHERE DATE(ordertimestamp) = ?
            AND totalamount = (
                SELECT MAX(totalamount)
                FROM "Order"
                WHERE DATE(ordertimestamp) = ?
            )
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setDate(2, Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(getById(rs.getInt("orderid")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}