package config.DAO;

import config.DatabaseManager;
import config.Models.Dessert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DessertDAO {

    public Dessert create(String nameUa, String nameEn, double price) {
        String SQL = "INSERT INTO dessert (name_ua, name_en, price) VALUES (?, ?, ?) RETURNING dessertid";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nameUa);
            pstmt.setString(2, nameEn);
            pstmt.setDouble(3, price);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Dessert(rs.getInt(1), nameUa, nameEn, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dessert getById(int dessertId) {
        String SQL = "SELECT * FROM dessert WHERE dessertid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, dessertId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Dessert(
                            rs.getInt("dessertid"),
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

    public List<Dessert> getAll() {
        List<Dessert> desserts = new ArrayList<>();
        String SQL = "SELECT * FROM dessert";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                desserts.add(getById(rs.getInt("dessertid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return desserts;
    }

    public boolean update(Dessert dessert) {
        String SQL = "UPDATE dessert SET name_ua = ?, name_en = ?, price = ? WHERE dessertid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, dessert.getNameUa());
            pstmt.setString(2, dessert.getNameEn());
            pstmt.setDouble(3, dessert.getPrice());
            pstmt.setInt(4, dessert.getDessertId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int dessertId) {
        String SQL = "DELETE FROM dessert WHERE dessertid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, dessertId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5 найулюбленіших десертів за попередні 10 днів
    public List<Dessert> getTop5FavoriteLast10Days() {
        List<Dessert> desserts = new ArrayList<>();
        String SQL = """
            SELECT ds.dessertid, ds.name_ua, ds.name_en, ds.price, SUM(od.quantity) as total_ordered
            FROM dessert ds
            JOIN orderdessert od ON ds.dessertid = od.dessertid
            JOIN "Order" o ON od.orderid = o.orderid
            WHERE o.ordertimestamp >= (NOW() - INTERVAL '10 days')
            GROUP BY ds.dessertid, ds.name_ua, ds.name_en, ds.price
            ORDER BY total_ordered DESC
            LIMIT 5
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                desserts.add(new Dessert(
                        rs.getInt("dessertid"),
                        rs.getString("name_ua"),
                        rs.getString("name_en"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return desserts;
    }
}