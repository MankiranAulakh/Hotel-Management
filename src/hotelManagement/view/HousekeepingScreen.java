package hotelManagement.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import hotelManagement.dao.HousekeepingDAO;
import hotelManagement.model.HousekeepingList;

import java.awt.*;
import java.util.List;

public class HousekeepingScreen extends JFrame {
    private JTextField housekeepingIdField, roomIdField, staffField, lastCleanedField;
    private JComboBox<String> statusComboBox;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTextArea displayArea;
    private JTable housekeepingTable;
    private HousekeepingDAO housekeepingDAO;

    public HousekeepingScreen() {
        housekeepingDAO = new HousekeepingDAO();
        setTitle("\uD83C\uDFE8 Housekeeping Management");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ======= Main Panel with background color =======
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(247, 239, 208));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // ======= Form Panel =======
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 248, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Manage Housekeeping",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        housekeepingIdField = new JTextField();
        roomIdField = new JTextField();
        staffField = new JTextField();
        lastCleanedField = new JTextField();
        statusComboBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});

        JLabel[] labels = {
                new JLabel("Housekeeping ID:"),
                new JLabel("Room ID:"),
                new JLabel("Assigned Staff:"),
                new JLabel("Cleaning Status:"),
                new JLabel("Last Cleaned (yyyy-MM-dd HH:mm:ss):")
        };

        JComponent[] fields = {
                housekeepingIdField, roomIdField, staffField, statusComboBox, lastCleanedField
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(labels[i], gbc);

            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        // ======= Button Panel =======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(247, 239, 208));

        addButton = new JButton("Add Record");
        updateButton = new JButton("Update Status");
        deleteButton = new JButton("Delete Record");
        viewButton = new JButton("View All Records");

        JButton[] buttons = {addButton, updateButton, deleteButton, viewButton};
        for (JButton btn : buttons) {
            btn.setBackground(new Color(51, 153, 255));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            buttonPanel.add(btn);
        }

        // ======= Table Panel =======
        housekeepingTable = new JTable(new DefaultTableModel(new Object[][]{},
                new String[]{"Housekeeping ID", "Room ID", "Assigned Staff", "Cleaning Status", "Last Cleaned"}));
        JScrollPane tableScroll = new JScrollPane(housekeepingTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Housekeeping Records"));
        tableScroll.setPreferredSize(new Dimension(760, 200));

        // ======= Display Area =======
        displayArea = new JTextArea(3, 50);
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        displayArea.setForeground(new Color(34, 139, 34));
        JScrollPane displayScroll = new JScrollPane(displayArea);
        displayScroll.setBorder(BorderFactory.createTitledBorder("Status"));

        // ======= Top Panel Structure (Form + Buttons) =======
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(162, 215, 250));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ======= Add to Main Panel =======
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        mainPanel.add(displayScroll, BorderLayout.SOUTH);

        add(mainPanel);

        // ======= Action Listeners =======
        addButton.addActionListener(e -> addHousekeeping());
        updateButton.addActionListener(e -> updateHousekeepingStatus());
        deleteButton.addActionListener(e -> deleteHousekeepingRecord());
        viewButton.addActionListener(e -> viewHousekeeping());
    }

    private void addHousekeeping() {
        try {
            int roomId = Integer.parseInt(roomIdField.getText().trim());
            String staff = staffField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();
            String lastCleaned = lastCleanedField.getText().trim();

            housekeepingDAO.addHousekeeping(roomId, staff, status, lastCleaned);
            displayArea.setText("✅ Housekeeping record added successfully.");
            clearFields();
        } catch (NumberFormatException ex) {
            displayArea.setText("❌ Invalid Room ID. Please enter a valid number.");
        }
    }

    private void updateHousekeepingStatus() {
        try {
            int housekeepingId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Housekeeping ID to update:"));
            String newStatus = JOptionPane.showInputDialog(this, "Enter new status (Pending, In Progress, Completed):");
            String lastCleaned = JOptionPane.showInputDialog(this, "Enter last cleaned date (yyyy-MM-dd HH:mm:ss):");

            housekeepingDAO.updateHousekeepingStatus(housekeepingId, newStatus, lastCleaned);
            displayArea.setText("✅ Housekeeping record updated successfully.");
        } catch (NumberFormatException ex) {
            displayArea.setText("❌ Invalid input. Please enter correct values.");
        }
    }

    private void deleteHousekeepingRecord() {
        try {
            int housekeepingId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Housekeeping ID to delete:"));
            housekeepingDAO.deleteHousekeeping(housekeepingId);
            displayArea.setText("✅ Housekeeping record deleted successfully.");
        } catch (NumberFormatException ex) {
            displayArea.setText("❌ Invalid Housekeeping ID.");
        }
    }

    private void viewHousekeeping() {
        List<HousekeepingList> list = housekeepingDAO.getHousekeepingList();
        String[][] data = new String[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            HousekeepingList h = list.get(i);
            data[i][0] = String.valueOf(h.getHousekeepingId());
            data[i][1] = String.valueOf(h.getRoomId());
            data[i][2] = h.getStaff();
            data[i][3] = h.getStatus();
            data[i][4] = h.getLastCleaned();
        }
        housekeepingTable.setModel(new DefaultTableModel(data,
                new String[]{"Housekeeping ID", "Room ID", "Assigned Staff", "Cleaning Status", "Last Cleaned"}));
        displayArea.setText("✅ Loaded " + list.size() + " housekeeping records.");
    }

    private void clearFields() {
        housekeepingIdField.setText("");
        roomIdField.setText("");
        staffField.setText("");
        lastCleanedField.setText("");
        statusComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HousekeepingScreen().setVisible(true));
    }
}