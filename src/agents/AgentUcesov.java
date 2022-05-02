package agents;

import OSPABA.*;
import entities.pracovnik.TypPracovnika;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="9"
public class AgentUcesov extends AgentPracovnika {
    public AgentUcesov(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent, TypPracovnika.UCES);
        inicializuj(((MySimulation) mySim()).pocetKadernicok);
        init();
        addOwnMessage(Mc.koniecObsluhyUcesy);

    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerUcesov(Id.managerUcesov, mySim(), this);
        setProces(new ProcesObsluhyUcesy(Id.procesObsluhyUcesy, mySim(), this)); //TODO: setProces
        addOwnMessage(Mc.obsluhaUcesy);
    }
    //meta! tag="end"

    @Override
    public int getPocetPracovnikov() {
        return ((MySimulation) mySim()).pocetKadernicok;
    }
}