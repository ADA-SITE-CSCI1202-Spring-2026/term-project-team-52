package engine.appliance;

import engine.order.MenuItem;

public interface IAppliance {

    boolean canProcess(MenuItem item);

    void processTask(MenuItem item);
}
