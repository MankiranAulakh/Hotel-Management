package hotelManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hotelManagement.DatabaseConnection;

public class RoomDAO {

    // CREATE (Insert Room)
    public void addRoom(String roomNumber, String roomType, double pricePerNight) {
        String sql = "INSERT INTO Rooms (room_number, room_type, price_per_night) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setDouble(3, pricePerNight);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " room(s) added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (Retrieve Room Details)
    public void getRoomByNumber(String roomNumber) {
        String sql = "SELECT * FROM Rooms WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Room ID: " + rs.getInt("room_id"));
                System.out.println("Room Number: " + rs.getString("room_number"));
                System.out.println("Room Type: " + rs.getString("room_type"));
                System.out.println("Price per Night: " + rs.getDouble("price_per_night"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("---------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE (Modify Room Price)
    public void updateRoomPrice(String roomNumber, double newPrice) {
        String sql = "UPDATE Rooms SET price_per_night = ? WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setString(2, roomNumber);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " room(s) updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE (Remove Room)
    public void deleteRoom(String roomNumber) {
        String sql = "DELETE FROM Rooms WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " room(s) deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check Room Availability (for a given date range)
    public boolean isRoomAvailable(int roomId, String checkIn, String checkOut) {
        String sql = "SELECT COUNT(*) FROM Reservations WHERE room_id = ? " + 
                     "AND (check_in < ? AND check_out > ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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

    // Automated Room Assignment based on Guest Preferences
    public int assignRoomBasedOnPreferences(String roomType, String checkIn, String checkOut) {
        String sql = "SELECT room_id FROM Rooms " +
                     "WHERE room_type = ? AND status = 'Available' AND room_id NOT IN (" +
                     "  SELECT room_id FROM Reservations " +
                     "  WHERE (check_in < ? AND check_out > ?)) " +
                     "LIMIT 1";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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

    // Flag Room for Maintenance
    public void flagRoomForMaintenance(int roomId) {
        String sql = "UPDATE Rooms SET status = 'Maintenance' WHERE room_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);

            int rows = stmt.executeUpdate();
            System.out.println("Room " + roomId + " flagged for maintenance.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Housekeeping Task Scheduling (Mark Room Status for Cleaning)
    public void scheduleHousekeeping(int roomId, String taskStatus) {
        String sql = "UPDATE Rooms SET status = ? WHERE room_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, taskStatus); // Clean, Dirty, In-progress
            stmt.setInt(2, roomId);

            int rows = stmt.executeUpdate();
            System.out.println("Room " + roomId + " housekeeping status updated to: " + taskStatus);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
