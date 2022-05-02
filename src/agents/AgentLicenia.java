package agents;

import OSPABA.*;
import entities.pracovnik.TypPracovnika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;
import java.util.PriorityQueue;

//meta! id="10"
public class AgentLicenia extends AgentPracovnika {


    public AgentLicenia(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent, TypPracovnika.LICENIE);
        inicializuj(((MySimulation) mySim()).pocetKozmeticiek);
        init();
        addOwnMessage(Mc.koniecObsluhyLicenie);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerLicenia(Id.managerLicenia, mySim(), this);
        setProces(new ProcesObsluhyLicenie(Id.procesObsluhyLicenie, mySim(), this));
        addOwnMessage(Mc.obsluhaLicenie);
    }
    //meta! tag="end"

    @Override
    public int getPocetPracovnikov() {
        return ((MySimulation) mySim()).pocetKozmeticiek;
    }
}