package hotelManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReservationScreen extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField guestIdField, roomIdField, checkInField, checkOutField, amountField;
    private JButton addButton, updateButton, deleteButton;

    public ReservationScreen() {
        setTitle("Reservations Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Guest ID:"));
        guestIdField = new JTextField();
        inputPanel.add(guestIdField);

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
        try (Connection con = DriverManager.getConnection("jdbc:mysql://44.202.231.250:3306/testdb", "newuser", "password");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Reservations")) {

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"ID", "Guest ID", "Room ID", "Check-in", "Check-out", "Amount"});
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("reservation_id"), rs.getInt("guest_id"), rs.getInt("room_id"), rs.getDate("check_in"), rs.getDate("check_out"), rs.getDouble("total_amount")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addEventListeners() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection con = DriverManager.getConnection("jdbc:mysql://44.202.231.250:3306/testdb", "newuser", "password")) {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO Reservations (guest_id, room_id, check_in, check_out, total_amount) VALUES (?, ?, ?, ?, ?)");
                    ps.setInt(1, Integer.parseInt(guestIdField.getText()));
                    ps.setInt(2, Integer.parseInt(roomIdField.getText()));
                    ps.setDate(3, Date.valueOf(checkInField.getText()));
                    ps.setDate(4, Date.valueOf(checkOutField.getText()));
                    ps.setDouble(5, Double.parseDouble(amountField.getText()));
                    ps.executeUpdate();
                    loadReservations();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://44.202.231.250:3306/testdb", "newuser", "password")) {
                        PreparedStatement ps = con.prepareStatement("DELETE FROM Reservations WHERE reservation_id = ?");
                        ps.setInt(1, id);
                        ps.executeUpdate();
                        loadReservations();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservationScreen().setVisible(true));
    }
}
