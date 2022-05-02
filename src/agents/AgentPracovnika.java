package agents;

import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.Simulation;
import entities.pracovnik.Pracovnik;
import entities.pracovnik.TypPracovnika;
import entities.zakaznik.Zakaznik;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class AgentPracovnika extends Agent {

    protected Queue<Zakaznik> rad = new LinkedList<>();
    private final TypPracovnika typPracovnika;
    private final PriorityQueue<Pracovnik> volnyZamestnanci = new PriorityQueue<>();
    private Pracovnik[] zamestnanci;
    private double lastRadChange;
    protected ContinualAssistant proces;

    public AgentPracovnika(int id, Simulation mySim, Agent parent, TypPracovnika typPracovnika) {
        super(id, mySim, parent);
        this.typPracovnika = typPracovnika;
    }

    public void inicializuj(int pocetZamestnancov) {
        zamestnanci = new Pracovnik[pocetZamestnancov];
        volnyZamestnanci.clear();
        rad.clear();
        lastRadChange = 0;
        for (int i = 0; i < pocetZamestnancov; i++) {
            zamestnanci[i] = new Pracovnik(mySim());
            volnyZamestnanci.add(zamestnanci[i]);
        }
    }

    public abstract int getPocetPracovnikov();

    public int getPocetZamestnancov() {
        return zamestnanci.length;
    }

    public Pracovnik obsadZamestnanca() {
        //System.out.println(volnyZamestnanci.size());
        return volnyZamestnanci.poll();
    }

    public void uvolniZamestnanca(Pracovnik zamestnanec) {
        volnyZamestnanci.add(zamestnanec);
    }

    public boolean jeNiektoVolny() {
        return !volnyZamestnanci.isEmpty();
    }

    public boolean niktoNepracuje() {
        return volnyZamestnanci.size() == zamestnanci.length;
    }

    public Pracovnik getZamestnanec(int i) {
        return zamestnanci[i];
    }

    public double getLastRadChange() {
        return lastRadChange;
    }

    public void setLastRadChange(double lastRadChange) {
        this.lastRadChange = lastRadChange;
    }

    public void pridajDoRadu(Zakaznik zakaznik) {
        rad.add(zakaznik);
    }

    public Zakaznik vyberZRadu() {
        return rad.poll();
    }

    public boolean isRadEmpty() {
        return rad.isEmpty();
    }

    public boolean ideNiektoPlatit() {
        if (rad.isEmpty())
            return false;
        return rad.peek().isObsluzeny();
    }

    public TypPracovnika getTypPracovnika() {
        return typPracovnika;
    }

    public ContinualAssistant getProces() {
        return proces;
    }

    public void setProces(ContinualAssistant proces) {
        this.proces = proces;
    }

    public int getRadSize() {
        return rad.size();
    }

}
