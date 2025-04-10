package hotelManagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import hotelManagement.DatabaseConnection;
import hotelManagement.dao.ReservationDAO;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReservationScreen extends JFrame {
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField guestIdField, roomIdField, checkInField, checkOutField, amountField;
    private JComboBox<String> roomTypeBox, paymentStatusBox;
    private JButton addButton, updateButton, deleteButton, findRoomButton;

    public ReservationScreen() {
        setTitle("Reservations Management");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initializeTable();
        add(createInputPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadReservations();
        addEventListeners();
    }

    // ========================= Initialize Table =========================
    private void initializeTable() {
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(22);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

 // ========================= Input Panel =========================
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10)); // 8 rows, 2 columns
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(181, 247, 209)); // Soft background

        guestIdField = new JTextField();
        roomTypeBox = new JComboBox<>(new String[]{"Single", "Double", "Suite", "Deluxe"});
        findRoomButton = new JButton("Find Room");
        roomIdField = new JTextField();
        checkInField = new JTextField();
        checkOutField = new JTextField();
        amountField = new JTextField();
        paymentStatusBox = new JComboBox<>(new String[]{"Pending", "Paid", "Cancelled"});

        panel.add(new JLabel("Guest ID:"));
        panel.add(guestIdField);

        panel.add(new JLabel("Room Type:"));
        panel.add(roomTypeBox);

        panel.add(new JLabel("")); // Empty cell for alignment
        panel.add(findRoomButton); // Place Find Room button in the second column

        panel.add(new JLabel("Room ID:"));
        panel.add(roomIdField);

        panel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        panel.add(checkInField);

        panel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        panel.add(checkOutField);

        panel.add(new JLabel("Total Amount:"));
        panel.add(amountField);

        panel.add(new JLabel("Payment Status:"));
        panel.add(paymentStatusBox);

        return panel;
    }


    // ========================= Button Panel =========================
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        addButton = new JButton("Add Reservation");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        return panel;
    }

    // ========================= Load Reservations from DB =========================
    private void loadReservations() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Reservations")) {

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{
                "ID", "Guest ID", "Room ID", "Check-in", "Check-out", "Amount", "Payment Status"
            });

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("reservation_id"),
                    rs.getInt("guest_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in"),
                    rs.getDate("check_out"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_status")
                });
            }
        } catch (SQLException e) {
            showError("Error loading reservations: " + e.getMessage());
        }
    }

    // ========================= Event Listeners =========================
    private void addEventListeners() {
        // Add Reservation
        addButton.addActionListener(e -> {
            try {
                int guestId = Integer.parseInt(guestIdField.getText());
                int roomId = Integer.parseInt(roomIdField.getText());
                String checkIn = checkInField.getText();
                String checkOut = checkOutField.getText();
                double totalAmount = Double.parseDouble(amountField.getText());
                String paymentStatus = (String) paymentStatusBox.getSelectedItem();

                boolean available = ReservationDAO.isRoomAvailable(roomId, checkIn, checkOut);
                if (available) {
                    new ReservationDAO().addReservation(guestId, roomId, checkIn, checkOut, totalAmount, paymentStatus);
                    loadReservations();
                    clearInputs();
                } else {
                    showError("Room is not available for selected dates.");
                }
            } catch (Exception ex) {
                showError("Error adding reservation: " + ex.getMessage());
            }
        });

        // Update Reservation
        updateButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                try {
                    int reservationId = (int) tableModel.getValueAt(row, 0);
                    int guestId = Integer.parseInt(guestIdField.getText());
                    int roomId = Integer.parseInt(roomIdField.getText());
                    String checkIn = checkInField.getText();
                    String checkOut = checkOutField.getText();
                    double totalAmount = Double.parseDouble(amountField.getText());
                    String paymentStatus = (String) paymentStatusBox.getSelectedItem();

                    new ReservationDAO().updateReservation(reservationId, guestId, roomId, checkIn, checkOut, totalAmount, paymentStatus);
                    loadReservations();
                    clearInputs();
                } catch (Exception ex) {
                    showError("Error updating reservation: " + ex.getMessage());
                }
            } else {
                showError("Please select a reservation to update.");
            }
        });

        // Delete Reservation
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                new ReservationDAO().cancelReservation(id);
                loadReservations();
                showMessage("Reservation successfully cancelled.");
            } else {
                showError("Please select a reservation to cancel.");
            }
        });

        // Find Available Room
        findRoomButton.addActionListener(e -> {
            String roomType = (String) roomTypeBox.getSelectedItem();
            String checkIn = checkInField.getText();
            String checkOut = checkOutField.getText();

            int roomId = ReservationDAO.findAvailableRoom(roomType, checkIn, checkOut);
            if (roomId != -1) {
                roomIdField.setText(String.valueOf(roomId));
                showMessage("Found Room ID: " + roomId);
            } else {
                showError("No available room found for selected type and dates.");
            }
        });

        // Table Click Listener to Populate Fields
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    guestIdField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                    roomIdField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                    checkInField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                    checkOutField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                    amountField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
                    paymentStatusBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 6)));
                }
            }
        });
    }

    // ========================= Helper Methods =========================
    private void clearInputs() {
        guestIdField.setText("");
        roomIdField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
        amountField.setText("");
        paymentStatusBox.setSelectedIndex(0);
        roomTypeBox.setSelectedIndex(0);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
