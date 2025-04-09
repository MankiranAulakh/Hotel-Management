package hotelManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GuestManagementScreen extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField, idProofField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable guestTable;
    private GuestDAO guestDAO;

    public GuestManagementScreen() {
        guestDAO = new GuestDAO();
        setTitle("Guest Management");
        setSize(600, 600); // Keep the size consistent
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for the form and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10)); // Use GridLayout for simplicity and neatness
        panel.setBorder(BorderFactory.createTitledBorder("Manage Guests"));

        // Adding form fields
        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField(15);
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(15);
        panel.add(lastNameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField(15);
        panel.add(emailField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField(15);
        panel.add(phoneField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField(15);
        panel.add(addressField);

        panel.add(new JLabel("ID Proof:"));
        idProofField = new JTextField(15);
        panel.add(idProofField);

        // Button panel with action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add Guest");
        updateButton = new JButton("Update Guest");
        deleteButton = new JButton("Delete Guest");
        viewButton = new JButton("View Guests");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Table for viewing guests
        String[] columnNames = {"Guest ID", "First Name", "Last Name", "Email", "Phone", "Address", "ID Proof"};
        guestTable = new JTable(new Object[0][7], columnNames);
        JScrollPane tableScroll = new JScrollPane(guestTable);

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
                addGuest();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGuest();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteGuest();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewGuests();
            }
        });
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
            if (updated) {
                JOptionPane.showMessageDialog(this, "Guest updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No guest found with the given email.");
            }
        }
    }

    private void deleteGuest() {
        String email = JOptionPane.showInputDialog(this, "Enter email of the guest to delete:");

        if (email != null && !email.trim().isEmpty()) {
            boolean deleted = guestDAO.deleteGuest(email);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Guest deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No guest found with the given email.");
            }
        }
    }

    private void viewGuests() {
        // Retrieve the guest data from the database
        List<Guest> guestList = guestDAO.getGuestList(); // Here you can implement a more specific query

        // Convert the guest data into a 2D array for JTable
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

        // Update the table model with the retrieved data
        guestTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Guest ID", "First Name", "Last Name", "Email", "Phone", "Address", "ID Proof"}));
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuestManagementScreen().setVisible(true);
            }
        });
    }
}
