package continualAssistants;

import OSPABA.*;
import entities.pracovnik.Miesto;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="99"
public class ProcesChodze extends Process
{
	public ProcesChodze(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentParkoviska", id="100", type="Start"
	public void processStart(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
		zakaznik.setStavZakaznika(StavZakaznika.KRACA);
		message.setCode(Mc.koniecChodze);
		Miesto miesto = zakaznik.getMiesto();
		double distance = 0;
		switch (miesto.getRad()) {
			case 0:
				distance = AgentParkoviska.toA;
				break;
			case 1:
				distance = AgentParkoviska.toA + AgentParkoviska.toB;
				break;
			case 2:
				distance = AgentParkoviska.toA + AgentParkoviska.toB + AgentParkoviska.toC;
				break;
		}
		distance += (Config.miestRadu - miesto.getPozicia()) * AgentParkoviska.parkingSize;

		hold(distance / zakaznik.getSpeed(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecChodze:
				assistantFinished(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentParkoviska myAgent()
	{
		return (AgentParkoviska)super.myAgent();
	}

}