package hotelManagement.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import hotelManagement.dao.ReportingDAO;
import hotelManagement.Main;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportingScreen extends JFrame {
    private JComboBox<String> reportTypeComboBox;
    private JTextField startDateField, endDateField;
    private JButton generateReportButton, dashboardButton;
    private JTextArea reportTextArea;

    public ReportingScreen() {
        setTitle("Reporting Dashboard");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(211, 219, 219));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Top Navigation with Dashboard Button 
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        dashboardButton = new JButton("🏠 Dashboard");
        styleButton(dashboardButton);
        dashboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashboardButton.addActionListener(e -> {
            dispose();
            new Main().setVisible(true); 
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(dashboardButton);
        topPanel.add(rightPanel, BorderLayout.NORTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Form Panel 
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 248, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Generate Report",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel typeLabel = new JLabel("Select Report Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(typeLabel, gbc);

        reportTypeComboBox = new JComboBox<>(new String[]{"Reservations", "Room Occupancy", "Housekeeping", "Financial"});
        gbc.gridx = 1;
        formPanel.add(reportTypeComboBox, gbc);

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(startDateLabel, gbc);

        startDateField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(startDateField, gbc);

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(endDateLabel, gbc);

        endDateField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(endDateField, gbc);

        generateReportButton = new JButton("Generate Report");
        styleButton(generateReportButton);
        generateReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(generateReportButton, gbc);

        // Report Display Area 
        reportTextArea = new JTextArea(12, 50);
        reportTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportTextArea.setEditable(false);
        reportTextArea.setLineWrap(true);
        reportTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));
        scrollPane.setPreferredSize(new Dimension(700, 250));

        // Assemble Panels
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        // Action Listener 
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            reportTextArea.setText("❗ Please enter both start and end dates.");
            return;
        }

        try {
            java.sql.Date.valueOf(startDate);
            java.sql.Date.valueOf(endDate);
        } catch (IllegalArgumentException e) {
            reportTextArea.setText("❗ Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        if (startDate.compareTo(endDate) > 0) {
            reportTextArea.setText("❗ Start date cannot be later than end date.");
            return;
        }

        ReportingDAO reportingDAO = new ReportingDAO();
        String reportContent = "";

        switch (reportType) {
            case "Reservations":
                reportContent = reportingDAO.generateReservationsReport(startDate, endDate);
                break;
            case "Room Occupancy":
                reportContent = reportingDAO.generateRoomOccupancyReport(startDate, endDate);
                break;
            case "Housekeeping":
                reportContent = reportingDAO.generateHousekeepingReport(startDate, endDate);
                break;
            case "Financial":
                reportContent = reportingDAO.generateFinancialReport(startDate, endDate);
                break;
            default:
                reportContent = "⚠ Invalid Report Type";
                break;
        }

        reportTextArea.setText(reportContent);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(51, 153, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 144, 255), 1),
                BorderFactory.createEmptyBorder(6, 15, 6, 15)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportingScreen().setVisible(true));
    }
}
