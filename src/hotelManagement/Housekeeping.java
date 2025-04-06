package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Housekeeping {

	public void addHousekeeping(int roomId, String assignedStaff, String cleaningStatus, String lastCleaned) {
	    String sql = "INSERT INTO Housekeeping (room_id, assigned_staff, cleaning_status, last_cleaned) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, roomId);
	    	stmt.setString(2, assignedStaff);
	    	stmt.setString(3, cleaningStatus);
	    	stmt.setString(4, lastCleaned);
	    	stmt.executeUpdate();
	        System.out.println("Housekeeping record added successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public void getHousekeepingById(int housekeepingId) {
	    String sql = "SELECT * FROM Housekeeping WHERE housekeeping_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, housekeepingId);
	        ResultSet resultSet = stmt.executeQuery();
	        
	        if (resultSet.next()) {
	            System.out.println("Housekeeping ID: " + resultSet.getInt("housekeeping_id"));
	            System.out.println("Room ID: " + resultSet.getInt("room_id"));
	            System.out.println("Assigned Staff: " + resultSet.getString("assigned_staff"));
	            System.out.println("Cleaning Status: " + resultSet.getString("cleaning_status"));
	            System.out.println("Last Cleaned: " + resultSet.getString("last_cleaned"));
	        } else {
	            System.out.println("No housekeeping record found with ID " + housekeepingId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public void updateHousekeepingStatus(int housekeepingId, String cleaningStatus, String lastCleaned) {
	    String sql = "UPDATE Housekeeping SET cleaning_status = ?, last_cleaned = ? WHERE housekeeping_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setString(1, cleaningStatus);
	    	stmt.setString(2, lastCleaned);
	    	stmt.setInt(3, housekeepingId);
	        int rowsUpdated = stmt.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            System.out.println("Housekeeping record updated successfully.");
	        } else {
	            System.out.println("No housekeeping record found with ID " + housekeepingId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public void deleteHousekeeping(int housekeepingId) {
	    String sql = "DELETE FROM Housekeeping WHERE housekeeping_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, housekeepingId);
	        int rowsDeleted = stmt.executeUpdate();
	        
	        if (rowsDeleted > 0) {
	            System.out.println("Housekeeping record deleted successfully.");
	        } else {
	            System.out.println("No housekeeping record found with ID " + housekeepingId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
