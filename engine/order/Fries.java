package engine.order;

import inventory.Ingredient;

import java.util.HashMap;
import java.util.Map;

final class Fries extends MenuItem {
    Fries() {
        super("Fries");
    }

    @Override
    public Map<Ingredient, Integer> getIngredients() {
        Map<Ingredient, Integer> map = new HashMap<>();
        map.put(Ingredient.POTATO, 2);
        return map;
    }

    @Override
    public ApplianceKind getRequiredAppliance() {
        return ApplianceKind.FRYER;
    }
}
