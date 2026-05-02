package engine.order;

import inventory.Ingredient;

import java.util.Map;

public abstract class MenuItem {
    private final String name;

    protected MenuItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Map<Ingredient, Integer> getIngredients();

    public abstract ApplianceKind getRequiredAppliance();
}
