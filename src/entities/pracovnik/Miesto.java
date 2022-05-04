package entities.pracovnik;

import entities.zakaznik.Zakaznik;

public class Miesto {

    private final int pozicia;
    private final int rad;
    private boolean volne;
    private Zakaznik zakaznik;

    public Miesto(int rad, int pozicia) {
        this.rad = rad;
        this.pozicia = pozicia;
    }

    public int getPozicia() {
        return pozicia;
    }

    public boolean isVolne() {
        return volne;
    }

    public Zakaznik getZakaznik() {
        return zakaznik;
    }

    public void setVolne(boolean volne) {
        this.volne = volne;
    }

    public void setZakaznik(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
    }

    public int getRad() {
        return rad;
    }
}
