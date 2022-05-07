package agents;

import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.Simulation;
import entities.Pracovnik;
import entities.zakaznik.Zakaznik;
import stats.WeightStat;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class AgentPracovnika extends Agent {

    protected Queue<Zakaznik> rad = new LinkedList<>();
    private final PriorityQueue<Pracovnik> volnyZamestnanci = new PriorityQueue<>();
    private Pracovnik[] zamestnanci;
    private final WeightStat dlzkaRaduStat = new WeightStat();
    protected ContinualAssistant proces;

    public AgentPracovnika(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

    }

    public void inicializuj(int pocetZamestnancov) {
        zamestnanci = new Pracovnik[pocetZamestnancov];
        volnyZamestnanci.clear();
        rad.clear();
        dlzkaRaduStat.init();
        for (int i = 0; i < pocetZamestnancov; i++) {
            zamestnanci[i] = new Pracovnik(mySim());
            volnyZamestnanci.add(zamestnanci[i]);
        }
    }

    public Pracovnik obsadZamestnanca() {
        return volnyZamestnanci.poll();
    }

    public void uvolniZamestnanca(Pracovnik zamestnanec) {
        volnyZamestnanci.add(zamestnanec);
    }

    public boolean jeNiektoVolny() {
        return !volnyZamestnanci.isEmpty();
    }

    public Pracovnik getZamestnanec(int i) {
        return zamestnanci[i];
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

    public ContinualAssistant getProces() {
        return proces;
    }

    public void setProces(ContinualAssistant proces) {
        this.proces = proces;
    }

    public int getRadSize() {
        return rad.size();
    }

    public WeightStat getDlzkaRaduStat() {
        return dlzkaRaduStat;
    }
}
