package engine.appliance;

import engine.order.ApplianceKind;
import engine.order.MenuItem;

public class Fryer implements IAppliance {

    @Override
    public boolean canProcess(MenuItem item) {
        return item.getRequiredAppliance() == ApplianceKind.FRYER;
    }

    @Override
    public void processTask(MenuItem item) {
        System.out.println("Fryer cooking: " + item.getName());
    }
}
