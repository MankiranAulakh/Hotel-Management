package hotelManagement;

import hotelManagement.InventoryItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
	
	public List<InventoryItem> getInventoryList() {
	    List<InventoryItem> inventoryItems = new ArrayList<>();
	    String sql = "SELECT * FROM inventory"; // Assuming you have an 'inventory' table
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        while (rs.next()) {
	            InventoryItem item = new InventoryItem(
	                rs.getInt("item_id"),
	                rs.getString("item_name"),
	                rs.getString("category"),
	                rs.getInt("quantity"),
	                rs.getInt("reorder_level"),
	                rs.getInt("usage_frequency")
	            );
	            inventoryItems.add(item);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return inventoryItems;
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
	
	public void checkLowStock() {
	    String sql = "SELECT item_id, item_name, quantity, reorder_level FROM Inventory WHERE quantity < reorder_level";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            int itemId = rs.getInt("item_id");
	            String itemName = rs.getString("item_name");
	            int quantity = rs.getInt("quantity");
	            int reorderLevel = rs.getInt("reorder_level");
	            
	            if (quantity < reorderLevel) {
	                System.out.println("ALERT: " + itemName + " is below reorder level. Current stock: " + quantity);
	                // You can integrate an email or notification service here for real-time alerts
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public void updateUsageFrequency(int itemId, int quantityUsed) {
	    String sql = "UPDATE Inventory SET quantity = quantity - ?, usage_frequency = usage_frequency + ? WHERE item_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, quantityUsed);
	        stmt.setInt(2, quantityUsed);
	        stmt.setInt(3, itemId);
	        int rowsUpdated = stmt.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            System.out.println("Usage frequency updated successfully.");
	        } else {
	            System.out.println("No inventory item found with ID " + itemId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void generateInventoryReport() {
	    String sql = "SELECT * FROM Inventory ORDER BY category, item_name";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {
	        
	        System.out.println("Inventory Report:\n");
	        while (rs.next()) {
	            System.out.println("Item Name: " + rs.getString("item_name"));
	            System.out.println("Category: " + rs.getString("category"));
	            System.out.println("Quantity: " + rs.getInt("quantity"));
	            System.out.println("Reorder Level: " + rs.getInt("reorder_level"));
	            System.out.println("Usage Frequency: " + rs.getInt("usage_frequency"));
	            System.out.println("-------------------------");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}




}
