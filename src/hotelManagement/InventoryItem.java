package hotelManagement;

public class InventoryItem {
    private int itemId;
    private String itemName;
    private String category;
    private int quantity;
    private int reorderLevel;
    private int usageFrequency;

    // Constructor
    public InventoryItem(int itemId, String itemName, String category, int quantity, int reorderLevel, int usageFrequency) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.usageFrequency = usageFrequency;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getUsageFrequency() {
        return usageFrequency;
    }

    public void setUsageFrequency(int usageFrequency) {
        this.usageFrequency = usageFrequency;
    }
}
