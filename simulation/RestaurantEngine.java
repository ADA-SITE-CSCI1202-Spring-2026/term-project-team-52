package simulation;

import model.Order;

import java.util.*;
import java.util.function.Consumer;

public class RestaurantEngine {

    public void setOnOrderAdded(Consumer<Order> callback) {
        // store and call this from the timer tick (c)
    }

    public void setOnStateChanged(Runnable callback) {
        // store and call after cookNext / buyIngredient / loadGame (c)
    }

    public void start() {
        // start javax.swing.Timer at 2–5 sec random or fixed 3 sec (c)
    }

    public List<String> getQueueSnapshot() {
        // return queue.stream().map(Order::getDisplayName).toList()
        return new ArrayList<>();
    }

    public String cookNext() {
        // peek → validate → poll → commit or log error (c+a)
        return "cookNext() not yet implemented.";
    }

    public Map<String, Integer> getInventorySnapshot() {
        // delegate to InventoryManager (b)
        return new LinkedHashMap<>();
    }

    public double getCash() {
        // delegate to InventoryManager / Economy (b)
        return 100.00;
    }

    public String buyIngredient(String ingredientName, int qty) {
        // InventoryManager.buy(ingredient, qty) (b)
        return "buyIngredient() not yet implemented.";
    }

    public List<String> getIngredientNames() {
        // return list from InventoryManager key set (b)
        return List.of("BUNS", "MEAT", "VEGETABLES");
    }

    public double getIngredientUnitCost(String ingredientName) {
        // return price from a price table in InventoryManager (b)
        return 2.00;
    }

    public void saveGame(String filePath) throws Exception {
        // write file (b+c)
        throw new UnsupportedOperationException("saveGame() not yet implemented.");
    }

    public void loadGame(String filePath) throws Exception {
        // read file, rebuild state (b+c)
        throw new UnsupportedOperationException("loadGame() not yet implemented.");
    }
}
