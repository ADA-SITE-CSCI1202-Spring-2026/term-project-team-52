package engine.order;

import inventory.Ingredient;

import java.util.HashMap;
import java.util.Map;

final class Drink extends MenuItem {
    Drink() {
        super("Drink");
    }

    @Override
    public Map<Ingredient, Integer> getIngredients() {
        Map<Ingredient, Integer> map = new HashMap<>();
        map.put(Ingredient.SYRUP, 1);
        return map;
    }

    @Override
    public ApplianceKind getRequiredAppliance() {
        return ApplianceKind.DRINK_DISPENSER;
    }
}
