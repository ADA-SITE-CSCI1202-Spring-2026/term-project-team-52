package simulation;

import model.MenuItem;
import model.OrderFactory;

import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;
import java.io.*;

public class RestaurantEngine {

    private Queue<MenuItem> taskQueue = new LinkedList<>();

    private InventoryManager inventory = new InventoryManager();

    private Consumer<MenuItem> onOrderAdded;
    private Runnable onStateChanged;

    private Timer timer;

    
    public void setOnOrderAdded(Consumer<MenuItem> callback) {
        this.onOrderAdded = callback;
    }

    public void setOnStateChanged(Runnable callback) {
        this.onStateChanged = callback;
    }

    public void start() {
        timer = new Timer(3000, e -> {
            MenuItem newOrder = OrderFactory.create();
            taskQueue.add(newOrder);

            if (onOrderAdded != null) {
                onOrderAdded.accept(newOrder);
            }
        });
        timer.start();
    }

    public String cookNext() {
        if (taskQueue.isEmpty()) {
            return "Queue is empty - nothing to cook.";
        }

        MenuItem task = taskQueue.peek();
        Map<String, Integer> ingredients = task.getIngredients();

        boolean canCook = true;
        String missingItem = "";

        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            if (!inventory.hasEnough(entry.getKey(), entry.getValue())) {
                canCook = false;
                missingItem = entry.getKey();
                break;
            }
        }

        if (!canCook) {
            if (onStateChanged != null) onStateChanged.run();
            return "ERROR: Cannot cook " + task.getName() + " - Insufficient " + missingItem + "!";
        }

        taskQueue.poll();
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            inventory.consume(entry.getKey(), entry.getValue());
        }
        inventory.addMoney(20);

        if (onStateChanged != null) onStateChanged.run();
        return "Cooked: " + task.getName() + " — +$20.00";
    }

    public List<String> getQueueSnapshot() {
        List<String> names = new ArrayList<>();
        for (MenuItem item : taskQueue) {
            names.add(item.getName());
        }
        return names;
    }

    public Map<String, Integer> getInventorySnapshot() {
        return inventory.getStock();
    }

    public double getCash() {
        return inventory.getMoney();
    }

    public String buyIngredient(String ingredientName, int qty) {
        double cost = getIngredientUnitCost(ingredientName) * qty;

        if (!inventory.hasEnoughMoney(cost)) {
            return "ERROR: Not enough cash to buy " + ingredientName + ".";
        }

        inventory.restock(ingredientName, qty);
        inventory.spendMoney(cost);

        if (onStateChanged != null) onStateChanged.run();
        return "Restocked " + qty + " " + ingredientName;
    }

    public List<String> getIngredientNames() {
        return List.of("Buns", "Meat", "Dough", "Cheese", "Lettuce", "Tomato");
    }

    public double getIngredientUnitCost(String ingredientName) {
        return switch (ingredientName) {
            case "Buns"    -> 1.50;
            case "Meat"    -> 3.00;
            case "Dough"   -> 2.00;
            case "Cheese"  -> 2.50;
            case "Lettuce" -> 1.00;
            case "Tomato"  -> 1.00;
            default        -> 2.00;
        };
    }

    public void saveGame(String filePath) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        writer.write("SILICON_SPATULA_SAVE_V1");
        writer.newLine();

        writer.write("CASH:" + inventory.getMoney());
        writer.newLine();

        Map<String, Integer> stock = inventory.getStock();
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            writer.write("INGREDIENT:" + entry.getKey() + ":" + entry.getValue());
            writer.newLine();
        }

        for (MenuItem item : taskQueue) {
            writer.write("ORDER:" + item.getName());
            writer.newLine();
        }

        writer.close();
    }

    public void loadGame(String filePath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        taskQueue.clear();

        reader.readLine();

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("CASH:")) {
                double cash = Double.parseDouble(line.substring(5));
                inventory.setCash(cash);

            } else if (line.startsWith("INGREDIENT:")) {
                String[] parts = line.split(":");
                inventory.setStock(parts[1], Integer.parseInt(parts[2]));

            } else if (line.startsWith("ORDER:")) {
                String orderName = line.substring(6);
                MenuItem item = nameToOrder(orderName);
                if (item != null) taskQueue.add(item);
            }
        }

        reader.close();
        if (onStateChanged != null) onStateChanged.run();
    }

    private MenuItem nameToOrder(String name) {
        return switch (name) {
            case "Burger" -> new model.Burger();
            case "Pizza"  -> new model.Pizza();
            case "Salad"  -> new model.Salad();
            default       -> null;
        };
    }
}
