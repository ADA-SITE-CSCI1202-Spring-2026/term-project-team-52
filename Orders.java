import java.util.*;

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

class Pizza extends MenuItem {
    public Pizza() { super("Pizza"); }
    public Map<String, Integer> getIngredients() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Dough", 1);
        map.put("Cheese", 2);
        return map;
    }
}

class Salad extends MenuItem {
    public Salad() { super("Salad"); }
    public Map<String, Integer> getIngredients() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Lettuce", 2);
        map.put("Tomato", 1);
        return map;
    }
}

class OrderFactory {
    private static Random rand = new Random();
    public static MenuItem create() {
        int type = rand.nextInt(3);
        if (type == 0) return new Burger();
        if (type == 1) return new Pizza();
        return new Salad();
    }
}