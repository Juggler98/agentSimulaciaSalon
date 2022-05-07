package entities.zakaznik;

import OSPABA.Entity;
import OSPABA.Simulation;
import entities.Miesto;

import java.util.ArrayList;
import java.util.Arrays;

public class Zakaznik extends Entity implements Comparable<Zakaznik> {

    private static int pocetZakaznikov = 0;

    private double casPrichodu;
    private double casOdchodu;
    private final double[] casZaciatkuObsluhy = new double[5]; //Objednavka, Uces, HlbkoveCistenie, Licenie,  Platba
    private StavZakaznika stavZakaznika;
    private final int poradie;

    private boolean obsluzeny = false;
    private TypZakaznika typZakaznika;
    private boolean hlbkoveLicenie = false;
    private boolean goToHlbkoveLicenie = false;

    private boolean odchadza = false;

    private final boolean autom;
    private boolean zaparkoval = true;
    private PolohaZakaznika poloha;
    private Miesto miesto;

    private int spokojnost = 0;

    private final ArrayList<Integer> preskumane = new ArrayList<>();

    private final double speed;

    public Zakaznik(Simulation mySim, boolean autom, double speed) {
        super(mySim);
        Arrays.fill(casZaciatkuObsluhy, 0.0);
        this.poradie = ++pocetZakaznikov;
        this.autom = autom;
        this.speed = speed;
    }

//    public Zakaznik(int id, Simulation mySim) {
//        super(id, mySim);
//    }

    public double getCasPrichodu() {
        return casPrichodu;
    }

    public void setCasZaciatkuObsluhy(int i, double casCakania) {
        this.casZaciatkuObsluhy[i] = casCakania;
    }

    public double getCasZaciatkuObsluhy(int i) {
        return casZaciatkuObsluhy[i];
    }

    public boolean isObsluzeny() {
        return obsluzeny;
    }

    public void setObsluzeny() {
        this.obsluzeny = true;
    }

    public TypZakaznika getTypZakaznika() {
        return typZakaznika;
    }

    public boolean isGoToHlbkoveLicenie() {
        return goToHlbkoveLicenie;
    }

    public boolean isHlbkoveLicenie() {
        return hlbkoveLicenie;
    }

    public void setHlbkoveLicenie(boolean hlbkoveLicenie) {
        this.hlbkoveLicenie = hlbkoveLicenie;
    }

    public void setTypZakaznika(TypZakaznika typZakaznika) {
        this.typZakaznika = typZakaznika;
    }

    public void setGoToHlbkoveLicenie(boolean goToHlbkoveLicenie) {
        this.goToHlbkoveLicenie = goToHlbkoveLicenie;
    }

    public double getCasOdchodu() {
        return casOdchodu;
    }

    public void setCasOdchodu(double casOdchodu) {
        this.casOdchodu = casOdchodu;
    }

    public void setStavZakaznika(StavZakaznika stavZakaznika) {
        this.stavZakaznika = stavZakaznika;
    }

    public StavZakaznika getStavZakaznika() {
        return stavZakaznika;
    }

    public int getPoradie() {
        return poradie;
    }

    public boolean isAutom() {
        return autom;
    }

    public void setCasPrichodu(double casPrichodu) {
        this.casPrichodu = casPrichodu;
    }

    public static void init() {
        Zakaznik.pocetZakaznikov = 0;
    }

    public PolohaZakaznika getPoloha() {
        return poloha;
    }

    public void setPoloha(PolohaZakaznika poloha) {
        this.poloha = poloha;
    }

    public double getSpeed() {
        if (speed < 0) {
            throw new IllegalStateException("When using getSpeed() speed should be > 0");
        }
        return speed;
    }

    public Miesto getMiesto() {
        return miesto;
    }

    public void setMiesto(Miesto miesto) {
        this.miesto = miesto;
    }

    public ArrayList<Integer> preskumane() {
        return preskumane;
    }

    public boolean zaparkoval() {
        return zaparkoval;
    }

    public void setZaparkoval(boolean zaparkoval) {
        this.zaparkoval = zaparkoval;
    }

    public boolean odchadza() {
        return odchadza;
    }

    public void setOdchadza() {
        this.odchadza = true;
    }

    public int getSpokojnost() {
        return spokojnost;
    }

    public void incSpokojnost(int spokojnost) {
        this.spokojnost += spokojnost;
    }

    @Override
    public int compareTo(Zakaznik o) {
        if (obsluzeny && o.obsluzeny)
            return 0;
        if (obsluzeny)
            return -1;
        if (o.obsluzeny)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "ZakaznikSalonu{" +
                "casPrichodu=" + casPrichodu +
                ", casOdchodu=" + casOdchodu +
                ", casZaciatkuObsluhy=" + Arrays.toString(casZaciatkuObsluhy) +
                ", stavZakaznika=" + stavZakaznika +
                ", poradie=" + poradie +
                ", obsluzeny=" + obsluzeny +
                ", typZakaznika=" + typZakaznika +
                ", hlbkoveLicenie=" + hlbkoveLicenie +
                ", goToHlbkoveLicenie=" + goToHlbkoveLicenie +
                '}';
    }

}
