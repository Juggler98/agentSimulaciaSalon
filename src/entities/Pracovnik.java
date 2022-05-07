package entities;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Pracovnik extends Entity implements Comparable<Pracovnik> {

    private Double odpracovanyCas = 0.0;
    private Double zaciatokObsluhy = 0.0;
    private Double vyuzitie = 0.0;
    private boolean obsluhuje = false;
    private int obsluhujeZakaznika = 0;

    public Pracovnik(Simulation mySim) {
        super(mySim);
    }

    public Pracovnik(int id, Simulation mySim) {
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
    public int compareTo(Pracovnik o) {
        return odpracovanyCas.compareTo(o.odpracovanyCas);
    }

    @Override
    public String toString() {
        return "Pracovnik{" +
                "odpracovanyCas=" + odpracovanyCas +
                ", zaciatokObsluhy=" + zaciatokObsluhy +
                ", vyuzitie=" + vyuzitie +
                ", obsluhuje=" + obsluhuje +
                ", obsluhujeZakaznika=" + obsluhujeZakaznika +
                '}';
    }
}
