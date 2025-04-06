package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Maintenance {
	public void addMaintenance(int roomId, String issueDescription, String scheduledDate, String status) {
	    String sql = "INSERT INTO Maintenance (room_id, issue_description, scheduled_date, status) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
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
	
	public void getMaintenanceById(int maintenanceId) {
	    String sql = "SELECT * FROM Maintenance WHERE maintenance_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
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
	
	public void updateMaintenanceStatus(int maintenanceId, String status) {
	    String sql = "UPDATE Maintenance SET status = ? WHERE maintenance_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setString(1, status);
	    	stmt.setInt(2, maintenanceId);
	        int rowsUpdated = stmt.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            System.out.println("Maintenance record updated successfully.");
	        } else {
	            System.out.println("No maintenance record found with ID " + maintenanceId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void deleteMaintenance(int maintenanceId) {
	    String sql = "DELETE FROM Maintenance WHERE maintenance_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
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




}
