package hotelManagement.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import hotelManagement.dao.MaintenanceDAO;
import hotelManagement.model.Maintenance;

public class MaintenanceScreen extends JFrame {

    private JTextField roomIdField, issueField, scheduledDateField, statusField, maintenanceIdField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable maintenanceTable;
    private MaintenanceDAO maintenanceDAO;

    public MaintenanceScreen() {
        maintenanceDAO = new MaintenanceDAO();
        setTitle("Maintenance Management");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(245, 248, 250);
        Color formColor = new Color(247, 220, 218);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // Top Panel - Title
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(162, 215, 250));
        JLabel titleLabel = new JLabel("Maintenance Management");
        titleLabel.setFont(titleFont);
        topPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formColor);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel[] labels = {
            new JLabel("Room ID:"), new JLabel("Issue Description:"),
            new JLabel("Scheduled Date (YYYY-MM-DD):"), new JLabel("Status (Pending/In Progress/Completed):"),
            new JLabel("Maintenance ID:")
        };

        JTextField[] fields = {
            roomIdField = new JTextField(15),
            issueField = new JTextField(15),
            scheduledDateField = new JTextField(15),
            statusField = new JTextField(15),
            maintenanceIdField = new JTextField(15)
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(labelFont);
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(bgColor);

        addButton = createStyledButton("Add");
        updateButton = createStyledButton("Update");
        deleteButton = createStyledButton("Delete");
        viewButton = createStyledButton("View");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Table Panel
        maintenanceTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(maintenanceTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Maintenance Records"));

        // Add sections to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);

        // Action Listeners
        addButton.addActionListener(e -> addMaintenance());
        updateButton.addActionListener(e -> updateMaintenance());
        deleteButton.addActionListener(e -> deleteMaintenance());
        viewButton.addActionListener(e -> viewMaintenance());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 180, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(140, 35));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 180, 255));
            }
        });

        return button;
    }

    private void addMaintenance() {
        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            String issue = issueField.getText();
            String scheduledDate = scheduledDateField.getText();
            String status = statusField.getText();

            maintenanceDAO.addMaintenance(roomId, issue, scheduledDate, status);
            JOptionPane.showMessageDialog(this, "Maintenance request logged successfully.");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateMaintenance() {
        try {
            if (maintenanceIdField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Maintenance ID.");
                return;
            }

            int maintenanceId = Integer.parseInt(maintenanceIdField.getText());
            String status = statusField.getText();

            if (status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid status.");
                return;
            }

            maintenanceDAO.updateMaintenanceStatus(maintenanceId, status);
            JOptionPane.showMessageDialog(this, "Maintenance status updated.");
            viewMaintenance();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteMaintenance() {
        try {
            int maintenanceId = Integer.parseInt(maintenanceIdField.getText());
            maintenanceDAO.deleteMaintenance(maintenanceId);
            JOptionPane.showMessageDialog(this, "Maintenance record deleted.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void viewMaintenance() {
        try {
            List<Maintenance> maintenanceRecords = maintenanceDAO.getMaintenanceList();
            String[][] data = new String[maintenanceRecords.size()][5];

            for (int i = 0; i < maintenanceRecords.size(); i++) {
                Maintenance m = maintenanceRecords.get(i);
                data[i][0] = String.valueOf(m.getMaintenanceId());
                data[i][1] = String.valueOf(m.getRoomId());
                data[i][2] = m.getIssueDescription();
                data[i][3] = m.getScheduledDate();
                data[i][4] = m.getStatus();
            }

            maintenanceTable.setModel(new DefaultTableModel(
                data,
                new String[]{"Maintenance ID", "Room ID", "Issue", "Scheduled Date", "Status"}
            ));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        roomIdField.setText("");
        issueField.setText("");
        scheduledDateField.setText("");
        statusField.setText("");
        maintenanceIdField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MaintenanceScreen().setVisible(true));
    }
}
