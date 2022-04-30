package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="9"
public class AgentUcesov extends Agent
{
	public AgentUcesov(int id, Simulation mySim, Agent parent)
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
		new ManagerUcesov(Id.managerUcesov, mySim(), this);
		new ProcesObsluhyUcesy(Id.procesObsluhyUcesy, mySim(), this);
		addOwnMessage(Mc.obsluhaUcesy);
	}
	//meta! tag="end"
}