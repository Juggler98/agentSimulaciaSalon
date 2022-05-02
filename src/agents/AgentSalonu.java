package agents;

import OSPABA.*;
import simulation.*;
import managers.*;

//meta! id="36"
public class AgentSalonu extends Agent {
    public AgentSalonu(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerSalonu(Id.managerSalonu, mySim(), this);
        addOwnMessage(Mc.obsluhaZakaznika);
        addOwnMessage(Mc.obsluhaRecepia);
        addOwnMessage(Mc.obsluhaUcesy);
        addOwnMessage(Mc.parkovanie);
        addOwnMessage(Mc.obsluhaLicenie);
        addOwnMessage(Mc.uzavri);
    }
    //meta! tag="end"
}