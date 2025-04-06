package hotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDAO {

    // CREATE (Insert Room)
    public void addRoom(String roomNumber, String roomType, double pricePerNight) {
        String sql = "INSERT INTO Rooms (room_number, room_type, price_per_night) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
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

        try (Connection conn = DatabaseConnection.getConnection();
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

        try (Connection conn = DatabaseConnection.getConnection();
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

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " room(s) deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
