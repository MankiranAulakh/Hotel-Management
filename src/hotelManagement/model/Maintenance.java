package hotelManagement.model;

public class Maintenance {
    private int maintenanceId;
    private int roomId;
    private String issueDescription;
    private String scheduledDate;
    private String status;

    // Constructor
    public Maintenance(int maintenanceId, int roomId, String issueDescription, String scheduledDate, String status) {
        this.maintenanceId = maintenanceId;
        this.roomId = roomId;
        this.issueDescription = issueDescription;
        this.scheduledDate = scheduledDate;
        this.status = status;
    }

    // Getters and Setters
    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ToString Method
    @Override
    public String toString() {
        return "Maintenance{" +
                "maintenanceId=" + maintenanceId +
                ", roomId=" + roomId +
                ", issueDescription='" + issueDescription + '\'' +
                ", scheduledDate='" + scheduledDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
