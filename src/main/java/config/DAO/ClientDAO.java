package config.DAO;

import config.DatabaseManager;
import config.Models.Client;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public Client create(Client client) {
        String SQL = "INSERT INTO client (firstname, lastname, middlename," +
                "dateofbirth, phonenumber, address, discount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING clientid";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getMiddleName());
            pstmt.setObject(4, client.getDateOfBirth() != null ?
                    Date.valueOf(client.getDateOfBirth()) : null, Types.DATE);
            pstmt.setString(5, client.getPhoneNumber());
            pstmt.setString(6, client.getAddress());
            pstmt.setDouble(7, client.getDiscount());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setClientId(rs.getInt(1));
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client getById(int clientId) {
        String SQL = "SELECT * FROM client WHERE clientid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, clientId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("clientid"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("middlename"),
                            rs.getDate("dateofbirth") != null ?
                                    rs.getDate("dateofbirth").toLocalDate() : null,
                            rs.getString("phonenumber"),
                            rs.getString("address"),
                            rs.getDouble("discount")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String SQL = "SELECT * FROM client";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                clients.add(getById(rs.getInt("clientid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean update(Client client) {
        String SQL = "UPDATE client SET firstname=?, lastname=?, middlename=?," +
                "dateofbirth=?, phonenumber=?, address=?, discount=? WHERE clientid=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getMiddleName());
            pstmt.setObject(4, client.getDateOfBirth() != null ?
                    Date.valueOf(client.getDateOfBirth()) : null, Types.DATE);
            pstmt.setString(5, client.getPhoneNumber());
            pstmt.setString(6, client.getAddress());
            pstmt.setDouble(7, client.getDiscount());
            pstmt.setInt(8, client.getClientId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int clientId) {
        String SQL = "DELETE FROM client WHERE clientid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, clientId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Інформація про постійного клієнта
    public List<Client> getFrequentClients() {
        List<Client> clients = new ArrayList<>();

        String SQL = """
            SELECT c.clientid
            FROM client c
            JOIN "Order" o ON c.clientid = o.clientid
            WHERE o.ordertimestamp >= (NOW() - INTERVAL '7 days')
            GROUP BY c.clientid
            HAVING COUNT(o.orderid) >= 3
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                clients.add(getById(rs.getInt("clientid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}