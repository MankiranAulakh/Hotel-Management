package hotelManagement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaintenanceScreen extends JFrame {

    private JTextField roomIdField, issueField, scheduledDateField, statusField, maintenanceIdField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable maintenanceTable;
    private MaintenanceDAO maintenanceDAO;

    public MaintenanceScreen() {
        maintenanceDAO = new MaintenanceDAO();
        setTitle("Maintenance Management");
        setSize(600, 600); // Consistent size with Inventory screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for the form and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10)); // Use GridLayout for simplicity and neatness
        panel.setBorder(BorderFactory.createTitledBorder("Manage Maintenance"));

        // Adding form fields
        panel.add(new JLabel("Room ID:"));
        roomIdField = new JTextField(15);
        panel.add(roomIdField);

        panel.add(new JLabel("Issue Description:"));
        issueField = new JTextField(15);
        panel.add(issueField);

        panel.add(new JLabel("Scheduled Date (YYYY-MM-DD):"));
        scheduledDateField = new JTextField(15);
        panel.add(scheduledDateField);

        panel.add(new JLabel("Status (Pending/In Progress/Completed):"));
        statusField = new JTextField(15);
        panel.add(statusField);

        panel.add(new JLabel("Maintenance ID:"));
        maintenanceIdField = new JTextField(15);
        panel.add(maintenanceIdField);

        // Button panel with action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add Maintenance");
        updateButton = new JButton("Update Status");
        deleteButton = new JButton("Delete Maintenance");
        viewButton = new JButton("View Maintenance");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Table for viewing maintenance records
        String[] columnNames = {"Maintenance ID", "Room ID", "Issue", "Scheduled Date", "Status"};
        maintenanceTable = new JTable(new Object[0][5], columnNames);
        JScrollPane tableScroll = new JScrollPane(maintenanceTable);

        // Layout for the frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        mainPanel.add(buttonPanel);
        mainPanel.add(tableScroll);

        add(mainPanel);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMaintenance();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMaintenance();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMaintenance();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewMaintenance();
            }
        });
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
            String maintenanceIdText = maintenanceIdField.getText();
            if (maintenanceIdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Maintenance ID.");
                return;
            }
            int maintenanceId = Integer.parseInt(maintenanceIdText);
            String status = statusField.getText();

            if (status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid status.");
                return;
            }

            maintenanceDAO.updateMaintenanceStatus(maintenanceId, status);
            JOptionPane.showMessageDialog(this, "Maintenance status updated.");
            viewMaintenance(); // Refresh the maintenance records view
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
            // Retrieve maintenance records from the database
            List<Maintenance> maintenanceRecords = maintenanceDAO.getMaintenanceList();

            // Convert the records into a 2D array for JTable
            String[][] data = new String[maintenanceRecords.size()][5];
            for (int i = 0; i < maintenanceRecords.size(); i++) {
                Maintenance record = maintenanceRecords.get(i);
                data[i][0] = String.valueOf(record.getMaintenanceId());
                data[i][1] = String.valueOf(record.getRoomId());
                data[i][2] = record.getIssueDescription();
                data[i][3] = record.getScheduledDate();
                data[i][4] = record.getStatus();
            }

            // Update the table model with the retrieved data
            maintenanceTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Maintenance ID", "Room ID", "Issue", "Scheduled Date", "Status"}));
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MaintenanceScreen().setVisible(true);
            }
        });
    }
}
