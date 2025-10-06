package config.DAO;

import config.DatabaseManager;
import config.Models.OrderDrink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDrinkDAO {

    public OrderDrink create(int orderId, int drinkId, int quantity) {
        String SQL = "INSERT INTO orderdrink (orderid, drinkid, quantity) VALUES (?, ?, ?)" +
                "RETURNING orderdrinkid";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, drinkId);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new OrderDrink(rs.getInt(1), orderId, drinkId, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OrderDrink getById(int orderDrinkId) {
        String SQL = "SELECT * FROM orderdrink WHERE orderdrinkid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderDrinkId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new OrderDrink(
                            rs.getInt("orderdrinkid"),
                            rs.getInt("orderid"),
                            rs.getInt("drinkid"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderDrink> getByOrderId(int orderId) {
        List<OrderDrink> list = new ArrayList<>();
        String SQL = "SELECT * FROM orderdrink WHERE orderid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(getById(rs.getInt("orderdrinkid")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}