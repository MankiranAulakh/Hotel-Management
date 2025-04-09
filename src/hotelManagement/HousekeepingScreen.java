package hotelManagement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setTitle("Housekeeping Management");
        setSize(600, 600); // Keep size consistent
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10)); // GridLayout for the form
        formPanel.setBorder(BorderFactory.createTitledBorder("Manage Housekeeping"));

        // Form Fields
        formPanel.add(new JLabel("Housekeeping ID:"));
        housekeepingIdField = new JTextField(15);
        formPanel.add(housekeepingIdField);

        formPanel.add(new JLabel("Room ID:"));
        roomIdField = new JTextField(15);
        formPanel.add(roomIdField);

        formPanel.add(new JLabel("Assigned Staff:"));
        staffField = new JTextField(15);
        formPanel.add(staffField);

        formPanel.add(new JLabel("Cleaning Status:"));
        statusComboBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
        formPanel.add(statusComboBox);

        formPanel.add(new JLabel("Last Cleaned (yyyy-MM-dd HH:mm:ss):"));
        lastCleanedField = new JTextField(15);
        formPanel.add(lastCleanedField);

        // Button panel with action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add Record");
        updateButton = new JButton("Update Status");
        deleteButton = new JButton("Delete Record");
        viewButton = new JButton("View Record");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Display area for feedback messages
        displayArea = new JTextArea(5, 50);
        displayArea.setEditable(false);
        JScrollPane displayScroll = new JScrollPane(displayArea);
        
     // Table for viewing inventory
        String[] columnNames = {"Item ID", "Item Name", "Category", "Quantity", "Reorder Level", "Usage Frequency"};
        housekeepingTable = new JTable(new Object[0][6], columnNames);
        JScrollPane tableScroll = new JScrollPane(housekeepingTable);

        // Layout for the frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(tableScroll);

        add(mainPanel);

        // Action Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHousekeeping();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHousekeepingStatus();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHousekeepingRecord();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHousekeeping();
            }
        });
    }

    private void addHousekeeping() {
        String staff = staffField.getText();
        String status = (String) statusComboBox.getSelectedItem();
        String lastCleaned = lastCleanedField.getText();
        int roomId = Integer.parseInt(roomIdField.getText());

        // Call DAO method to add housekeeping record
        housekeepingDAO.addHousekeeping(roomId, staff, status, lastCleaned);
        displayArea.setText("Housekeeping record added successfully.");
        clearFields();
    }

    private void updateHousekeepingStatus() {
        // Get the Housekeeping ID
        String housekeepingIdStr = JOptionPane.showInputDialog(this, "Enter Housekeeping ID to update status:");
        int housekeepingId = Integer.parseInt(housekeepingIdStr);

        // Get the new status
        String newStatus = JOptionPane.showInputDialog(this, "Enter new status (Pending, In Progress, Completed):");

        // Get the last cleaned date
        String lastCleaned = JOptionPane.showInputDialog(this, "Enter last cleaned date (yyyy-MM-dd HH:mm:ss):");

        // Update the status via DAO
        housekeepingDAO.updateHousekeepingStatus(housekeepingId, newStatus, lastCleaned);
        
        // Display a success message
        displayArea.setText("Housekeeping record updated successfully.");
    }


    private void deleteHousekeepingRecord() {
        String housekeepingIdStr = JOptionPane.showInputDialog(this, "Enter Housekeeping ID to delete:");
        int housekeepingId = Integer.parseInt(housekeepingIdStr);

        // Delete via DAO
        housekeepingDAO.deleteHousekeeping(housekeepingId);
        displayArea.setText("Housekeeping record deleted successfully.");
    }

    private void viewHousekeepingRecord() {
        String housekeepingIdStr = JOptionPane.showInputDialog(this, "Enter Housekeeping ID to view:");
        int housekeepingId = Integer.parseInt(housekeepingIdStr);

        String result = housekeepingDAO.getHousekeepingById(housekeepingId);
        displayArea.setText(result);
    }
    
    private void viewHousekeeping() {
        // Retrieve the housekeeping data from the database
        List<HousekeepingList> housekeepingList = housekeepingDAO.getHousekeepingList();

        // Convert the housekeeping data into a 2D array for JTable
        String[][] data = new String[housekeepingList.size()][5];
        for (int i = 0; i < housekeepingList.size(); i++) {
            HousekeepingList record = housekeepingList.get(i);
            data[i][0] = String.valueOf(record.getHousekeepingId());
            data[i][1] = String.valueOf(record.getRoomId());
            data[i][2] = record.getStaff();
            data[i][3] = record.getStatus();
            data[i][4] = record.getLastCleaned();
        }

        // Update the table model with the retrieved data
        housekeepingTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Housekeeping ID", "Room ID", "Assigned Staff", "Cleaning Status", "Last Cleaned"}));
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
