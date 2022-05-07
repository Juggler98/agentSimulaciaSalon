package entities;

import entities.zakaznik.Zakaznik;

public class Miesto {

    private final int pozicia;
    private final int rad;
    private Zakaznik zakaznik;

    public Miesto(int rad, int pozicia) {
        this.rad = rad;
        this.pozicia = pozicia;
    }

    public int getPozicia() {
        return pozicia;
    }

    public Zakaznik getZakaznik() {
        return zakaznik;
    }

    public void setZakaznik(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
    }

    public int getRad() {
        return rad;
    }
}
