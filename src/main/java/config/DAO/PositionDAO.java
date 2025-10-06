package config.DAO;

import config.DatabaseManager;
import config.Models.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionDAO {

    public Position create(String titleUa, String titleEn) {
        String SQL = "INSERT INTO position (title_ua, title_en) VALUES (?, ?) RETURNING positionid";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, titleUa);
            pstmt.setString(2, titleEn);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Position(rs.getInt(1), titleUa, titleEn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Position getById(int positionId) {
        String SQL = "SELECT * FROM position WHERE positionid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, positionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Position(
                            rs.getInt("positionid"),
                            rs.getString("title_ua"),
                            rs.getString("title_en")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Position> getAll() {
        List<Position> positions = new ArrayList<>();
        String SQL = "SELECT * FROM position";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                positions.add(getById(rs.getInt("positionid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }

    public boolean update(Position position) {
        String SQL = "UPDATE position SET title_ua = ?, title_en = ? WHERE positionid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, position.getTitleUa());
            pstmt.setString(2, position.getTitleEn());
            pstmt.setInt(3, position.getPositionId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int positionId) {
        String SQL = "DELETE FROM position WHERE positionid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, positionId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}