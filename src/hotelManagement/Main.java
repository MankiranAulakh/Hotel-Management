package hotelManagement;

import javax.swing.*;

import hotelManagement.view.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Hotel Management System");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 248, 250)); // light background

        // Title
        JLabel titleLabel = new JLabel("Hotel Management Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        buttonPanel.setBackground(new Color(245, 248, 250));

        // Add colorful card-style buttons
        buttonPanel.add(createCardButton("Guest Management", new Color(52, 152, 219), e -> new GuestManagementScreen().setVisible(true)));
        buttonPanel.add(createCardButton("Reservation System", new Color(46, 204, 113), e -> new ReservationScreen().setVisible(true)));
        buttonPanel.add(createCardButton("Room Management", new Color(155, 89, 182), e -> new RoomManagementScreen().setVisible(true)));
        buttonPanel.add(createCardButton("Housekeeping", new Color(241, 196, 15), e -> new HousekeepingScreen().setVisible(true)));
        buttonPanel.add(createCardButton("Maintenance", new Color(231, 76, 60), e -> new MaintenanceScreen().setVisible(true)));
        buttonPanel.add(createCardButton("Billing System", new Color(230, 126, 34), e -> new BillingScreen().setVisible(true)));
        buttonPanel.add(createCardButton("Inventory Management", new Color(26, 188, 156), e -> new InventoryManagementScreen().setVisible(true)));
        buttonPanel.add(createCardButton("Reporting", new Color(149, 165, 166), e -> new ReportingScreen().setVisible(true)));

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createCardButton(String text, Color backgroundColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Go to " + text);
        button.setPreferredSize(new Dimension(250, 60));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Hover effect
        Color hoverColor = backgroundColor.brighter();

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });

        button.addActionListener(action);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
