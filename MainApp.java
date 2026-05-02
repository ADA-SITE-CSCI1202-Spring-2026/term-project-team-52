import engine.order.OrderFactory;
import engine.simulation.RestaurantEngine;
import inventory.InventoryManager;

/** Console smoke test; GUI replaces later. */
public class MainApp {

    public static void main(String[] args) {
        InventoryManager inventory = InventoryManager.withRestaurantDefaults();
        RestaurantEngine engine = new RestaurantEngine(inventory);
        engine.setLogger(System.out::println);

        engine.getQueue().add(OrderFactory.createBurgerOrder());
        engine.cookNextOrder();
    }
}
