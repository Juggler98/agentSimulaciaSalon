package entities.zakaznik;

import OSPABA.Entity;
import OSPABA.Simulation;

import java.util.Arrays;
import java.util.Random;

public class Zakaznik extends Entity implements Comparable<Zakaznik> {

    private static int pocetZakaznikov = 0;

    private static final Random seedGenerator = new Random();
    private static final Random randPercentageTypZakaznika = new Random(seedGenerator.nextLong());
    private static final Random randPercentageCiseniePleti = new Random(seedGenerator.nextLong());

    private final double casPrichodu;
    private double casOdchodu;
    private final double[] casZaciatkuObsluhy = new double[5]; //Objednavka, Uces, HlbkoveCistenie, Licenie,  Platba
    private StavZakaznika stavZakaznika;
    private final int poradie;

    private boolean obsluzeny = false;
    private TypZakaznika typZakaznika;
    private boolean hlbkoveLicenie = false;
    private boolean goToHlbkoveLicenie = false;

    public Zakaznik(double casPrichodu, Simulation mySim) {
        super(mySim);
        this.casPrichodu = casPrichodu;
        Arrays.fill(casZaciatkuObsluhy, 0.0);
        this.poradie = ++pocetZakaznikov;

        double percentage = randPercentageTypZakaznika.nextDouble();
        if (percentage < 0.2) {
            typZakaznika = TypZakaznika.UCES;
            //salonSimulation.getStatsVykonov()[0]++;
        } else if (percentage < 0.35) {
//            salonSimulation.getStatsVykonov()[2]++;
            typZakaznika = TypZakaznika.LICENIE;
        } else {
//            salonSimulation.getStatsVykonov()[4]++;
            typZakaznika = TypZakaznika.UCESAJLICENIE;
        }
        if (typZakaznika == TypZakaznika.LICENIE || typZakaznika == TypZakaznika.UCESAJLICENIE) {
            double percentage2 = randPercentageCiseniePleti.nextDouble();
            if (percentage2 < 0.35) {
                //salonSimulation.getStatsVykonov()[6]++;
                goToHlbkoveLicenie = true;
                hlbkoveLicenie = true;
            }
        }
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
