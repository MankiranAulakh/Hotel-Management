package hotelManagement.InventoryObserver;

public class SMSNotifier implements Observer {
    @Override
    public void update(double inventoryLevel) {
        if (inventoryLevel < 50) {
            sendSMS(inventoryLevel);  // Send SMS alert if inventory is low
        }
    }

    private void sendSMS(double inventoryLevel) {
        System.out.println("SMS Alert: Inventory level is low! Current inventory: " + inventoryLevel);
    }
}

