package agents;

import OSPABA.*;
import entities.pracovnik.TypPracovnika;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.PriorityQueue;

//meta! id="8"
public class AgentRecepcie extends AgentPracovnika {

    public AgentRecepcie(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent, Config.pocetRecepcnych, TypPracovnika.RECEPCIA);
        rad = new PriorityQueue<>();
        init();
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
        setProces(new ProcesObsluhyRecepcia(Id.procesObsluhyRecepcia, mySim(), this));
        addOwnMessage(Mc.obsluhaRecepia);
    }
    //meta! tag="end"

    @Override
    public int getPocetPracovnikov() {
        return Config.pocetRecepcnych;
    }
}