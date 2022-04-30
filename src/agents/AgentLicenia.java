package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="10"
public class AgentLicenia extends Agent
{
	public AgentLicenia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
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