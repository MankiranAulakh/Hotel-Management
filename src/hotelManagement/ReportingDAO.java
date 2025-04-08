package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportingDAO {

    // Reservations Report (example: total reservations, total cancellations)
	public String generateReservationsReport(String startDate, String endDate) {
	    String reportContent = "";
	    String sql = "SELECT * FROM Reservations WHERE check_in BETWEEN ? AND ?";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, startDate);
	        stmt.setString(2, endDate);
	        
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	            reportContent += "Reservation ID: " + rs.getInt("reservation_id") + "\n";
	            reportContent += "Guest ID: " + rs.getInt("guest_id") + "\n";
	            reportContent += "Room ID: " + rs.getInt("room_id") + "\n";
	            reportContent += "Check-In Date: " + rs.getDate("check_in") + "\n";
	            reportContent += "Check-Out Date: " + rs.getDate("check_out") + "\n";
	            reportContent += "Total Amount: " + rs.getDouble("total_amount") + "\n";
	            reportContent += "Payment Status: " + rs.getString("payment_status") + "\n";
	            reportContent += "Special Requests: " + rs.getString("special_requests") + "\n";
	            reportContent += "Status: " + rs.getString("status") + "\n";
	            reportContent += "----------------------------\n";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        reportContent = "Error generating report: " + e.getMessage();
	    }

	    return reportContent;
	}


    // Room Occupancy Report (example: total rooms, occupied, available)
	public String generateRoomOccupancyReport(String startDate, String endDate) {
	    String reportContent = "";
	    String sql = "SELECT r.room_number, r.room_type, COUNT(res.room_id) AS occupancy_count " +
	                 "FROM Rooms r LEFT JOIN Reservations res ON r.room_id = res.room_id " +
	                 "WHERE res.check_in BETWEEN ? AND ? GROUP BY r.room_number";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, startDate);
	        stmt.setString(2, endDate);
	        
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	            reportContent += "Room Number: " + rs.getString("room_number") + "\n";
	            reportContent += "Room Type: " + rs.getString("room_type") + "\n";
	            reportContent += "Occupancy Count: " + rs.getInt("occupancy_count") + "\n";
	            reportContent += "----------------------------\n";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        reportContent = "Error generating report: " + e.getMessage();
	    }

	    return reportContent;
	}


    // Housekeeping Report (example: total cleaned, pending cleaning)
    public String generateHousekeepingReport(String startDate, String endDate) {
        String reportContent = "";
        String sql = "SELECT r.room_number, h.assigned_staff, h.cleaning_status, h.last_cleaned " +
                     "FROM Housekeeping h JOIN Rooms r ON h.room_id = r.room_id " +
                     "WHERE h.last_cleaned BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reportContent += "Room Number: " + rs.getString("room_number") + "\n";
                reportContent += "Assigned Staff: " + rs.getString("assigned_staff") + "\n";
                reportContent += "Cleaning Status: " + rs.getString("cleaning_status") + "\n";
                reportContent += "Last Cleaned: " + rs.getTimestamp("last_cleaned") + "\n";
                reportContent += "----------------------------\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            reportContent = "Error generating report: " + e.getMessage();
        }

        return reportContent;
    }


    // Financial Report (example: total revenue, total expenses)
    public String generateFinancialReport(String startDate, String endDate) {
        String reportContent = "";
        String sql = "SELECT b.bill_id, r.reservation_id, g.first_name, g.last_name, b.total_amount, " +
                     "b.seasonal_discount, b.final_amount, r.payment_status " +  // Changed to r.payment_status
                     "FROM Billing b " +
                     "JOIN Reservations r ON b.reservation_id = r.reservation_id " +
                     "JOIN Guests g ON b.guest_id = g.guest_id " +
                     "WHERE b.created_at BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reportContent += "Bill ID: " + rs.getInt("bill_id") + "\n";
                reportContent += "Reservation ID: " + rs.getInt("reservation_id") + "\n";
                reportContent += "Guest Name: " + rs.getString("first_name") + " " + rs.getString("last_name") + "\n";
                reportContent += "Total Amount: " + rs.getDouble("total_amount") + "\n";
                reportContent += "Seasonal Discount: " + rs.getDouble("seasonal_discount") + "\n";
                reportContent += "Final Amount: " + rs.getDouble("final_amount") + "\n";
                reportContent += "Payment Status: " + rs.getString("payment_status") + "\n"; // Now from Reservations
                reportContent += "----------------------------\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            reportContent = "Error generating report: " + e.getMessage();
        }

        return reportContent;
    }


}
