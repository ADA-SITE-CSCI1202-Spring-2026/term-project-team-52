package engine.appliance;

import engine.order.ApplianceKind;
import engine.order.MenuItem;

public class Grill implements IAppliance {

    @Override
    public boolean canProcess(MenuItem item) {
        return item.getRequiredAppliance() == ApplianceKind.GRILL;
    }

    @Override
    public void processTask(MenuItem item) {
        System.out.println("Grill cooking: " + item.getName());
    }
}
