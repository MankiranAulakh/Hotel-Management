package hotelManagement.view;

import hotelManagement.dao.InventoryDAO;
import hotelManagement.model.InventoryItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InventoryManagementScreen extends JFrame {
    private JTextField itemNameField, categoryField, quantityField, reorderLevelField, usageFrequencyField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable inventoryTable;
    private InventoryDAO inventoryDAO;

    public InventoryManagementScreen() {
        inventoryDAO = new InventoryDAO();
        setTitle("Inventory Management");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for the form and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Inventory"));
        panel.setBackground(new Color(195, 250, 239));

        // Adding form fields
        panel.add(new JLabel("Item Name:"));
        itemNameField = new JTextField(15);
        itemNameField.setBackground(new Color(240, 240, 240));
        panel.add(itemNameField);

        panel.add(new JLabel("Category:"));
        categoryField = new JTextField(15);
        categoryField.setBackground(new Color(240, 240, 240));
        panel.add(categoryField);

        panel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(15);
        quantityField.setBackground(new Color(240, 240, 240));
        panel.add(quantityField);

        panel.add(new JLabel("Reorder Level:"));
        reorderLevelField = new JTextField(15);
        reorderLevelField.setBackground(new Color(240, 240, 240));
        panel.add(reorderLevelField);

        panel.add(new JLabel("Usage Frequency:"));
        usageFrequencyField = new JTextField(15);
        usageFrequencyField.setBackground(new Color(240, 240, 240));
        panel.add(usageFrequencyField);

        // Button panel with action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 248, 250));

        addButton = new JButton("Add Item");
        updateButton = new JButton("Update Quantity");
        deleteButton = new JButton("Delete Item");
        viewButton = new JButton("View Inventory");

        // Button styling
        addButton.setBackground(new Color(0, 123, 255));
        updateButton.setBackground(new Color(0, 123, 255));
        deleteButton.setBackground(new Color(220, 53, 69));
        viewButton.setBackground(new Color(40, 167, 69));

        addButton.setForeground(Color.WHITE);
        updateButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        viewButton.setForeground(Color.WHITE);

        // Rounded corner buttons
        addButton.setFocusPainted(false);
        updateButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);
        viewButton.setFocusPainted(false);

        // Hover effects
        addButton.setRolloverEnabled(true);
        updateButton.setRolloverEnabled(true);
        deleteButton.setRolloverEnabled(true);
        viewButton.setRolloverEnabled(true);

        addButton.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 2));
        updateButton.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 2));
        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 2));
        viewButton.setBorder(BorderFactory.createLineBorder(new Color(40, 167, 69), 2));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Table for viewing inventory
        String[] columnNames = {"Item ID", "Item Name", "Category", "Quantity", "Reorder Level", "Usage Frequency"};
        inventoryTable = new JTable(new Object[0][6], columnNames);
        JScrollPane tableScroll = new JScrollPane(inventoryTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1));

        // Layout for the frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 248, 250));
        mainPanel.add(panel);
        mainPanel.add(buttonPanel);
        mainPanel.add(tableScroll);

        add(mainPanel);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateItemQuantity();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteItem();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewInventory();
            }
        });
    }

    private void addItem() {
        String itemName = itemNameField.getText();
        String category = categoryField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        int reorderLevel = Integer.parseInt(reorderLevelField.getText());
        int usageFrequency = Integer.parseInt(usageFrequencyField.getText());

        inventoryDAO.addInventory(itemName, category, quantity, reorderLevel, usageFrequency);
        JOptionPane.showMessageDialog(this, "Inventory item added successfully.");
        clearFields();
    }

    private void updateItemQuantity() {
        String itemIdStr = JOptionPane.showInputDialog(this, "Enter Item ID to update quantity:");
        int itemId = Integer.parseInt(itemIdStr);
        int newQuantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter new quantity:"));

        inventoryDAO.updateInventoryQuantity(itemId, newQuantity);
        JOptionPane.showMessageDialog(this, "Inventory item updated successfully.");
    }

    private void deleteItem() {
        String itemIdStr = JOptionPane.showInputDialog(this, "Enter Item ID to delete:");
        int itemId = Integer.parseInt(itemIdStr);

        inventoryDAO.deleteInventory(itemId);
        JOptionPane.showMessageDialog(this, "Inventory item deleted successfully.");
    }

    private void viewInventory() {
        // Retrieve the inventory data from the database
        List<InventoryItem> inventoryItems = inventoryDAO.getInventoryList();

        // Convert the inventory data into a 2D array for JTable
        String[][] data = new String[inventoryItems.size()][6];
        for (int i = 0; i < inventoryItems.size(); i++) {
            InventoryItem item = inventoryItems.get(i);
            data[i][0] = String.valueOf(item.getItemId());
            data[i][1] = item.getItemName();
            data[i][2] = item.getCategory();
            data[i][3] = String.valueOf(item.getQuantity());
            data[i][4] = String.valueOf(item.getReorderLevel());
            data[i][5] = String.valueOf(item.getUsageFrequency());
        }

        // Update the table model with the retrieved data
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Item ID", "Item Name", "Category", "Quantity", "Reorder Level", "Usage Frequency"}));
    }

    private void clearFields() {
        itemNameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        reorderLevelField.setText("");
        usageFrequencyField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryManagementScreen().setVisible(true);
            }
        });
    }
}
