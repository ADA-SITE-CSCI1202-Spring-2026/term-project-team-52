package engine.simulation;

import engine.appliance.DrinkDispenser;
import engine.appliance.Fryer;
import engine.appliance.Grill;
import engine.appliance.IAppliance;
import engine.order.MenuItem;
import engine.order.Order;
import engine.order.OrderFactory;
import inventory.InventoryManager;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.function.Consumer;

public class RestaurantEngine {

    private final Queue<Order> orderQueue = new LinkedList<>();
    private final List<IAppliance> appliances = new ArrayList<>();
    private final InventoryManager inventory;
    private Timer autoTimer;

    private Consumer<String> logger;

    public RestaurantEngine(InventoryManager inventory) {
        this.inventory = inventory;
        appliances.add(new Grill());
        appliances.add(new Fryer());
        appliances.add(new DrinkDispenser());
    }

    public void setLogger(Consumer<String> logger) {
        this.logger = logger;
    }

    private void log(String message) {
        if (logger != null) {
            logger.accept(message);
        } else {
            System.out.println(message);
        }
    }

    public Queue<Order> getQueue() {
        return orderQueue;
    }

    public void startSimulation() {
        stopSimulation();
        autoTimer = new Timer(3000, e -> {
            Order order = generateRandomOrder();
            orderQueue.add(order);
            log("New Order: " + order.getName());
        });
        autoTimer.start();
    }

    public void stopSimulation() {
        if (autoTimer != null) {
            autoTimer.stop();
            autoTimer = null;
        }
    }

    private Order generateRandomOrder() {
        Random rand = new Random();
        int type = rand.nextInt(4);
        if (type == 0) {
            return OrderFactory.createBurgerOrder();
        }
        if (type == 1) {
            return OrderFactory.createFriesOrder();
        }
        if (type == 2) {
            return OrderFactory.createMealDealOrder();
        }
        return OrderFactory.createDrinkOrder();
    }

    public void cookNextOrder() {
        if (orderQueue.isEmpty()) {
            log("Queue is empty");
            return;
        }

        Order order = orderQueue.peek();

        if (!inventory.canFulfill(order.getTotalIngredients())) {
            log("ERROR: Cannot cook " + order.getName() + " - Insufficient resources!");
            return;
        }

        for (MenuItem item : order.getItems()) {
            boolean handled = false;
            for (IAppliance appliance : appliances) {
                if (appliance.canProcess(item)) {
                    handled = true;
                    break;
                }
            }
            if (!handled) {
                log("ERROR: No appliance for " + item.getName());
                return;
            }
        }

        orderQueue.poll();

        for (MenuItem item : order.getItems()) {
            for (IAppliance appliance : appliances) {
                if (appliance.canProcess(item)) {
                    appliance.processTask(item);
                    break;
                }
            }
        }

        if (inventory.consumeIfPossible(order.getTotalIngredients())) {
            inventory.addRevenue(order.getPrice());
            log("Order completed: " + order.getName());
        }
    }
}
