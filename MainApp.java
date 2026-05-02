import java.util.*;

public class MainApp {
    private static Queue<MenuItem> taskQueue = new LinkedList<>();
    private static InventoryManager inventory = new InventoryManager();
    
    public static void main(String[] args) {
        taskQueue.add(new Burger());
        
        inventory.showInventory();
        
        processNextTask();
        
        inventory.showInventory();
    }
    
    private static void processNextTask() {
        if (taskQueue.isEmpty()) {
            System.out.println("No tasks to process");
            return;
        }
        
        MenuItem task = taskQueue.poll();
        System.out.println("Processing: " + task.getName());
        
        Map<String, Integer> ingredients = task.getIngredients();
        boolean canProcess = true;
        
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            if (!inventory.hasEnough(entry.getKey(), entry.getValue())) {
                System.out.println("ERROR: Not enough " + entry.getKey());
                canProcess = false;
                break;
            }
        }
        
        if (canProcess) {
            for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
                inventory.consume(entry.getKey(), entry.getValue());
            }
            inventory.addMoney(20);
            System.out.println("SUCCESS: " + task.getName() + " completed!");
        } else {
            System.out.println("FAILED: " + task.getName() + " - insufficient resources");
        }
    }
}