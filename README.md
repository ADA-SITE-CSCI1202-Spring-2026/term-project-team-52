# Silicon Spatula – Restaurant Tycoon
**Term Project – Team 52**

A Java Swing simulation game where you run a fast-food restaurant. Orders come in automatically, you cook them using available ingredients, restock when you run low, and try to keep the cash flowing.

---

## How to Run

**Requirements:** Java 11 or higher. Check with `java -version`.

**(Linux) Compile and run:**
```bash
javac -d out $(find . -name "*.java")
java -cp out RestaurantGUI
```

**(Windows) Compile and run:**
```bash
javac -d out MainApp.java RestaurantGUI.java engine\appliance\*.java engine\order\*.java engine\simulation\*.java inventory\*.java
java -cp out RestaurantGUI
```

The main entry point is `RestaurantGUI` (or `MainApp`, which calls it).

---

## Project Structure

```
├── MainApp.java                        # Entry point
├── RestaurantGUI.java                  # Swing UI – all four panels
├── engine/
│   ├── appliance/
│   │   ├── IAppliance.java             # Appliance interface
│   │   ├── Grill.java                  # Handles burgers
│   │   ├── Fryer.java                  # Handles fries
│   │   └── DrinkDispenser.java         # Handles drinks
│   ├── order/
│   │   ├── ApplianceKind.java          # Enum: GRILL, FRYER, DRINK_DISPENSER
│   │   ├── MenuItem.java               # Abstract base for menu items
│   │   ├── Burger.java
│   │   ├── Fries.java
│   │   ├── Drink.java
│   │   ├── Order.java                  # An order (name, items, price)
│   │   └── OrderFactory.java           # Creates burger/fries/drink/meal deal orders
│   └── simulation/
│       └── RestaurantEngine.java       # Core game loop, queue, cooking logic
└── inventory/
    ├── Ingredient.java                 # Enum: BUNS, MEAT, VEGETABLES, POTATO, SYRUP
    └── InventoryManager.java           # Tracks stock and cash
```

---

## The Four UI Panels

### 1. Orders Queue
Shows all pending customer orders in a numbered list. New orders arrive automatically every 4 seconds. Click **Cook Next Order** to process the order at the front of the queue.

### 2. Inventory
Displays current cash and the count of every ingredient. Updates immediately after cooking, restocking, or loading a save. Counts turn orange when low and red when empty.

### 3. Restock
Pick an ingredient from the dropdown and click **Buy Ingredient** to purchase 5 units. The panel shows the total cost before you buy. If you don't have enough cash, it tells you.

### 4. System Log
A scrolling terminal-style log of everything that happens — new orders, successful cooks, failed cooks with the specific missing ingredient, restocks, saves, and loads.

---

## Ingredients & Recipes

| Ingredient | Cost/unit | Used in |
|---|---|---|
| Buns | $2 | Burger (x2) |
| Meat | $4 | Burger (x1) |
| Vegetables | $3 | Burger (x1) |
| Potato | $2 | Fries (x2) |
| Syrup | $2 | Drink (x1) |

**Menu prices:** Burger $10 · Fries $7 · Drink $5 · Meal Deal $15

Starting inventory: 5 of each ingredient, $100 cash.

---

## Save / Load

Use the **Game** menu to save and load. Progress is saved to `savegame.dat` in the same directory. New Game resets everything back to defaults.