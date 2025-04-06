package hotelManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.sql.*;

public class BillingScreen extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtReservationId, txtGuestId, txtAmount, txtDiscount, txtMethod, txtStatus, txtBillingId;

    private BillingDAO billingDAO;

    public BillingScreen() {
        billingDAO = new BillingDAO();
        setTitle("Billing & Payment Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table Setup
        model = new DefaultTableModel(new String[]{"Billing ID", "Reservation ID", "Guest ID", "Amount", "Discount", "Method", "Status"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form Inputs
        JPanel form = new JPanel(new GridLayout(3, 5, 10, 10));
        txtBillingId = new JTextField();
        txtReservationId = new JTextField();
        txtGuestId = new JTextField();
        txtAmount = new JTextField();
        txtDiscount = new JTextField();
        txtMethod = new JTextField();
        txtStatus = new JTextField();

        form.add(new JLabel("Billing ID (for update/delete):"));
        form.add(txtBillingId);
        form.add(new JLabel("Reservation ID:"));
        form.add(txtReservationId);
        form.add(new JLabel("Guest ID:"));
        form.add(txtGuestId);
        form.add(new JLabel("Total Amount:"));
        form.add(txtAmount);
        form.add(new JLabel("Seasonal Discount:"));
        form.add(txtDiscount);
        form.add(new JLabel("Payment Method:"));
        form.add(txtMethod);
        form.add(new JLabel("Transaction Status:"));
        form.add(txtStatus);

        add(form, BorderLayout.NORTH);

        // Buttons
        JPanel buttons = new JPanel();
        JButton btnAdd = new JButton("Add Billing");
        JButton btnUpdate = new JButton("Update Status");
        JButton btnDelete = new JButton("Delete Billing");
        JButton btnRefresh = new JButton("Refresh");

        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnRefresh);
        add(buttons, BorderLayout.SOUTH);

        // Button Listeners
        btnAdd.addActionListener(e -> addBilling());
        btnUpdate.addActionListener(e -> updateStatus());
        btnDelete.addActionListener(e -> deleteBilling());
        btnRefresh.addActionListener(e -> loadBilling());

        loadBilling();
        setVisible(true);
    }

    private void addBilling() {
        try {
            int reservationId = Integer.parseInt(txtReservationId.getText());
            int guestId = Integer.parseInt(txtGuestId.getText());
            double amount = Double.parseDouble(txtAmount.getText());
            double discount = Double.parseDouble(txtDiscount.getText());
            String method = txtMethod.getText();
            String status = txtStatus.getText();
            billingDAO.addBilling(reservationId, guestId, amount, discount, method, status);
            JOptionPane.showMessageDialog(this, "Billing added successfully.");
            loadBilling();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding billing: " + e.getMessage());
        }
    }

    private void updateStatus() {
        try {
            int billingId = Integer.parseInt(txtBillingId.getText());
            String status = txtStatus.getText();
            billingDAO.updateBillingStatus(billingId, status);
            JOptionPane.showMessageDialog(this, "Transaction status updated.");
            loadBilling();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage());
        }
    }

    private void deleteBilling() {
        try {
            int billingId = Integer.parseInt(txtBillingId.getText());
            billingDAO.deleteBilling(billingId);
            JOptionPane.showMessageDialog(this, "Billing deleted.");
            loadBilling();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting billing: " + e.getMessage());
        }
    }

    private void loadBilling() {
        model.setRowCount(0);
        String sql = "SELECT * FROM Billing";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("billing_id"));
                row.add(rs.getInt("reservation_id"));
                row.add(rs.getInt("guest_id"));
                row.add(rs.getDouble("total_amount"));
                row.add(rs.getDouble("seasonal_discount"));
                row.add(rs.getString("payment_method"));
                row.add(rs.getString("transaction_status"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading billing data: " + e.getMessage());
        }
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillingScreen());
    }
}
