package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationDAO {

    // CREATE (Insert a Reservation)
    public void addReservation(int guestId, int roomId, String checkIn, String checkOut, double totalAmount) {
        String sql = "INSERT INTO Reservations (guest_id, room_id, check_in, check_out, total_amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
            stmt.setInt(2, roomId);
            stmt.setString(3, checkIn);
            stmt.setString(4, checkOut);
            stmt.setDouble(5, totalAmount);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " reservation(s) added successfully.");

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

    // UPDATE (Modify Check-out Date)
    public void updateCheckoutDate(int reservationId, String newCheckOut) {
        String sql = "UPDATE Reservations SET check_out = ? WHERE reservation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newCheckOut);
            stmt.setInt(2, reservationId);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " reservation(s) updated successfully.");

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
            System.out.println(rows + " reservation(s) cancelled successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

