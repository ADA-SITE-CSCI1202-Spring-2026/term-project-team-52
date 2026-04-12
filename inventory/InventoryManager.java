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
}
