package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationDAO {

    // CREATE (Insert a Reservation)
    public void addReservation(int guestId, int roomId, String checkIn, String checkOut, double totalAmount, String paymentStatus) {
        String sql = "INSERT INTO Reservations (guest_id, room_id, check_in, check_out, total_amount, payment_status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
            stmt.setInt(2, roomId);
            stmt.setString(3, checkIn);
            stmt.setString(4, checkOut);
            stmt.setDouble(5, totalAmount);
            stmt.setString(6, paymentStatus);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(rows + " reservation(s) added successfully.");
            } else {
                System.out.println("Failed to add reservation.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (Retrieve Reservation Details)
    public void getReservationById(int reservationId) {
        String sql = "SELECT * FROM Reservations WHERE reservation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Guest ID: " + rs.getInt("guest_id"));
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Check-in Date: " + rs.getDate("check_in"));
                System.out.println("Check-out Date: " + rs.getDate("check_out"));
                System.out.println("Total Amount: " + rs.getDouble("total_amount"));
                System.out.println("Payment Status: " + rs.getString("payment_status"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("---------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateReservation(int reservationId, int guestId, int roomId, String checkIn, String checkOut, double totalAmount, String paymentStatus) {
        String sql = "UPDATE Reservations SET guest_id = ?, room_id = ?, check_in = ?, check_out = ?, total_amount = ?, payment_status = ? WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the parameters for the prepared statement
            stmt.setInt(1, guestId);
            stmt.setInt(2, roomId);
            stmt.setString(3, checkIn);
            stmt.setString(4, checkOut);
            stmt.setDouble(5, totalAmount);
            stmt.setString(6, paymentStatus);
            stmt.setInt(7, reservationId); // Update the reservation based on reservation_id

            // Execute the update
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Reservation updated successfully!");
            } else {
                System.out.println("No reservation found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating reservation: " + e.getMessage());
        }
    }

    // UPDATE (Modify Check-out Date)
    public void updateCheckoutDate(int reservationId, String newCheckOut) {
        String sql = "UPDATE Reservations SET check_out = ? WHERE reservation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newCheckOut);
            stmt.setInt(2, reservationId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(rows + " reservation(s) updated successfully.");
            } else {
                System.out.println("No reservation found to update.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE (Cancel Reservation)
    public void cancelReservation(int reservationId) {
        String sql = "DELETE FROM Reservations WHERE reservation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(rows + " reservation(s) cancelled successfully.");
            } else {
                System.out.println("No reservation found to cancel.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRoomAvailable(int roomId, String checkIn, String checkOut) {
        String sql = "SELECT COUNT(*) FROM Reservations WHERE room_id = ? " + 
                     "AND (check_in < ? AND check_out > ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setString(2, checkOut); // Requested checkOut > existing check_in
            stmt.setString(3, checkIn);  // Requested checkIn < existing check_out

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // True if no conflict
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int findAvailableRoom(String roomType, String checkIn, String checkOut) {
        String sql = "SELECT room_id FROM Rooms " +
                     "WHERE room_type = ? AND status = 'Available' AND room_id NOT IN (" +
                     "  SELECT room_id FROM Reservations " +
                     "  WHERE (check_in < ? AND check_out > ?)) " +
                     "LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomType);
            stmt.setString(2, checkOut);
            stmt.setString(3, checkIn);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("room_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Not found
    }
    
    

}


