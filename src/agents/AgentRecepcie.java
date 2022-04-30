package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="8"
public class AgentRecepcie extends Agent
{
	public AgentRecepcie(int id, Simulation mySim, Agent parent)
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
		new ManagerRecepcie(Id.managerRecepcie, mySim(), this);
		new ProcesObsluhyRecepcia(Id.procesObsluhyRecepcia, mySim(), this);
		addOwnMessage(Mc.obsluhaRecepia);
	}
	//meta! tag="end"
}