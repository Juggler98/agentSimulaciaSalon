package agents;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.PriorityQueue;

//meta! id="8"
public class AgentRecepcie extends AgentPracovnika {

    int pocetObsluhovanychRecepcia = 0;
    boolean zavrete = false;
    double casObjednavky = 0;

    public AgentRecepcie(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        rad = new PriorityQueue<>();
        init();
        addOwnMessage(Mc.koniecObsluhyRecepcia);
        setProces((ContinualAssistant) findAssistant(Id.procesObsluhyRecepcia));
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        inicializuj(((MySimulation) mySim()).properties().getPocetRecepcnych());
        pocetObsluhovanychRecepcia = 0;
        zavrete = false;
        casObjednavky = 0;
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerRecepcie(Id.managerRecepcie, mySim(), this);
		new ProcesObsluhyRecepcia(Id.procesObsluhyRecepcia, mySim(), this);
		addOwnMessage(Mc.zmenaRadu);
		addOwnMessage(Mc.obsluhaRecepia);
		addOwnMessage(Mc.uzavri);
	}
	//meta! tag="end"

    public void uzavri() {
        zavrete = true;
        for (Zakaznik z : rad) {
            if (!z.isObsluzeny()) {
                z.setStavZakaznika(StavZakaznika.NEOBSLUZENY);
                z.setCasOdchodu(mySim().currentTime());

                if (z.isAutom()) {
                    z.getMiesto().setZakaznik(null);
                    z.setMiesto(null);
                }
            }
        }
        getDlzkaRaduStat().addValue(mySim().currentTime(), rad.size());
        rad.removeIf(z -> !z.isObsluzeny());
    }

    public void incPocetObsluhovanychRecepcia(int dlzka) {
        this.pocetObsluhovanychRecepcia += dlzka;
    }

    public int getPocetObsluhovanychRecepcia() {
        return pocetObsluhovanychRecepcia;
    }

    public boolean isZavrete() {
        return zavrete;
    }

    public void addCasObjednavky(double cas) {
        casObjednavky += cas;
    }

    public double getCasObjednavky() {
        return casObjednavky;
    }
}