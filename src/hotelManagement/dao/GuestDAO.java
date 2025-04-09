package hotelManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hotelManagement.DatabaseConnection;
import hotelManagement.model.Guest;

public class GuestDAO {

    // CREATE (Insert Guest)
    public void addGuest(String firstName, String lastName, String email, String phone, String address, String idProof) {
        String sql = "INSERT INTO Guests (first_name, last_name, email, phone, address, id_proof) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, idProof);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " guest(s) added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (Retrieve Guest Details)
    public void getGuestByEmail(String email) {
        String sql = "SELECT * FROM Guests WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Guest ID: " + rs.getInt("guest_id"));
                System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("ID Proof: " + rs.getString("id_proof"));
                System.out.println("---------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // READ (Retrieve Guest by ID)
    public void getGuestById(int guestId) {
        String sql = "SELECT * FROM Guests WHERE guest_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Guest ID: " + rs.getInt("guest_id"));
                System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("ID Proof: " + rs.getString("id_proof"));
                System.out.println("---------------------------");
            } else {
                System.out.println("No guest found with ID: " + guestId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (Retrieve Guests by First or Last Name)
    public void getGuestsByName(String name) {
        String sql = "SELECT * FROM Guests WHERE first_name LIKE ? OR last_name LIKE ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String queryParam = "%" + name + "%";
            stmt.setString(1, queryParam);
            stmt.setString(2, queryParam);

            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Guest ID: " + rs.getInt("guest_id"));
                System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("ID Proof: " + rs.getString("id_proof"));
                System.out.println("---------------------------");
            }

            if (!found) {
                System.out.println("No guests found with name containing: " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // READ (Retrieve List of All Guests)
    public List<Guest> getGuestList() {
        List<Guest> guestList = new ArrayList<>();
        String sql = "SELECT * FROM Guests";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Guest guest = new Guest(
                    rs.getInt("guest_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("id_proof")
                );
                guestList.add(guest);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guestList;
    }



    // UPDATE (Modify Guest Information)
    public boolean updateGuestPhone(String email, String newPhone) {
        String sql = "UPDATE Guests SET phone = ? WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPhone);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE (Remove Guest)
    public boolean deleteGuest(String email) {
        String sql = "DELETE FROM Guests WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
