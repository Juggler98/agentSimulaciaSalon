package stats;

import simulation.MySimulation;

public class WeightStat {

    private double lastChangeTime = 0;
    private double value = 0;

    public WeightStat() {
    }

    public void addValue(double time, double value) {
        this.value += value * (time - lastChangeTime);
        lastChangeTime = time;
    }

    public double getValue() {
        if (lastChangeTime == 0)
            return 0;
        return value / lastChangeTime;
    }

    public void init() {
        this.lastChangeTime = 0;
        this.value = 0;
    }
}
