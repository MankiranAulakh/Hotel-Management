package hotelManagement.InventoryObserver;

public class InventoryNotifier implements Observer {
    private double threshold;

    public InventoryNotifier(double threshold) {
        this.threshold = threshold;  // Define a threshold for low inventory
    }

    @Override
    public void update(double inventoryLevel) {
        if (inventoryLevel < threshold) {
            sendAlert(inventoryLevel);  // Send an alert if inventory is below the threshold
        }
    }

    private void sendAlert(double inventoryLevel) {
        // Example of sending an alert 
        System.out.println("ALERT: Inventory level is low! Current inventory: " + inventoryLevel);
        
    }
}

