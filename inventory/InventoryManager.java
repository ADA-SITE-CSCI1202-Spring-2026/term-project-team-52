package inventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    private final Map<Ingredient, Integer> stock = new HashMap<>();
    private int cash;

    public InventoryManager(int startingCash, Map<Ingredient, Integer> startingStock) {
        cash = Math.max(0, startingCash);
        
        for (Ingredient ing : Ingredient.values()) {
            int amount = startingStock.getOrDefault(ing, 0);
            stock.put(ing, Math.max(0, amount));
        }
    }

    public static InventoryManager withRestaurantDefaults() {
        Map<Ingredient, Integer> startingStock = new HashMap<>();
        startingStock.put(Ingredient.BUNS, 5);
        startingStock.put(Ingredient.MEAT, 5);
        startingStock.put(Ingredient.VEGETABLES, 5);
        return new InventoryManager(100, startingStock);
    }

    public int getCash() {
        return cash;
    }

    public int getCount(Ingredient ingredient) {
        return stock.getOrDefault(ingredient, 0);
    }

    public void setCash(int newCash) {
        cash = Math.max(0, newCash);
    }

    public void setCount(Ingredient ingredient, int newCount) {
        stock.put(ingredient, Math.max(0, newCount));
    }

    public void addCount(Ingredient ingredient, int amount) {
        stock.put(ingredient, Math.max(0, stock.getOrDefault(ingredient, 0) + amount));
    }


    public Map<Ingredient, Integer> getStockSnapshot() {
        return new HashMap<>(stock);
    }

    public boolean buyIngredient(Ingredient ingredient, int units, int totalCost) {
        if (units <= 0 || totalCost < 0 || cash < totalCost) {
            return false;
        }
        cash -= totalCost;
        addCount(ingredient, units);
        return true;
    }


    public boolean consumeIfPossible(Map<Ingredient, Integer> recipe) {
        if (!canFulfill(recipe)) {
            return false;
        }

        for (Map.Entry<Ingredient, Integer> entry : recipe.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int needed = entry.getValue();
            setCount(ingredient, getCount(ingredient) - needed);
        }
        return true;
    }

    public boolean canFulfill(Map<Ingredient, Integer> recipe) {
        for (Map.Entry<Ingredient, Integer> entry : recipe.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int needed = entry.getValue();
            if (needed < 0 || getCount(ingredient) < needed) {
                return false;
            }
        }
        return true;
    }
}
