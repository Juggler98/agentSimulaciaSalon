package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;


//meta! id="10"
public class AgentLicenia extends AgentPracovnika {

    public AgentLicenia(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.koniecObsluhyLicenie);
        setProces((ContinualAssistant) findAssistant(Id.procesObsluhyLicenie));
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        inicializuj(((MySimulation) mySim()).properties().getPocetKozmeticiek());
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerLicenia(Id.managerLicenia, mySim(), this);
		new ProcesObsluhyLicenie(Id.procesObsluhyLicenie, mySim(), this);
		addOwnMessage(Mc.obsluhaLicenie);
	}
	//meta! tag="end"

}