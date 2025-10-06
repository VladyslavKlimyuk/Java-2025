package config.DAO;

import config.DatabaseManager;
import config.Models.Drink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO {

    public Drink create(String nameUa, String nameEn, double price) {
        String SQL = "INSERT INTO drink (name_ua, name_en, price) VALUES (?, ?, ?) RETURNING drinkid";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nameUa);
            pstmt.setString(2, nameEn);
            pstmt.setDouble(3, price);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Drink(rs.getInt(1), nameUa, nameEn, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Drink getById(int drinkId) {
        String SQL = "SELECT * FROM drink WHERE drinkid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, drinkId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Drink(
                            rs.getInt("drinkid"),
                            rs.getString("name_ua"),
                            rs.getString("name_en"),
                            rs.getDouble("price")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Drink> getAll() {
        List<Drink> drinks = new ArrayList<>();
        String SQL = "SELECT * FROM drink";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                drinks.add(getById(rs.getInt("drinkid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drinks;
    }

    public boolean update(Drink drink) {
        String SQL = "UPDATE drink SET name_ua = ?, name_en = ?, price = ? WHERE drinkid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, drink.getNameUa());
            pstmt.setString(2, drink.getNameEn());
            pstmt.setDouble(3, drink.getPrice());
            pstmt.setInt(4, drink.getDrinkId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int drinkId) {
        String SQL = "DELETE FROM drink WHERE drinkid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, drinkId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3 найулюбленіші напої за попередній місяць
    public List<Drink> getTop3FavoriteLastMonth() {
        List<Drink> drinks = new ArrayList<>();
        String SQL = """
            SELECT d.drinkid, d.name_ua, d.name_en, d.price, SUM(od.quantity) as total_ordered
            FROM drink d
            JOIN orderdrink od ON d.drinkid = od.drinkid
            JOIN "Order" o ON od.orderid = o.orderid
            WHERE o.ordertimestamp >= (NOW() - INTERVAL '30 days')
            GROUP BY d.drinkid, d.name_ua, d.name_en, d.price
            ORDER BY total_ordered DESC
            LIMIT 3
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                drinks.add(new Drink(
                        rs.getInt("drinkid"),
                        rs.getString("name_ua"),
                        rs.getString("name_en"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drinks;
    }
}