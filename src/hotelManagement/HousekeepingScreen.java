package hotelManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HousekeepingScreen extends JFrame {
    private JTextField housekeepingIdField, roomIdField, staffField, lastCleanedField;
    private JComboBox<String> statusComboBox;
    private JTextArea displayArea;
    private Housekeeping housekeepingDAO;

    public HousekeepingScreen() {
        housekeepingDAO = new Housekeeping();
        setTitle("Housekeeping Management");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Housekeeping ID:"), gbc);
        gbc.gridx = 1;
        housekeepingIdField = new JTextField(10);
        panel.add(housekeepingIdField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Room ID:"), gbc);
        gbc.gridx = 3;
        roomIdField = new JTextField(10);
        panel.add(roomIdField, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Assigned Staff:"), gbc);
        gbc.gridx = 1;
        staffField = new JTextField(15);
        panel.add(staffField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Cleaning Status:"), gbc);
        gbc.gridx = 3;
        statusComboBox = new JComboBox<>(new String[] {"Pending", "In Progress", "Completed"});
        panel.add(statusComboBox, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Last Cleaned (yyyy-MM-dd HH:mm:ss):"), gbc);
        gbc.gridx = 1;
        lastCleanedField = new JTextField(15);
        panel.add(lastCleanedField, gbc);

        // Row 3 - Buttons
        gbc.gridy = 3;
        gbc.gridx = 0;
        JButton addBtn = new JButton("Add");
        panel.add(addBtn, gbc);
        gbc.gridx = 1;
        JButton viewBtn = new JButton("View");
        panel.add(viewBtn, gbc);
        gbc.gridx = 2;
        JButton updateBtn = new JButton("Update");
        panel.add(updateBtn, gbc);
        gbc.gridx = 3;
        JButton deleteBtn = new JButton("Delete");
        panel.add(deleteBtn, gbc);

        // Row 4 - Display Area
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        displayArea = new JTextArea(10, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        panel.add(scrollPane, gbc);

        add(panel);

        // Action Listeners
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int roomId = Integer.parseInt(roomIdField.getText());
                    String staff = staffField.getText();
                    String status = statusComboBox.getSelectedItem().toString();
                    String lastCleaned = lastCleanedField.getText();
                    housekeepingDAO.addHousekeeping(roomId, staff, status, lastCleaned);
                    displayArea.setText("Housekeeping record added.");
                } catch (Exception ex) {
                    displayArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(housekeepingIdField.getText());
                    displayArea.setText("");
                    housekeepingDAO.getHousekeepingById(id); // Still prints to console
                } catch (Exception ex) {
                    displayArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(housekeepingIdField.getText());
                    String status = statusComboBox.getSelectedItem().toString();
                    String lastCleaned = lastCleanedField.getText();
                    housekeepingDAO.updateHousekeepingStatus(id, status, lastCleaned);
                    displayArea.setText("Housekeeping status updated.");
                } catch (Exception ex) {
                    displayArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(housekeepingIdField.getText());
                    housekeepingDAO.deleteHousekeeping(id);
                    displayArea.setText("Housekeeping record deleted.");
                } catch (Exception ex) {
                    displayArea.setText("Error: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HousekeepingScreen().setVisible(true));
    }
}
