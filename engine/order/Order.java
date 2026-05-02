package engine.order;

import inventory.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private final String name;
    private final List<MenuItem> items;
    private final int price;

    public Order(String name, List<MenuItem> items, int price) {
        this.name = name;
        this.items = items;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public int getPrice() {
        return price;
    }

    public Map<Ingredient, Integer> getTotalIngredients() {
        Map<Ingredient, Integer> total = new HashMap<>();
        for (MenuItem item : items) {
            for (Map.Entry<Ingredient, Integer> e : item.getIngredients().entrySet()) {
                total.merge(e.getKey(), e.getValue(), Integer::sum);
            }
        }
        return total;
    }
}
