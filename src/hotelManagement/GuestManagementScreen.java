package hotelManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GuestManagementScreen extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable guestTable;
    private DefaultTableModel tableModel;

    public GuestManagementScreen() {
        // Frame setup
        setTitle("Guest Management");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);

        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        add(inputPanel, BorderLayout.NORTH);

        // Create buttons for CRUD operations
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton = new JButton("Add Guest");
        updateButton = new JButton("Update Guest");
        deleteButton = new JButton("Delete Guest");
        viewButton = new JButton("View Guests");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Create table to display guest data
        tableModel = new DefaultTableModel(new Object[]{"Guest ID", "First Name", "Last Name", "Email", "Phone", "Address"}, 0);
        guestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(guestTable);
        add(scrollPane, BorderLayout.SOUTH);

        // Event listeners
        addButton.addActionListener(e -> addGuest());
        updateButton.addActionListener(e -> updateGuest());
        deleteButton.addActionListener(e -> deleteGuest());
        viewButton.addActionListener(e -> loadGuests());

        setVisible(true);
    }

    private void addGuest() {
        // Get input from fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        // Add guest to database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://18.212.186.57:3306/testdb", "newuser", "password")) {
            String sql = "INSERT INTO Guests (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, phone);
                statement.setString(5, address);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Guest added successfully.");
                clearFields();
                loadGuests();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding guest.");
        }
    }

    private void updateGuest() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow != -1) {
            int guestId = (int) guestTable.getValueAt(selectedRow, 0);
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            // Update guest in database
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://18.212.186.57:3306/testdb", "newuser", "password")) {
                String sql = "UPDATE Guests SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE guest_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, firstName);
                    statement.setString(2, lastName);
                    statement.setString(3, email);
                    statement.setString(4, phone);
                    statement.setString(5, address);
                    statement.setInt(6, guestId);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Guest updated successfully.");
                    loadGuests();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating guest.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a guest to update.");
        }
    }

    private void deleteGuest() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow != -1) {
            int guestId = (int) guestTable.getValueAt(selectedRow, 0);

            // Delete guest from database
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://18.212.186.57:3306/testdb", "newuser", "password")) {
                String sql = "DELETE FROM Guests WHERE guest_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, guestId);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Guest deleted successfully.");
                    loadGuests();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting guest.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a guest to delete.");
        }
    }

    private void loadGuests() {
        tableModel.setRowCount(0);
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://18.212.186.57:3306/testdb", "newuser", "password")) {
            String sql = "SELECT * FROM Guests";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] row = {
                        resultSet.getInt("guest_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
        new GuestManagementScreen();
    }
}
