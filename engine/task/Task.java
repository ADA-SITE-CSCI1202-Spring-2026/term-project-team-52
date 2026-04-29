package engine.task;

import java.util.Map;


public abstract class Task {
    protected String name;
    protected Map<String, Integer> requiredResources;
    protected int reward;

    public Task(String name, Map<String, Integer> requiredResources, int reward) {
        this.name = name;
        this.requiredResources = requiredResources;
        this.reward = reward;
    }

    public Map<String, Integer> getRequiredResources() {
        return requiredResources;
    }

    public int getReward() {
        return reward;
    }

    public String getName() {
        return name;
    }
}