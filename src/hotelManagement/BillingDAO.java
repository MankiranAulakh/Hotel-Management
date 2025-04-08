package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingDAO {
	public void addBilling(int reservationId, int guestId, double totalAmount, double seasonalDiscount, String paymentMethod, String transactionStatus) {
	    String sql = "INSERT INTO Billing (reservation_id, guest_id, total_amount, seasonal_discount, payment_method, transaction_status) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, reservationId);
	    	stmt.setInt(2, guestId);
	    	stmt.setDouble(3, totalAmount);
	    	stmt.setDouble(4, seasonalDiscount);
	    	stmt.setString(5, paymentMethod);
	    	stmt.setString(6, transactionStatus);
	    	stmt.executeUpdate();
	        System.out.println("Billing record added successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void getBillingByReservationId(int reservationId) {
	    String sql = "SELECT * FROM Billing WHERE reservation_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, reservationId);
	        ResultSet resultSet = stmt.executeQuery();
	        
	        if (resultSet.next()) {
	            System.out.println("Billing ID: " + resultSet.getInt("bill_id"));
	            System.out.println("Guest ID: " + resultSet.getInt("guest_id"));
	            System.out.println("Total Amount: $" + resultSet.getDouble("total_amount"));
	            System.out.println("Seasonal Discount: $" + resultSet.getDouble("seasonal_discount"));
	            System.out.println("Payment Method: " + resultSet.getString("payment_method"));
	            System.out.println("Transaction Status: " + resultSet.getString("transaction_status"));
	        } else {
	            System.out.println("No billing record found for reservation ID: " + reservationId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void updateBillingStatus(int billingId, String transactionStatus) {
	    String sql = "UPDATE Billing SET transaction_status = ? WHERE bill_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setString(1, transactionStatus);
	    	stmt.setInt(2, billingId);
	        int rowsUpdated = stmt.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            System.out.println("Billing record updated successfully.");
	        } else {
	            System.out.println("No billing record found with ID " + billingId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void deleteBilling(int billingId) {
	    String sql = "DELETE FROM Billing WHERE bill_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setInt(1, billingId);
	        int rowsDeleted = stmt.executeUpdate();
	        
	        if (rowsDeleted > 0) {
	            System.out.println("Billing record deleted successfully.");
	        } else {
	            System.out.println("No billing record found with ID " + billingId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public String getBillingDetailsAsString(int reservationId) {
	    StringBuilder output = new StringBuilder();
	    String sql = "SELECT * FROM Billing WHERE reservation_id = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, reservationId);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            output.append("Billing ID: ").append(rs.getInt("bill_id")).append("\n")
	                  .append("Guest ID: ").append(rs.getInt("guest_id")).append("\n")
	                  .append("Total Amount: $").append(rs.getDouble("total_amount")).append("\n")
	                  .append("Seasonal Discount: $").append(rs.getDouble("seasonal_discount")).append("\n")
	                  .append("Payment Method: ").append(rs.getString("payment_method")).append("\n")
	                  .append("Transaction Status: ").append(rs.getString("transaction_status"));
	        } else {
	            output.append("No billing record found for reservation ID: ").append(reservationId);
	        }
	    } catch (SQLException e) {
	        output.append("Error: ").append(e.getMessage());
	    }
	    return output.toString();
	}




}
