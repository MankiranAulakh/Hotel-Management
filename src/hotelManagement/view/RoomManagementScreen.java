package hotelManagement.view;

import javax.swing.*;
import hotelManagement.dao.RoomDAO;
import java.awt.*;

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
        setSize(750, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 248, 250));

        // ===== Top Input Panel =====
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Room Information"));
        //inputPanel.setBackground(new Color(244, 222, 252));

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

        // ===== Center Button Panel =====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.setBackground(new Color(230, 240, 255));

        JButton addButton = createStyledButton("Add Room");
        JButton updatePriceButton = createStyledButton("Update Price");
        JButton getDetailsButton = createStyledButton("Get Room Details");
        JButton deleteButton = createStyledButton("Delete Room");
        JButton checkAvailabilityButton = createStyledButton("Check Room Availability");
        JButton flagMaintenanceButton = createStyledButton("Flag for Maintenance");

        buttonPanel.add(addButton);
        buttonPanel.add(updatePriceButton);
        buttonPanel.add(getDetailsButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkAvailabilityButton);
        buttonPanel.add(flagMaintenanceButton);

        add(buttonPanel, BorderLayout.CENTER);

        // ===== Bottom Output Area =====
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBorder(BorderFactory.createTitledBorder("System Output"));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(700, 150));
        add(scrollPane, BorderLayout.SOUTH);

        // ===== Action Listeners =====
        addButton.addActionListener(e -> addRoom());
        updatePriceButton.addActionListener(e -> updatePrice());
        getDetailsButton.addActionListener(e -> fetchRoomDetails());
        deleteButton.addActionListener(e -> deleteRoom());
        checkAvailabilityButton.addActionListener(e -> checkAvailability());
        flagMaintenanceButton.addActionListener(e -> flagForMaintenance());

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(244, 222, 252));
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return button;
    }

    // ========== Action Methods ==========

    private void addRoom() {
        String roomNumber = roomNumberField.getText();
        String roomType = roomTypeField.getText();
        String priceText = priceField.getText();

        if (roomNumber.isEmpty() || roomType.isEmpty() || priceText.isEmpty()) {
            showMessage("Please fill in all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            roomDAO.addRoom(roomNumber, roomType, price);
            outputArea.setText("✅ Room added successfully.\n");
        } catch (NumberFormatException ex) {
            showMessage("Invalid price format.");
        }
    }

    private void updatePrice() {
        String roomNumber = roomNumberField.getText();
        String priceText = priceField.getText();

        if (roomNumber.isEmpty() || priceText.isEmpty()) {
            showMessage("Enter room number and new price.");
            return;
        }

        try {
            double newPrice = Double.parseDouble(priceText);
            roomDAO.updateRoomPrice(roomNumber, newPrice);
            outputArea.setText("✅ Price updated successfully.\n");
        } catch (NumberFormatException ex) {
            showMessage("Invalid price format.");
        }
    }

    private void fetchRoomDetails() {
        String roomNumber = roomNumberField.getText();

        if (roomNumber.isEmpty()) {
            showMessage("Enter room number to fetch details.");
            return;
        }

        outputArea.setText("Fetching room details...\n");
        SwingUtilities.invokeLater(() -> {
            outputArea.setText(""); // Clear first
            roomDAO.getRoomByNumber(roomNumber);
        });
    }

    private void deleteRoom() {
        String roomNumber = roomNumberField.getText();

        if (roomNumber.isEmpty()) {
            showMessage("Enter room number to delete.");
            return;
        }

        roomDAO.deleteRoom(roomNumber);
        outputArea.setText("🗑️ Room deleted if it existed.\n");
    }

    private void checkAvailability() {
        String roomNumberText = roomNumberField.getText();
        String checkIn = JOptionPane.showInputDialog("Enter Check-in Date (YYYY-MM-DD):");
        String checkOut = JOptionPane.showInputDialog("Enter Check-out Date (YYYY-MM-DD):");

        if (roomNumberText.isEmpty() || checkIn == null || checkOut == null ||
                checkIn.isEmpty() || checkOut.isEmpty()) {
            showMessage("Please fill in all fields.");
            return;
        }

        try {
            int roomId = Integer.parseInt(roomNumberText);
            boolean isAvailable = roomDAO.isRoomAvailable(roomId, checkIn, checkOut);
            outputArea.setText(isAvailable
                    ? "✅ Room " + roomId + " is available for the given dates.\n"
                    : "❌ Room " + roomId + " is not available for the given dates.\n");
        } catch (NumberFormatException ex) {
            showMessage("Please enter a valid room number.");
        }
    }

    private void flagForMaintenance() {
        String roomNumber = roomNumberField.getText();

        if (roomNumber.isEmpty()) {
            showMessage("Please provide a room number.");
            return;
        }

        try {
            int roomNumberInt = Integer.parseInt(roomNumber);
            roomDAO.flagRoomForMaintenance(roomNumberInt);
            outputArea.setText("🛠️ Room " + roomNumber + " has been flagged for maintenance.\n");
        } catch (NumberFormatException ex) {
            showMessage("Please enter a valid room number.");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoomManagementScreen::new);
    }
}
