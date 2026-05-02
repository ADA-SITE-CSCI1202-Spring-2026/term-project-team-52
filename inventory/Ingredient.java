package inventory;

// Pantry ingredients for the Silicon Spatula tycoon.

public enum Ingredient {
    BUNS(2),
    MEAT(4),
    VEGETABLES(3),
    POTATO(2),
    SYRUP(2);

    private final int unitCost;

    Ingredient(int unitCost) {
        this.unitCost = unitCost;
    }

    public int getUnitCost() {
        return unitCost;
    }
}
