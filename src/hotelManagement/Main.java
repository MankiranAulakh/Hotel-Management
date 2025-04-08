package hotelManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Hotel Management Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create buttons
        JButton guestButton = new JButton("Guest Management");
        JButton reservationButton = new JButton("Reservation System");
        JButton housekeepingButton = new JButton("Housekeeping");
        JButton billingButton = new JButton("Billing System");
        JButton inventoryButton = new JButton("Inventory Management");

        // Set layout
        setLayout(new GridLayout(5, 1, 10, 10));
        add(guestButton);
        add(reservationButton);
        add(housekeepingButton);
        add(billingButton);
        add(inventoryButton);

        // Add action listeners
        guestButton.addActionListener(e -> new GuestManagementScreen().setVisible(true));
        reservationButton.addActionListener(e -> new ReservationScreen().setVisible(true));
        housekeepingButton.addActionListener(e -> new HousekeepingScreen().setVisible(true));
        billingButton.addActionListener(e -> new BillingScreen().setVisible(true));
        inventoryButton.addActionListener(e -> new InventoryManagementScreen().setVisible(true));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}

