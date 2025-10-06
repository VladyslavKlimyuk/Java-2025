package config.DAO;

import config.DatabaseManager;
import config.Models.WorkSchedule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkScheduleDAO {

    // Вспомогательный DTO для отображения расписания с именем и должностью
    public static class ScheduleView {
        public String fullName;
        public String position;
        public LocalDate workDate;
        public Time startTime;
        public Time endTime;

        @Override
        public String toString() {
            return String.format("%-20s | %-15s | %s | %s - %s",
                    fullName, position, workDate, startTime, endTime);
        }
    }

    public WorkSchedule create(int employeeId, LocalDate workDate, Time startTime, Time endTime) {
        String SQL = "INSERT INTO workschedule (employeeid, workdate, starttime, endtime) " +
                "VALUES (?, ?, ?, ?) RETURNING scheduleid";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, employeeId);
            pstmt.setDate(2, Date.valueOf(workDate));
            pstmt.setTime(3, startTime);
            pstmt.setTime(4, endTime);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new WorkSchedule(rs.getInt(1), employeeId, workDate, startTime, endTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public WorkSchedule getById(int scheduleId) {
        String SQL = "SELECT * FROM workschedule WHERE scheduleid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, scheduleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new WorkSchedule(
                            rs.getInt("scheduleid"),
                            rs.getInt("employeeid"),
                            rs.getDate("workdate").toLocalDate(),
                            rs.getTime("starttime"),
                            rs.getTime("endtime")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WorkSchedule> getAll() {
        List<WorkSchedule> schedule = new ArrayList<>();
        String SQL = "SELECT * FROM workschedule";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                schedule.add(getById(rs.getInt("scheduleid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    public boolean update(WorkSchedule schedule) {
        String SQL = "UPDATE workschedule SET employeeid = ?, workdate = ?, starttime = ?, endtime = ?" +
                "WHERE scheduleid = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, schedule.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(schedule.getWorkDate()));
            pstmt.setTime(3, schedule.getStartTime());
            pstmt.setTime(4, schedule.getEndTime());
            pstmt.setInt(5, schedule.getScheduleId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int scheduleId) {
        String SQL = "DELETE FROM workschedule WHERE scheduleid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, scheduleId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Розклад роботи для усіх працівників на довільну дату
    public List<ScheduleView> getScheduleByDate(LocalDate date) {
        List<ScheduleView> schedule = new ArrayList<>();

        String SQL = """
            SELECT ws.workdate, ws.starttime, ws.endtime, e.firstname, e.lastname, p.title_ua 
            FROM workschedule ws
            JOIN employee e ON ws.employeeid = e.employeeid
            JOIN position p ON e.positionid = p.positionid
            WHERE ws.workdate = ?
            ORDER BY p.title_ua, e.lastname, ws.starttime
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ScheduleView view = new ScheduleView();
                    view.fullName = rs.getString("firstname") + " " +
                            rs.getString("lastname");
                    view.position = rs.getString("title_ua");
                    view.workDate = rs.getDate("workdate").toLocalDate();
                    view.startTime = rs.getTime("starttime");
                    view.endTime = rs.getTime("endtime");
                    schedule.add(view);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedule;
    }
}