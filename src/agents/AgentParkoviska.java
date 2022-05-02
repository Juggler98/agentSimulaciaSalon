package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="57"
public class AgentParkoviska extends Agent {
    public AgentParkoviska(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
		addOwnMessage(Mc.koniecParkovania);
	}

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerParkoviska(Id.managerParkoviska, mySim(), this);
        new ProcesParkovania(Id.procesParkovania, mySim(), this);
        addOwnMessage(Mc.parkovanie);
    }
    //meta! tag="end"
}