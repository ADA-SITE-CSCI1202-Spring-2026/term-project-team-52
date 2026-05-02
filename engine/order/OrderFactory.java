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
}
