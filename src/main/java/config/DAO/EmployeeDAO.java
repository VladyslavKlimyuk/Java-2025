package config.DAO;

import config.DatabaseManager;
import config.Models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public Employee create(Employee employee) {
        String SQL = "INSERT INTO employee (firstname, lastname, middlename, phonenumber, address, positionid) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING employeeid";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getMiddleName());
            pstmt.setString(4, employee.getPhoneNumber());
            pstmt.setString(5, employee.getAddress());
            pstmt.setInt(6, employee.getPositionId());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    employee.setEmployeeId(rs.getInt(1));
                    return employee;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee getById(int employeeId) {
        String SQL = "SELECT * FROM employee WHERE employeeid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, employeeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("employeeid"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("middlename"),
                            rs.getString("phonenumber"),
                            rs.getString("address"),
                            rs.getInt("positionid")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String SQL = "SELECT * FROM employee";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                employees.add(getById(rs.getInt("employeeid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public boolean update(Employee employee) {
        String SQL = "UPDATE employee SET firstname=?, lastname=?, middlename=?, phonenumber=?, address=?," +
                "positionid=? WHERE employeeid=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getMiddleName());
            pstmt.setString(4, employee.getPhoneNumber());
            pstmt.setString(5, employee.getAddress());
            pstmt.setInt(6, employee.getPositionId());
            pstmt.setInt(7, employee.getEmployeeId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int employeeId) {
        String SQL = "DELETE FROM employee WHERE employeeid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, employeeId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Інформація про всіх працівників з конкретною посадою
    public List<Employee> getEmployeesByPosition(int positionId) {
        List<Employee> employees = new ArrayList<>();
        String SQL = "SELECT * FROM employee WHERE positionid = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, positionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(getById(rs.getInt("employeeid")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}