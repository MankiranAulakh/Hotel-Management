package hotelManagement.InventoryObserver;

public class InventoryTest {
    public static void main(String[] args) {
        // Create an Inventory object with an initial level of 100 items
        Inventory inventory = new Inventory(100);

        // Create an InventoryNotifier that will notify staff if inventory goes below 50
        InventoryNotifier notifier = new InventoryNotifier(50);

        // Add the notifier as an observer of the inventory
        inventory.addObserver(notifier);

        // Update inventory levels and check if observers are notified
        inventory.updateInventory(60);  // No alert, inventory is above 50
        inventory.updateInventory(40);  // This will trigger an alert
    }
}

