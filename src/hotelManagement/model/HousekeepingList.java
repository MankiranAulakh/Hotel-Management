package hotelManagement.model;

public class HousekeepingList {
    private int housekeepingId;
    private int roomId;
    private String staff;
    private String status;
    private String lastCleaned;

    // Constructor
    public HousekeepingList(int housekeepingId, int roomId, String staff, String status, String lastCleaned) {
        this.housekeepingId = housekeepingId;
        this.roomId = roomId;
        this.staff = staff;
        this.status = status;
        this.lastCleaned = lastCleaned;
    }

    // Getters
    public int getHousekeepingId() {
        return housekeepingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getStaff() {
        return staff;
    }

    public String getStatus() {
        return status;
    }

    public String getLastCleaned() {
        return lastCleaned;
    }

    // Setters (if needed)
    public void setHousekeepingId(int housekeepingId) {
        this.housekeepingId = housekeepingId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastCleaned(String lastCleaned) {
        this.lastCleaned = lastCleaned;
    }
}
