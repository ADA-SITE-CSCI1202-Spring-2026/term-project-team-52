package project.model;

/*
 * ─────────────────────────────────────────────────────────────────────────
 *  THIS CLASS IS OWNED BY MEMBER A (Menu & Orders).
 *  Only the two methods below are needed by the UI layer.
 * ─────────────────────────────────────────────────────────────────────────
 */
public abstract class Order {

    /**
     * Human-readable name shown in the Orders queue list.
     * e.g. "Classic Burger Meal", "Veggie Plate", "Fryer Snack + Drink"
     */
    public abstract String getDisplayName();
}
