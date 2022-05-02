package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="2"
public class AgentOkolia extends Agent {

    private ContinualAssistant planovacPrichodovZakaznikov;

    public AgentOkolia(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.novyZakaznik);
        addOwnMessage(Mc.zatvorenie);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerOkolia(Id.managerOkolia, mySim(), this);
        new PlanovacUzavretia(Id.planovacUzavretia, mySim(), this);
        new PlanovacPrichodyZakaznika(Id.planovacPrichodyZakaznika, mySim(), this);
        addOwnMessage(Mc.init);
        addOwnMessage(Mc.odchodZakaznika);
    }
    //meta! tag="end"
}