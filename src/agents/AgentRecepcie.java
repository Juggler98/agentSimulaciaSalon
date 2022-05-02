package agents;

import OSPABA.*;
import entities.pracovnik.TypPracovnika;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.PriorityQueue;

//meta! id="8"
public class AgentRecepcie extends AgentPracovnika {

    public AgentRecepcie(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent, TypPracovnika.RECEPCIA);
        rad = new PriorityQueue<>();
        init();
        inicializuj(((MySimulation) mySim()).pocetRecepcnych);
        addOwnMessage(Mc.koniecObsluhyRecepcia);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerRecepcie(Id.managerRecepcie, mySim(), this);
        setProces(new ProcesObsluhyRecepcia(Id.procesObsluhyRecepcia, mySim(), this)); //TODO: setProces
        addOwnMessage(Mc.zmenaRadu);
        addOwnMessage(Mc.obsluhaRecepia);
        addOwnMessage(Mc.uzavri);
    }
    //meta! tag="end"

    @Override
    public int getPocetPracovnikov() {
        return ((MySimulation) mySim()).pocetRecepcnych;
    }

    public void uzavri() {
        for (Zakaznik z : rad) {
            if (!z.isObsluzeny()) {
                z.setStavZakaznika(StavZakaznika.ODCHOD);
                z.setCasOdchodu(mySim().currentTime());

                if (z.isAutom()) {
                    ((MySimulation) mySim()).getFreeSlots().add(z.getZaparkovane());
                }
            }
        }
        ((MySimulation) mySim()).addDlzkaRadu(0, rad.size() * (mySim().currentTime() - getLastRadChange()));
        setLastRadChange(mySim().currentTime());
        rad.removeIf(z -> !z.isObsluzeny());
    }

}