package hotelManagement.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import hotelManagement.DatabaseConnection;
import hotelManagement.model.Maintenance;

public class MaintenanceDAO {
    // Method to add a new maintenance record
    public void addMaintenance(int roomId, String issueDescription, String scheduledDate, String status) {
        String sql = "INSERT INTO Maintenance (room_id, issue_description, scheduled_date, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.setString(2, issueDescription);
            stmt.setString(3, scheduledDate);
            stmt.setString(4, status);
            stmt.executeUpdate();
            System.out.println("Maintenance record added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a maintenance record by its ID
    public void getMaintenanceById(int maintenanceId) {
        String sql = "SELECT * FROM Maintenance WHERE maintenance_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maintenanceId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                System.out.println("Maintenance ID: " + resultSet.getInt("maintenance_id"));
                System.out.println("Room ID: " + resultSet.getInt("room_id"));
                System.out.println("Issue Description: " + resultSet.getString("issue_description"));
                System.out.println("Scheduled Date: " + resultSet.getString("scheduled_date"));
                System.out.println("Status: " + resultSet.getString("status"));
            } else {
                System.out.println("No maintenance record found with ID " + maintenanceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a list of all maintenance records
    public List<Maintenance> getMaintenanceList() {
        List<Maintenance> maintenanceList = new ArrayList<>();
        String sql = "SELECT * FROM Maintenance";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Maintenance maintenance = new Maintenance(
                    rs.getInt("maintenance_id"),
                    rs.getInt("room_id"),
                    rs.getString("issue_description"),
                    rs.getString("scheduled_date"),
                    rs.getString("status")
                );
                maintenanceList.add(maintenance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maintenanceList;
    }

    // Method to update the status of a maintenance record
    public void updateMaintenanceStatus(int maintenanceId, String status) {
        String sql = "UPDATE Maintenance SET status = ? WHERE maintenance_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, maintenanceId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Maintenance record updated successfully.");
            } else {
                System.out.println("No maintenance record found with ID: " + maintenanceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    // Method to delete a maintenance record
    public void deleteMaintenance(int maintenanceId) {
        String sql = "DELETE FROM Maintenance WHERE maintenance_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maintenanceId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Maintenance record deleted successfully.");
            } else {
                System.out.println("No maintenance record found with ID " + maintenanceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Additional method to check for any pending maintenance tasks
    public void checkPendingMaintenance() {
        String sql = "SELECT maintenance_id, room_id, issue_description, scheduled_date FROM Maintenance WHERE status = 'Pending'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("Pending Maintenance ID: " + rs.getInt("maintenance_id"));
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Issue: " + rs.getString("issue_description"));
                System.out.println("Scheduled Date: " + rs.getString("scheduled_date"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
