package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDAO {
	public void addInventory(String itemName, String category, int quantity, int reorderLevel, int usageFrequency) {
	    String sql = "INSERT INTO Inventory (item_name, category, quantity, reorder_level, usage_frequency) VALUES (?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setString(1, itemName);
	    	stmt.setString(2, category);
	    	stmt.setInt(3, quantity);
	    	stmt.setInt(4, reorderLevel);
	    	stmt.setInt(5, usageFrequency);
	    	stmt.executeUpdate();
	        System.out.println("Inventory item added successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void getInventoryById(int itemId) {
	    String sql = "SELECT * FROM Inventory WHERE item_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, itemId);
	        ResultSet resultSet = stmt.executeQuery();
	        
	        if (resultSet.next()) {
	            System.out.println("Item ID: " + resultSet.getInt("item_id"));
	            System.out.println("Item Name: " + resultSet.getString("item_name"));
	            System.out.println("Category: " + resultSet.getString("category"));
	            System.out.println("Quantity: " + resultSet.getInt("quantity"));
	            System.out.println("Reorder Level: " + resultSet.getInt("reorder_level"));
	            System.out.println("Usage Frequency: " + resultSet.getInt("usage_frequency"));
	        } else {
	            System.out.println("No inventory item found with ID " + itemId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void updateInventoryQuantity(int itemId, int quantity) {
	    String sql = "UPDATE Inventory SET quantity = ? WHERE item_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, quantity);
	    	stmt.setInt(2, itemId);
	        int rowsUpdated = stmt.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            System.out.println("Inventory item updated successfully.");
	        } else {
	            System.out.println("No inventory item found with ID " + itemId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void deleteInventory(int itemId) {
	    String sql = "DELETE FROM Inventory WHERE item_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, itemId);
	        int rowsDeleted = stmt.executeUpdate();
	        
	        if (rowsDeleted > 0) {
	            System.out.println("Inventory item deleted successfully.");
	        } else {
	            System.out.println("No inventory item found with ID " + itemId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}





}
