package hotelManagement.view;

import javax.swing.*;

import hotelManagement.dao.ReportingDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportingScreen extends JFrame {
    private JComboBox<String> reportTypeComboBox;
    private JTextField startDateField, endDateField;
    private JButton generateReportButton;
    private JTextArea reportTextArea;
    
    public ReportingScreen() {
        setTitle("Generate Reports");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel for selecting report type and date range
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        
        panel.add(new JLabel("Select Report Type:"));
        reportTypeComboBox = new JComboBox<>(new String[]{"Reservations", "Room Occupancy", "Housekeeping", "Financial"});
        panel.add(reportTypeComboBox);

        panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        startDateField = new JTextField(15);
        panel.add(startDateField);

        panel.add(new JLabel("End Date (YYYY-MM-DD):"));
        endDateField = new JTextField(15);
        panel.add(endDateField);
        
        // Button panel for generating reports
        JPanel buttonPanel = new JPanel();
        generateReportButton = new JButton("Generate Report");
        buttonPanel.add(generateReportButton);
        
        // Area to display generated report
        reportTextArea = new JTextArea(10, 40);
        reportTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportTextArea);

        // Layout for the frame
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        
        // Button Action
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
        
     // Validate input dates
        if (startDate.isEmpty() || endDate.isEmpty()) {
            reportTextArea.setText("Please enter both start and end dates.");
            return;
        }
        
     // Simple date format validation
        try {
            java.sql.Date.valueOf(startDate); // Validate start date format
            java.sql.Date.valueOf(endDate); // Validate end date format
        } catch (IllegalArgumentException e) {
            reportTextArea.setText("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        if (startDate.compareTo(endDate) > 0) {
            reportTextArea.setText("Start date cannot be later than end date.");
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
                reportContent = "Invalid Report Type";
                break;
        }
        
     // Display the generated report
        reportTextArea.setText(reportContent);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReportingScreen().setVisible(true);
            }
        });
    }
}
