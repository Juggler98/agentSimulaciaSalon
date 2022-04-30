package entities;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Zamestnanec extends Entity implements Comparable<Zamestnanec> {

    private Double odpracovanyCas = 0.0;
    private Double zaciatokObsluhy = 0.0;
    private Double vyuzitie = 0.0;
    private boolean obsluhuje = false;
    private int obsluhujeZakaznika = 0;

    public Zamestnanec(Simulation mySim) {
        super(mySim);
    }

    public Zamestnanec(int id, Simulation mySim) {
        super(id, mySim);
    }

    public void addOdpracovanyCas(double odpracovanyCas) {
        this.odpracovanyCas += odpracovanyCas;
    }

    public double getOdpracovanyCas() {
        return odpracovanyCas;
    }

    public void setObsluhuje(boolean obsluhuje) {
        this.obsluhuje = obsluhuje;
    }

    public boolean isObsluhuje() {
        return obsluhuje;
    }

    public Double getZaciatokObsluhy() {
        return zaciatokObsluhy;
    }

    public void setZaciatokObsluhy(Double zaciatokObsluhy) {
        this.zaciatokObsluhy = zaciatokObsluhy;
    }

    public Double getVyuzitie() {
        return vyuzitie;
    }

    public void setVyuzitie(Double vyuzitie) {
        this.vyuzitie = vyuzitie;
    }

    public void setObsluhujeZakaznika(int obsluhujeZakaznika) {
        this.obsluhujeZakaznika = obsluhujeZakaznika;
    }

    public int getObsluhujeZakaznika() {
        return obsluhujeZakaznika;
    }

    @Override
    public int compareTo(Zamestnanec o) {
        return odpracovanyCas.compareTo(o.odpracovanyCas);
    }

}
