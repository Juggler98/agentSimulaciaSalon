package stats;

public class DiscreteStat extends Stat {

    private int value = 0;

    public DiscreteStat() {

    }

    public void init() {
        value = 0;
    }

    @Override
    public void addValue(Number value) {
        this.value += value.intValue();
    }

    public int getValue() {
        return value;
    }
}
