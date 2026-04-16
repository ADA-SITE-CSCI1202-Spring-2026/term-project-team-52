import java.util.*;
import inventory.Ingredient;
import inventory.InventoryManager;

abstract class MenuItem {
    private String name;
    public MenuItem(String name) { this.name = name; }
    public String getName() { return name; }
    public abstract Map<String, Integer> getIngredients();
}

class Burger extends MenuItem {
    public Burger() { super("Burger"); }
    public Map<String, Integer> getIngredients() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Buns", 2);
        map.put("Meat", 1);
        return map;
    }
}

public class MainApp {
    private static Queue<MenuItem> taskQueue = new LinkedList<>();
    
    public static void main(String[] args) {
        taskQueue.add(new Burger());
        System.out.println("Queue size: " + taskQueue.size());
        System.out.println("Task: " + taskQueue.peek().getName());

        // Small pantry smoke demo.
        Map<Ingredient, Integer> startingStock = new HashMap<>();
        startingStock.put(Ingredient.BUNS, 5);
        startingStock.put(Ingredient.MEAT, 4);
        startingStock.put(Ingredient.VEGETABLES, 6);

        InventoryManager inventory = new InventoryManager(100, startingStock);

        System.out.println("Initial cash: " + inventory.getCash());
        System.out.println("Initial stock: " + inventory.getStockSnapshot());

        boolean restockOk = inventory.buyIngredient(Ingredient.MEAT, 2, 12);
        System.out.println("Restock meat success: " + restockOk);

        Map<Ingredient, Integer> burgerRecipe = new HashMap<>();
        burgerRecipe.put(Ingredient.BUNS, 2);
        burgerRecipe.put(Ingredient.MEAT, 1);
        boolean cooked = inventory.consumeIfPossible(burgerRecipe);
        System.out.println("Cook burger success: " + cooked);

        System.out.println("Final cash: " + inventory.getCash());
        System.out.println("Final stock: " + inventory.getStockSnapshot());
    }
}