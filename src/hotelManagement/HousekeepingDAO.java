package hotelManagement;

import java.sql.*;
import java.util.*;

public class HousekeepingDAO {

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

    public String getHousekeepingById(int housekeepingId) {
        String sql = "SELECT * FROM Housekeeping WHERE housekeeping_id = ?";
        StringBuilder result = new StringBuilder();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, housekeepingId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                result.append("Housekeeping ID: ").append(resultSet.getInt("housekeeping_id")).append("\n");
                result.append("Room ID: ").append(resultSet.getInt("room_id")).append("\n");
                result.append("Assigned Staff: ").append(resultSet.getString("assigned_staff")).append("\n");
                result.append("Cleaning Status: ").append(resultSet.getString("cleaning_status")).append("\n");
                result.append("Last Cleaned: ").append(resultSet.getString("last_cleaned")).append("\n");
            } else {
                result.append("No housekeeping record found with ID ").append(housekeepingId);
            }
        } catch (SQLException e) {
            result.append("Error: ").append(e.getMessage());
        }
        return result.toString();
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

    public List<HousekeepingList> getHousekeepingList() {
        List<HousekeepingList> housekeepingList = new ArrayList<>();
        String sql = "SELECT housekeeping_id, room_id, assigned_staff, cleaning_status, last_cleaned FROM Housekeeping";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HousekeepingList record = new HousekeepingList(
                    rs.getInt("housekeeping_id"),
                    rs.getInt("room_id"),
                    rs.getString("assigned_staff"),
                    rs.getString("cleaning_status"),
                    rs.getString("last_cleaned")
                );
                housekeepingList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return housekeepingList;
    }

}
