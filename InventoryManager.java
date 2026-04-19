import java.util.*;

public class InventoryManager {
    private Map<String, Integer> inventory;
    private int money;
    
    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Buns", 10);
        inventory.put("Meat", 5);
        money = 100;
    }
    
    public boolean hasEnough(String item, int amount) {
        return inventory.getOrDefault(item, 0) >= amount;
    }
    
    public boolean consume(String item, int amount) {
        if (!hasEnough(item, amount)) {
            System.out.println("ERROR: Insufficient " + item);
            return false;
        }
        inventory.put(item, inventory.get(item) - amount);
        return true;
    }
    
    public void add(String item, int amount) {
        inventory.put(item, inventory.get(item) + amount);
        System.out.println("Restocked " + amount + " " + item);
    }
    
    public void addMoney(int amount) {
        money += amount;
        System.out.println("Money: $" + money);
    }
    
    public void showInventory() {
        System.out.println("Inventory: " + inventory);
        System.out.println("Money: $" + money);
    }
    
    public int getMoney() { return money; }
}