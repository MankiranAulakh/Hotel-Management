package hotelManagement.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import hotelManagement.DatabaseConnection;
import hotelManagement.dao.ReservationDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReservationScreen extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField guestIdField, roomIdField, checkInField, checkOutField, amountField;
    private JComboBox<String> roomTypeBox, paymentStatusBox;
    private JButton addButton, updateButton, deleteButton, findRoomButton;

    public ReservationScreen() {
        setTitle("Reservations Management");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2));

        inputPanel.add(new JLabel("Guest ID:"));
        guestIdField = new JTextField();
        inputPanel.add(guestIdField);

        inputPanel.add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(new String[]{"Single", "Double", "Suite", "Deluxe"});
        inputPanel.add(roomTypeBox);

        findRoomButton = new JButton("Find Room");
        inputPanel.add(findRoomButton);
        inputPanel.add(new JLabel("")); // Empty placeholder

        inputPanel.add(new JLabel("Room ID:"));
        roomIdField = new JTextField();
        inputPanel.add(roomIdField);

        inputPanel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        checkInField = new JTextField();
        inputPanel.add(checkInField);

        inputPanel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        checkOutField = new JTextField();
        inputPanel.add(checkOutField);

        inputPanel.add(new JLabel("Total Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);
        
        inputPanel.add(new JLabel("Payment Status:"));
        paymentStatusBox = new JComboBox<>(new String[]{"Pending", "Paid", "Cancelled"});
        inputPanel.add(paymentStatusBox);

        addButton = new JButton("Add Reservation");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        loadReservations();
        addEventListeners();
    }

    private void loadReservations() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Reservations")) {

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"ID", "Guest ID", "Room ID", "Check-in", "Check-out", "Amount", "Payment Status"});

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
            e.printStackTrace();
        }
    }

    private void addEventListeners() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int guestId = Integer.parseInt(guestIdField.getText());
                    int roomId = Integer.parseInt(roomIdField.getText());
                    String checkIn = checkInField.getText();
                    String checkOut = checkOutField.getText();
                    String paymentStatus = (String) paymentStatusBox.getSelectedItem();
                    double totalAmount = Double.parseDouble(amountField.getText());

                    boolean isAvailable = ReservationDAO.isRoomAvailable(roomId, checkIn, checkOut);

                    if (isAvailable) {
                        ReservationDAO dao = new ReservationDAO();
                        dao.addReservation(guestId, roomId, checkIn, checkOut, totalAmount, paymentStatus);
                        loadReservations();
                    } else {
                        JOptionPane.showMessageDialog(null, "Room is not available for the selected dates.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
                        int guestId = Integer.parseInt(guestIdField.getText());
                        int roomId = Integer.parseInt(roomIdField.getText());
                        String checkIn = checkInField.getText();
                        String checkOut = checkOutField.getText();
                        String paymentStatus = (String) paymentStatusBox.getSelectedItem();
                        double totalAmount = Double.parseDouble(amountField.getText());

                        // Now include guestId in the update logic if required
                        ReservationDAO dao = new ReservationDAO();
                        dao.updateReservation(reservationId, guestId, roomId, checkIn, checkOut, totalAmount, paymentStatus); // Assuming you want to update all fields

                        // Reload the reservations table after the update
                        loadReservations();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a reservation to update.");
                }
            }
        });



        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    ReservationDAO dao = new ReservationDAO();
                    dao.cancelReservation(id);

                    // After cancellation, reload reservations and show success message
                    loadReservations();
                    JOptionPane.showMessageDialog(null, "Reservation successfully cancelled.");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a reservation to cancel.");
                }
            }
        });


        findRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String roomType = (String) roomTypeBox.getSelectedItem();
                String checkIn = checkInField.getText();
                String checkOut = checkOutField.getText();

                int roomId = ReservationDAO.findAvailableRoom(roomType, checkIn, checkOut);

                if (roomId != -1) {
                    roomIdField.setText(String.valueOf(roomId));
                    JOptionPane.showMessageDialog(null, "Found Room ID: " + roomId);
                } else {
                    JOptionPane.showMessageDialog(null, "No available room found for selected type and dates.");
                }
            }
        });
    }
}
