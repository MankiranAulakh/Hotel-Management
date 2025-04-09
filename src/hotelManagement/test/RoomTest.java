package hotelManagement.test;

import hotelManagement.dao.RoomDAO;

public class RoomTest {
    public static void main(String[] args) {
        RoomDAO roomDAO = new RoomDAO();

        // Create (Insert Room)
        roomDAO.addRoom("101", "Single", 100.00);

        // Read (Retrieve Room by Number)
        roomDAO.getRoomByNumber("101");

        // Update (Modify Room Price)
        roomDAO.updateRoomPrice("101", 120.00);

        // Delete (Remove Room)
        roomDAO.deleteRoom("101");
    }
}

