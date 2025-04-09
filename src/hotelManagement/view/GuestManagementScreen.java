package hotelManagement.view;

import javax.swing.*;
import hotelManagement.dao.GuestDAO;
import hotelManagement.model.Guest;
import hotelManagement.Main;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GuestManagementScreen extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField, idProofField;
    private JButton addButton, updateButton, deleteButton, viewButton, dashboardButton;
    private JTable guestTable;
    private GuestDAO guestDAO;

    public GuestManagementScreen() {
        guestDAO = new GuestDAO();
        setTitle("Guest Management");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(162, 215, 250));

        // Dashboard Button
        dashboardButton = createCardButton("⬅ Dashboard", new Color(41, 128, 185));
        dashboardButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashboardButton.addActionListener(e -> {
            new Main().setVisible(true); // Open main dashboard
            dispose(); // Close current screen
        });

        // Top Panel for Dashboard Button + Title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(162, 215, 250));
        topPanel.add(dashboardButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("👤 Guest Management", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(33, 37, 41));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH); // Added to mainPanel

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 12, 12));
        formPanel.setBackground(new Color(245, 248, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder("Guest Details"));

        formPanel.add(createStyledLabel("First Name:", labelFont));
        firstNameField = new JTextField(15);
        formPanel.add(firstNameField);

        formPanel.add(createStyledLabel("Last Name:", labelFont));
        lastNameField = new JTextField(15);
        formPanel.add(lastNameField);

        formPanel.add(createStyledLabel("Email:", labelFont));
        emailField = new JTextField(15);
        formPanel.add(emailField);

        formPanel.add(createStyledLabel("Phone:", labelFont));
        phoneField = new JTextField(15);
        formPanel.add(phoneField);

        formPanel.add(createStyledLabel("Address:", labelFont));
        addressField = new JTextField(15);
        formPanel.add(addressField);

        formPanel.add(createStyledLabel("ID Proof:", labelFont));
        idProofField = new JTextField(15);
        formPanel.add(idProofField);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 248, 250));

        addButton = createCardButton("Add Guest", new Color(46, 204, 113));
        updateButton = createCardButton("Update Guest", new Color(52, 152, 219));
        deleteButton = createCardButton("Delete Guest", new Color(231, 76, 60));
        viewButton = createCardButton("View Guests", new Color(155, 89, 182));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Table
        String[] columnNames = {"Guest ID", "First Name", "Last Name", "Email", "Phone", "Address", "ID Proof"};
        guestTable = new JTable(new Object[0][7], columnNames);
        JScrollPane tableScroll = new JScrollPane(guestTable);
        tableScroll.setPreferredSize(new Dimension(700, 200));

        // Center panel for form and buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 248, 250));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(tableScroll, BorderLayout.SOUTH);
        add(mainPanel);

        // Button actions
        addButton.addActionListener(e -> addGuest());
        updateButton.addActionListener(e -> updateGuest());
        deleteButton.addActionListener(e -> deleteGuest());
        viewButton.addActionListener(e -> viewGuests());
    }

    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JButton createCardButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverColor = backgroundColor.brighter();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void addGuest() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String idProof = idProofField.getText();

        guestDAO.addGuest(firstName, lastName, email, phone, address, idProof);
        JOptionPane.showMessageDialog(this, "Guest added successfully.");
        clearFields();
    }

    private void updateGuest() {
        String email = JOptionPane.showInputDialog(this, "Enter email of the guest to update:");
        String newPhone = JOptionPane.showInputDialog(this, "Enter new phone number:");

        if (email != null && newPhone != null && !email.trim().isEmpty() && !newPhone.trim().isEmpty()) {
            boolean updated = guestDAO.updateGuestPhone(email, newPhone);
            JOptionPane.showMessageDialog(this, updated ? "Guest updated successfully." : "No guest found with the given email.");
        }
    }

    private void deleteGuest() {
        String email = JOptionPane.showInputDialog(this, "Enter email of the guest to delete:");

        if (email != null && !email.trim().isEmpty()) {
            boolean deleted = guestDAO.deleteGuest(email);
            JOptionPane.showMessageDialog(this, deleted ? "Guest deleted successfully." : "No guest found with the given email.");
        }
    }

    private void viewGuests() {
        List<Guest> guestList = guestDAO.getGuestList();
        String[][] data = new String[guestList.size()][7];
        for (int i = 0; i < guestList.size(); i++) {
            Guest guest = guestList.get(i);
            data[i][0] = String.valueOf(guest.getGuestId());
            data[i][1] = guest.getFirstName();
            data[i][2] = guest.getLastName();
            data[i][3] = guest.getEmail();
            data[i][4] = guest.getPhone();
            data[i][5] = guest.getAddress();
            data[i][6] = guest.getIdProof();
        }
        guestTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{
            "Guest ID", "First Name", "Last Name", "Email", "Phone", "Address", "ID Proof"
        }));
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        idProofField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GuestManagementScreen().setVisible(true));
    }
}
