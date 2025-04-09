package hotelManagement.InventoryObserver;

public interface Observer {
    void update(double inventoryLevel);  // Method to receive notifications when inventory changes
}
