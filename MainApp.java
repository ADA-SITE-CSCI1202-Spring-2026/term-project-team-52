import java.util.*;

public class MainApp {
    public static void main(String[] args) {
        Queue<String> taskQueue = new LinkedList<>();
        taskQueue.add("Burger");
        taskQueue.add("Pizza");
        
        System.out.println("Task queue size: " + taskQueue.size());
    }
}