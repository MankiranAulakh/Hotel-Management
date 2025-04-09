package hotelManagement.test;

import hotelManagement.dao.ReservationDAO;

public class ReservationTest {
    public static void main(String[] args) {
        ReservationDAO reservationDAO = new ReservationDAO();

        // Replace these with valid IDs from the database
        int validGuestId = 1;
        int validRoomId = 1;

        // Create (Insert a Reservation)
        reservationDAO.addReservation(validGuestId, validRoomId, "2025-03-10", "2025-03-15", 500.00, "Pending");

        // Read (Retrieve Reservation by ID)
        reservationDAO.getReservationById(1);

        // Update (Modify Check-out Date)
        reservationDAO.updateCheckoutDate(1, "2025-03-16");

        // Delete (Cancel Reservation)
        reservationDAO.cancelReservation(1);
    }
}


