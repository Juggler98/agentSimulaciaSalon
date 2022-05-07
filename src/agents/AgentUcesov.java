package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="9"
public class AgentUcesov extends AgentPracovnika {

    double casUcesov = 0;

    public AgentUcesov(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.koniecObsluhyUcesy);
        setProces((ContinualAssistant) findAssistant(Id.procesObsluhyUcesy));
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        // Setup component for the next replication
        inicializuj(((MySimulation) mySim()).properties().getPocetKadernicok());
        casUcesov = 0;
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerUcesov(Id.managerUcesov, mySim(), this);
		new ProcesObsluhyUcesy(Id.procesObsluhyUcesy, mySim(), this);
		addOwnMessage(Mc.obsluhaUcesy);
	}
	//meta! tag="end"

    public void addCasUcesov(double cas) {
        casUcesov += cas;
    }

    public double getCasUcesov() {
        return casUcesov;
    }
}