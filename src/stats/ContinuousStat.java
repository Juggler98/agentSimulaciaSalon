package stats;

public class ContinuousStat extends Stat {

    private double value = 0.0;

    public ContinuousStat() {
    }


    @Override
    public void init() {
        value = 0.0;
    }

    @Override
    public void addValue(Number value) {
        this.value += value.doubleValue();
    }

    public double getValue() {
        return value;
    }
}
