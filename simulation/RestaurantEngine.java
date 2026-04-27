package simulation;

import model.Order;

import java.util.*;
import java.util.function.Consumer;

/**
 * RestaurantEngine — STUB for Member D (Dashboard) to compile against.
 *
 * ─────────────────────────────────────────────────────────────────────────
 *  THIS FILE IS OWNED BY MEMBER C (Kitchen & Clock).
 *  Member D should compile against this stub. Member C replaces the bodies.
 * ─────────────────────────────────────────────────────────────────────────
 *
 *  The UI calls ONLY these public methods — never reaches into InventoryManager
 *  or the Queue directly.
 */
public class RestaurantEngine {

    public void setOnOrderAdded(Consumer<Order> callback) {
        // store and call this from the timer tick (c)
    }

    /** Called after any state change (cook, restock, load). Runs on EDT. */
    public void setOnStateChanged(Runnable callback) {
        // store and call after cookNext / buyIngredient / loadGame (c)
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────

    /** Starts the Swing Timer. Call once after the window is built. */
    public void start() {
        // start javax.swing.Timer at 2–5 sec random or fixed 3 sec (c)
    }

    // ── Queue read (for display) ──────────────────────────────────────────

    /**
     * Returns a snapshot of the pending queue as display strings (order names).
     * The real Queue<Order> stays inside the engine — UI gets this list only.
     *
     * @return list of order display names, head of queue first
     */
    public List<String> getQueueSnapshot() {
        // return queue.stream().map(Order::getDisplayName).toList()
        return new ArrayList<>();
    }

    // ── Cook ──────────────────────────────────────────────────────────────

    /**
     * Peeks at the queue head, validates ingredients, and if OK polls + commits.
     * Returns a human-readable log message for LogPanel to display.
     *
     * Examples:
     *   "Cooked: Classic Burger Meal — +$8.00"
     *   "ERROR: Cannot cook Veggie Plate - Insufficient Vegetables!"
     *   "Queue is empty — nothing to cook."
     */
    public String cookNext() {
        // peek → validate → poll → commit or log error (c+a)
        return "cookNext() not yet implemented.";
    }

    // ── Inventory read ────────────────────────────────────────────────────

    /**
     * Returns a copy of the ingredient counts keyed by ingredient name.
     * The UI renders this — it must NOT modify the returned map.
     *
     * @return e.g. {"BUNS" → 10, "MEAT" → 5, "VEGETABLES" → 8}
     */
    public Map<String, Integer> getInventorySnapshot() {
        // delegate to InventoryManager (b)
        return new LinkedHashMap<>();
    }

    /**
     * Returns current cash balance.
     */
    public double getCash() {
        // delegate to InventoryManager / Economy (b)
        return 100.00;
    }

    // ── Restock ───────────────────────────────────────────────────────────

    /**
     * Attempts to buy `qty` units of the named ingredient.
     * Returns a log message.
     *
     * Examples:
     *   "Restocked 5 Buns"
     *   "ERROR: Not enough cash to buy Buns."
     */
    public String buyIngredient(String ingredientName, int qty) {
        // InventoryManager.buy(ingredient, qty) (b)
        return "buyIngredient() not yet implemented.";
    }

    /**
     * Returns the list of ingredient names for the dropdown.
     */
    public List<String> getIngredientNames() {
        // return list from InventoryManager key set (b)
        return List.of("BUNS", "MEAT", "VEGETABLES");
    }

    /**
     * Returns the unit cost (in $) for restocking one of the named ingredient.
     * Used by RestockPanel to show a cost preview before the user clicks Buy.
     */
    public double getIngredientUnitCost(String ingredientName) {
        // return price from a price table in InventoryManager (b)
        return 2.00;
    }

    // ── Persistence ───────────────────────────────────────────────────────

    /**
     * Serialises current state (cash, inventory, queue) to `filePath`.
     * Throws IOException on failure (caller shows error dialog).
     */
    public void saveGame(String filePath) throws Exception {
        // write file (b+c)
        throw new UnsupportedOperationException("saveGame() not yet implemented.");
    }

    /**
     * Deserialises state from `filePath` and restores cash, inventory, queue.
     * Throws IOException / parse errors (caller shows error dialog).
     * After this returns, all four panels must be refreshed by the caller.
     */
    public void loadGame(String filePath) throws Exception {
        // read file, rebuild state (b+c)
        throw new UnsupportedOperationException("loadGame() not yet implemented.");
    }
}
