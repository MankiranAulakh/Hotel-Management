package hotelManagement.view;

import javax.swing.*;

import hotelManagement.dao.RoomDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomManagementScreen extends JFrame {
    private JTextField roomNumberField;
    private JTextField roomTypeField;
    private JTextField priceField;
    private JTextArea outputArea;
    private RoomDAO roomDAO;

    public RoomManagementScreen() {
        roomDAO = new RoomDAO();
        setTitle("Room Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        roomNumberField = new JTextField();
        roomTypeField = new JTextField();
        priceField = new JTextField();

        inputPanel.add(new JLabel("Room Number:"));
        inputPanel.add(roomNumberField);
        inputPanel.add(new JLabel("Room Type:"));
        inputPanel.add(roomTypeField);
        inputPanel.add(new JLabel("Price per Night:"));
        inputPanel.add(priceField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Room");
        JButton updatePriceButton = new JButton("Update Price");
        JButton getDetailsButton = new JButton("Get Room Details");
        JButton deleteButton = new JButton("Delete Room");
        JButton checkAvailabilityButton = new JButton("Check Room Availability");
        JButton flagMaintenanceButton = new JButton("Flag for Maintenance");

        buttonPanel.add(addButton);
        buttonPanel.add(updatePriceButton);
        buttonPanel.add(getDetailsButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkAvailabilityButton);
        buttonPanel.add(flagMaintenanceButton);

        add(buttonPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = roomNumberField.getText();
                String roomType = roomTypeField.getText();
                String priceText = priceField.getText();

                if (roomNumber.isEmpty() || roomType.isEmpty() || priceText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                try {
                    double price = Double.parseDouble(priceText);
                    roomDAO.addRoom(roomNumber, roomType, price);
                    outputArea.setText("Room added successfully.\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid price format.");
                }
            }
        });

        updatePriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = roomNumberField.getText();
                String priceText = priceField.getText();

                if (roomNumber.isEmpty() || priceText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter room number and new price.");
                    return;
                }

                try {
                    double newPrice = Double.parseDouble(priceText);
                    roomDAO.updateRoomPrice(roomNumber, newPrice);
                    outputArea.setText("Price updated successfully.\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid price format.");
                }
            }
        });

        getDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = roomNumberField.getText();

                if (roomNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter room number to fetch details.");
                    return;
                }

                outputArea.setText("Fetching room details...\n");

                // Use a background thread to avoid freezing the UI
                SwingUtilities.invokeLater(() -> {
                    outputArea.setText(""); // clear first
                    roomDAO.getRoomByNumber(roomNumber);
                });
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = roomNumberField.getText();

                if (roomNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter room number to delete.");
                    return;
                }

                roomDAO.deleteRoom(roomNumber);
                outputArea.setText("Room deleted if existed.\n");
            }
        });
        
        checkAvailabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumberText = roomNumberField.getText();
                String checkIn = JOptionPane.showInputDialog("Enter Check-in Date (YYYY-MM-DD):");
                String checkOut = JOptionPane.showInputDialog("Enter Check-out Date (YYYY-MM-DD):");

                if (roomNumberText.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                try {
                    int roomId = Integer.parseInt(roomNumberText);  // Convert room number to int

                    boolean isAvailable = roomDAO.isRoomAvailable(roomId, checkIn, checkOut);

                    if (isAvailable) {
                        outputArea.setText("Room " + roomId + " is available for the given dates.\n");
                    } else {
                        outputArea.setText("Room " + roomId + " is not available for the given dates.\n");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid room number.");
                }
            }
        });


        
     // Flag Room for Maintenance
        flagMaintenanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = roomNumberField.getText(); // Fetch the room number from the input field

                if (roomNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please provide a room number.");
                    return;
                }

                try {
                    int roomNumberInt = Integer.parseInt(roomNumber); // Convert the room number to an integer
                    roomDAO.flagRoomForMaintenance(roomNumberInt); // Call the DAO method with the integer room number

                    outputArea.setText("Room " + roomNumber + " has been flagged for maintenance.\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid room number.");
                }
            }
        });


        setVisible(true);
    }

    public static void main(String[] args) {
        new RoomManagementScreen();
    }
}
