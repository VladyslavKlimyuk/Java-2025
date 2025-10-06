package config.DAO;

import config.DatabaseManager;
import config.Models.OrderDessert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDessertDAO {

    public OrderDessert create(int orderId, int dessertId, int quantity) {
        String SQL = "INSERT INTO orderdessert (orderid, dessertid, quantity) VALUES (?, ?, ?)" +
                "RETURNING orderdessertid";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, dessertId);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new OrderDessert(rs.getInt(1), orderId, dessertId, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OrderDessert getById(int orderDessertId) {
        String SQL = "SELECT * FROM orderdessert WHERE orderdessertid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderDessertId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new OrderDessert(
                            rs.getInt("orderdessertid"),
                            rs.getInt("orderid"),
                            rs.getInt("dessertid"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderDessert> getByOrderId(int orderId) {
        List<OrderDessert> list = new ArrayList<>();
        String SQL = "SELECT * FROM orderdessert WHERE orderid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(getById(rs.getInt("orderdessertid")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}