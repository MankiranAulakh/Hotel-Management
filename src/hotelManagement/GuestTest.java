package hotelManagement;

public class GuestTest {
    public static void main(String[] args) {
        GuestDAO guestDAO = new GuestDAO();

        // Create (Insert Guest)
        guestDAO.addGuest("John", "Doe", "john.doe@example.com", "1234567890", "123 Street, NY", "Passport");

        // Read (Retrieve Guest)
        guestDAO.getGuestByEmail("john.doe@example.com");

        // Update (Modify Phone Number)
        guestDAO.updateGuestPhone("john.doe@example.com", "0987654321");

        // Delete (Remove Guest)
        guestDAO.deleteGuest("john.doe@example.com");
    }
}

