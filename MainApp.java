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

public class MainApp {
    private static Queue<MenuItem> taskQueue = new LinkedList<>();
    
    public static void main(String[] args) {
        taskQueue.add(new Burger());
        System.out.println("Queue size: " + taskQueue.size());
        System.out.println("Task: " + taskQueue.peek().getName());
    }
}