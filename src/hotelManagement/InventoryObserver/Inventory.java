package hotelManagement.InventoryObserver;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Observer> observers = new ArrayList<>();
    private double inventoryLevel;  // Represents the current inventory level

    public Inventory(double initialLevel) {
        this.inventoryLevel = initialLevel;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(inventoryLevel);
        }
    }

    public void updateInventory(double newLevel) {
        this.inventoryLevel = newLevel;
        notifyObservers();  // Notify observers when inventory changes
    }

    public double getInventoryLevel() {
        return inventoryLevel;
    }
}

