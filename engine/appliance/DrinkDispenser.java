package engine.appliance;

import engine.order.ApplianceKind;
import engine.order.MenuItem;

public class DrinkDispenser implements IAppliance {

    @Override
    public boolean canProcess(MenuItem item) {
        return item.getRequiredAppliance() == ApplianceKind.DRINK_DISPENSER;
    }

    @Override
    public void processTask(MenuItem item) {
        System.out.println("Drink dispenser pouring: " + item.getName());
    }
}
