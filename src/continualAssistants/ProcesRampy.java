package continualAssistants;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import myGenerators.RandExponential;
import myGenerators.RandUniformDiscrete;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="125"
public class ProcesRampy extends Process
{

	private static final RandUniformDiscrete randRampa = new RandUniformDiscrete(5,60, Config.seedGenerator);

	public ProcesRampy(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentParkoviska", id="126", type="Start"
	public void processStart(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
		message.setCode(Mc.koniecRampy);
		zakaznik.setStavZakaznika(StavZakaznika.RAMPA);
		hold(randRampa.nextValue(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecRampy:
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