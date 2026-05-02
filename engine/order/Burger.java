package engine.order;

import inventory.Ingredient;

import java.util.HashMap;
import java.util.Map;

final class Burger extends MenuItem {
    Burger() {
        super("Burger");
    }

    @Override
    public Map<Ingredient, Integer> getIngredients() {
        Map<Ingredient, Integer> map = new HashMap<>();
        map.put(Ingredient.BUNS, 2);
        map.put(Ingredient.MEAT, 1);
        map.put(Ingredient.VEGETABLES, 1);
        return map;
    }

    @Override
    public ApplianceKind getRequiredAppliance() {
        return ApplianceKind.GRILL;
    }
}
