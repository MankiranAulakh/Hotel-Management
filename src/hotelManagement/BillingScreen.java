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
    private JTextField reservationIdField, guestIdField, totalAmountField, seasonalDiscountField, billingIdField;
    private JComboBox<String> paymentMethodBox, transactionStatusBox; private JTextArea outputArea;
    private BillingDAO billingDAO;

    public BillingScreen() {
    	billingDAO = new BillingDAO();

        setTitle("Billing & Payment");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        reservationIdField = new JTextField();
        guestIdField = new JTextField();
        totalAmountField = new JTextField();
        seasonalDiscountField = new JTextField();
        billingIdField = new JTextField();

        paymentMethodBox = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "Cash", "Online Payment"});
        transactionStatusBox = new JComboBox<>(new String[]{"Success", "Failed", "Pending"});

        inputPanel.add(new JLabel("Reservation ID:"));
        inputPanel.add(reservationIdField);

        inputPanel.add(new JLabel("Guest ID:"));
        inputPanel.add(guestIdField);

        inputPanel.add(new JLabel("Total Amount:"));
        inputPanel.add(totalAmountField);

        inputPanel.add(new JLabel("Seasonal Discount:"));
        inputPanel.add(seasonalDiscountField);

        inputPanel.add(new JLabel("Payment Method:"));
        inputPanel.add(paymentMethodBox);

        inputPanel.add(new JLabel("Transaction Status:"));
        inputPanel.add(transactionStatusBox);

        inputPanel.add(new JLabel("Billing ID (for Update/Delete):"));
        inputPanel.add(billingIdField);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton viewBtn = new JButton("View by Reservation");
        JButton updateBtn = new JButton("Update Status");
        JButton deleteBtn = new JButton("Delete");

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        // Output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Button Actions
        addBtn.addActionListener((ActionEvent e) -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                int guestId = Integer.parseInt(guestIdField.getText());
                double total = Double.parseDouble(totalAmountField.getText());
                double discount = Double.parseDouble(seasonalDiscountField.getText());
                String method = (String) paymentMethodBox.getSelectedItem();
                String status = (String) transactionStatusBox.getSelectedItem();

                billingDAO.addBilling(reservationId, guestId, total, discount, method, status);
                outputArea.setText("Billing record added.");
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        viewBtn.addActionListener((ActionEvent e) -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                outputArea.setText("");
                billingDAO.getBillingByReservationId(reservationId);
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        updateBtn.addActionListener((ActionEvent e) -> {
            try {
                int billingId = Integer.parseInt(billingIdField.getText());
                String newStatus = (String) transactionStatusBox.getSelectedItem();
                billingDAO.updateBillingStatus(billingId, newStatus);
                outputArea.setText("Billing status updated.");
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener((ActionEvent e) -> {
            try {
                int billingId = Integer.parseInt(billingIdField.getText());
                billingDAO.deleteBilling(billingId);
                outputArea.setText("Billing record deleted.");
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });
        
        viewBtn.addActionListener((ActionEvent e) -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                String details = billingDAO.getBillingDetailsAsString(reservationId);
                outputArea.setText(details);
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });


        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingScreen::new);
    }
}
