package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PricingDAO {
	public void addPricing(String roomType, double baseRate, double seasonalRate, String effectiveFrom, String effectiveTo) {
	    String sql = "INSERT INTO Pricing (room_type, base_rate, seasonal_rate, effective_from, effective_to) VALUES (?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setString(1, roomType);
	    	stmt.setDouble(2, baseRate);
	    	stmt.setDouble(3, seasonalRate);
	    	stmt.setString(4, effectiveFrom);
	    	stmt.setString(5, effectiveTo);
	    	stmt.executeUpdate();
	        System.out.println("Pricing rule added successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void getPricingByRoomType(String roomType) {
	    String sql = "SELECT * FROM Pricing WHERE room_type = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setString(1, roomType);
	        ResultSet resultSet = stmt.executeQuery();
	        
	        if (resultSet.next()) {
	            System.out.println("Room Type: " + resultSet.getString("room_type"));
	            System.out.println("Base Rate: $" + resultSet.getDouble("base_rate"));
	            System.out.println("Seasonal Rate: $" + resultSet.getDouble("seasonal_rate"));
	            System.out.println("Effective From: " + resultSet.getString("effective_from"));
	            System.out.println("Effective To: " + resultSet.getString("effective_to"));
	        } else {
	            System.out.println("No pricing rule found for room type: " + roomType);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void updatePricing(String roomType, double baseRate, double seasonalRate) {
	    String sql = "UPDATE Pricing SET base_rate = ?, seasonal_rate = ? WHERE room_type = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setDouble(1, baseRate);
	    	stmt.setDouble(2, seasonalRate);
	    	stmt.setString(3, roomType);
	        int rowsUpdated = stmt.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            System.out.println("Pricing rule updated successfully.");
	        } else {
	            System.out.println("No pricing rule found for room type: " + roomType);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void deletePricing(String roomType) {
	    String sql = "DELETE FROM Pricing WHERE room_type = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setString(1, roomType);
	        int rowsDeleted = stmt.executeUpdate();
	        
	        if (rowsDeleted > 0) {
	            System.out.println("Pricing rule deleted successfully.");
	        } else {
	            System.out.println("No pricing rule found for room type: " + roomType);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}





}
