package hotelManagement.view;

import javax.swing.*;
import hotelManagement.dao.BillingDAO;

import java.awt.*;
import java.awt.event.*;

public class BillingScreen extends JFrame {
    private JTextField reservationIdField, guestIdField, totalAmountField, seasonalDiscountField, billingIdField;
    private JComboBox<String> paymentMethodBox, transactionStatusBox;
    private JTextArea outputArea;
    private BillingDAO billingDAO;

    public BillingScreen() {
        billingDAO = new BillingDAO();
        setTitle("💳 Billing & Payment");
        setSize(750, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.decode("#f4f6f8"));

        // Title 
        JLabel titleLabel = new JLabel("Billing & Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Input Panel 
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 12, 12));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Billing Information"));
        inputPanel.setBackground(Color.white);

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

        inputPanel.add(new JLabel("Billing ID (Update/Delete):"));
        inputPanel.add(billingIdField);

        add(inputPanel, BorderLayout.WEST);

        // Button Panel 
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.setBackground(Color.white);
        JButton addBtn = createStyledButton("Add Billing");
        JButton viewBtn = createStyledButton("View by Reservation");
        JButton updateBtn = createStyledButton("Update Status");
        JButton deleteBtn = createStyledButton("Delete");
        JButton calcTotalBtn = createStyledButton("Calculate Total");

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(calcTotalBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // Output Panel 
        outputArea = new JTextArea(10, 40);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        add(scrollPane, BorderLayout.SOUTH);

        // Button Actions 
        addBtn.addActionListener(e -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                int guestId = Integer.parseInt(guestIdField.getText());
                double total = Double.parseDouble(totalAmountField.getText());
                double discount = Double.parseDouble(seasonalDiscountField.getText());
                String method = (String) paymentMethodBox.getSelectedItem();
                String status = (String) transactionStatusBox.getSelectedItem();
                billingDAO.addBilling(reservationId, guestId, total, discount, method, status);
                outputArea.setText("✅ Billing record added successfully.");
            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
            }
        });

        viewBtn.addActionListener(e -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                String details = billingDAO.getBillingDetailsAsString(reservationId);
                outputArea.setText(details);
            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
            }
        });

        updateBtn.addActionListener(e -> {
            try {
                int billingId = Integer.parseInt(billingIdField.getText());
                String newStatus = (String) transactionStatusBox.getSelectedItem();
                billingDAO.updateBillingStatus(billingId, newStatus);
                outputArea.setText("🔄 Billing status updated.");
            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                int billingId = Integer.parseInt(billingIdField.getText());
                billingDAO.deleteBilling(billingId);
                outputArea.setText("🗑️ Billing record deleted.");
            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
            }
        });

        calcTotalBtn.addActionListener(e -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                double reservationTotal = billingDAO.getReservationTotal(reservationId);
                totalAmountField.setText(String.valueOf(reservationTotal));
                double discount = 0.0;
                if (!seasonalDiscountField.getText().isEmpty()) {
                    discount = Double.parseDouble(seasonalDiscountField.getText());
                }
                double finalAmount = reservationTotal - discount;
                outputArea.setText("💰 Total: " + reservationTotal + "\n💸 Discount: " + discount + "\n🧾 Final Amount: " + finalAmount);
            } catch (Exception ex) {
                outputArea.setText("❌ Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingScreen::new);
    }
}
