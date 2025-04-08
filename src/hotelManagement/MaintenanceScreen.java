package hotelManagement;

import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;

public class MaintenanceScreen extends JFrame {

private JTextField roomIdField, issueField, scheduledDateField, statusField, maintenanceIdField;
private JTextArea outputArea;
private Maintenance maintenanceDAO;

public MaintenanceScreen() {
    setTitle("Maintenance Management");
    setSize(600, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    maintenanceDAO = new Maintenance();

    JLabel roomIdLabel = new JLabel("Room ID:");
    roomIdField = new JTextField(10);

    JLabel issueLabel = new JLabel("Issue Description:");
    issueField = new JTextField(20);

    JLabel scheduledDateLabel = new JLabel("Scheduled Date (YYYY-MM-DD):");
    scheduledDateField = new JTextField(10);

    JLabel statusLabel = new JLabel("Status (Pending/In Progress/Completed):");
    statusField = new JTextField(15);

    JLabel maintenanceIdLabel = new JLabel("Maintenance ID:");
    maintenanceIdField = new JTextField(10);

    JButton addButton = new JButton("Add Maintenance");
    JButton getButton = new JButton("Get by ID");
    JButton updateButton = new JButton("Update Status");
    JButton deleteButton = new JButton("Delete Record");

    outputArea = new JTextArea(10, 50);
    outputArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(outputArea);

    JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
    inputPanel.setBorder(BorderFactory.createTitledBorder("Maintenance Form"));
    inputPanel.add(roomIdLabel);
    inputPanel.add(roomIdField);
    inputPanel.add(issueLabel);
    inputPanel.add(issueField);
    inputPanel.add(scheduledDateLabel);
    inputPanel.add(scheduledDateField);
    inputPanel.add(statusLabel);
    inputPanel.add(statusField);
    inputPanel.add(maintenanceIdLabel);
    inputPanel.add(maintenanceIdField);
    inputPanel.add(addButton);
    inputPanel.add(updateButton);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(getButton);
    buttonPanel.add(deleteButton);

    add(inputPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    addButton.addActionListener(e -> addMaintenance());
    getButton.addActionListener(e -> getMaintenanceById());
    updateButton.addActionListener(e -> updateMaintenance());
    deleteButton.addActionListener(e -> deleteMaintenance());
}

private void addMaintenance() {
    try {
        int roomId = Integer.parseInt(roomIdField.getText());
        String issue = issueField.getText();
        String scheduledDate = scheduledDateField.getText();
        String status = statusField.getText();

        maintenanceDAO.addMaintenance(roomId, issue, scheduledDate, status);
        outputArea.setText("Maintenance request logged successfully.");
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

private void getMaintenanceById() {
    try {
        int id = Integer.parseInt(maintenanceIdField.getText());
        JTextArea result = new JTextArea();
        result.setEditable(false);
        maintenanceDAO.getMaintenanceById(id);
        outputArea.setText("Check console for maintenance details.");
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

private void updateMaintenance() {
    try {
        int id = Integer.parseInt(maintenanceIdField.getText());
        String status = statusField.getText();
        maintenanceDAO.updateMaintenanceStatus(id, status);
        outputArea.setText("Maintenance status updated.");
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

private void deleteMaintenance() {
    try {
        int id = Integer.parseInt(maintenanceIdField.getText());
        maintenanceDAO.deleteMaintenance(id);
        outputArea.setText("Maintenance record deleted.");
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new MaintenanceScreen().setVisible(true));
}
}