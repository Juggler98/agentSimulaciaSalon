package simulation;

public class Properties {

    private final int pocetRecepcnych;
    private final int pocetKadernicok;
    private final int pocetKozmeticiek;
    private final boolean ajParkovisko;
    private final int pocetRadov;
    private final int strategia;
    private final boolean reklamy;

    public Properties(int pocetRecepcnych, int pocetKadernicok, int pocetKozmeticiek, boolean ajParkovisko, int pocetRadov, int strategia, boolean reklamy) {
        this.pocetRecepcnych = pocetRecepcnych;
        this.pocetKadernicok = pocetKadernicok;
        this.pocetKozmeticiek = pocetKozmeticiek;
        this.ajParkovisko = ajParkovisko;
        this.pocetRadov = pocetRadov;
        this.strategia = strategia;
        this.reklamy = reklamy;
    }

    public int getPocetRecepcnych() {
        return pocetRecepcnych;
    }

    public int getPocetKadernicok() {
        return pocetKadernicok;
    }

    public int getPocetKozmeticiek() {
        return pocetKozmeticiek;
    }

    public boolean ajParkovisko() {
        return ajParkovisko;
    }

    public int getPocetRadov() {
        return pocetRadov;
    }

    public int getStrategia() {
        return strategia;
    }

    public boolean isReklamy() {
        return reklamy;
    }
}
