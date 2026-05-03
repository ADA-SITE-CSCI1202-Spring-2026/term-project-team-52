package engine.order;

import java.util.ArrayList;
import java.util.List;

public final class OrderFactory {

    private OrderFactory() {
    }

    public static Order createBurgerOrder() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new Burger());
        return new Order("Burger Order", items, 10);
    }

    public static Order createFriesOrder() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new Fries());
        return new Order("Fries Order", items, 7);
    }

    public static Order createMealDealOrder() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new Burger());
        items.add(new Fries());
        items.add(new Drink());
        return new Order("Meal Deal Order", items, 15);
    }

    public static Order createDrinkOrder() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new Drink());
        return new Order("Drink Order", items, 5);
    }

    /** Stable id for save/load (matches queue order). */
    public static String templateKey(Order order) {
        String n = order.getName();
        if ("Burger Order".equals(n)) {
            return "BURGER";
        }
        if ("Fries Order".equals(n)) {
            return "FRIES";
        }
        if ("Meal Deal Order".equals(n)) {
            return "MEAL_DEAL";
        }
        if ("Drink Order".equals(n)) {
            return "DRINK";
        }
        throw new IllegalArgumentException("Unknown order template: " + n);
    }

    public static Order fromTemplateKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("empty template key");
        }
        switch (key.trim()) {
            case "BURGER":
                return createBurgerOrder();
            case "FRIES":
                return createFriesOrder();
            case "MEAL_DEAL":
                return createMealDealOrder();
            case "DRINK":
                return createDrinkOrder();
            default:
                throw new IllegalArgumentException("Unknown template key: " + key);
        }
    }
}
