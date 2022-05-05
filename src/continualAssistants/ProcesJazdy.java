package continualAssistants;

import OSPABA.*;
import entities.zakaznik.PolohaZakaznika;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="103"
public class ProcesJazdy extends Process
{

	private static final double speed = 20.0 / 3.6;


	public ProcesJazdy(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentParkoviska", id="104", type="Start"
	public void processStart(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
		message.setCode(Mc.koniecJazdy);
		zakaznik.setStavZakaznika(StavZakaznika.IDEOKOLO);
		switch (zakaznik.getPoloha()) {
			case START:
				zakaznik.setPoloha(PolohaZakaznika.A);
				hold((AgentParkoviska.width + AgentParkoviska.toA) / speed, message);
				break;
			case A:
				zakaznik.setPoloha(PolohaZakaznika.B);
				hold(AgentParkoviska.toB / speed, message);
				break;
			case B:
				zakaznik.setPoloha(PolohaZakaznika.C);
				hold(AgentParkoviska.toC / speed, message);
				break;
			case A_END:
				if (zakaznik.odchadza()) {
					hold(AgentParkoviska.toA / speed, message);
				} else {
					zakaznik.setPoloha(PolohaZakaznika.A);
					hold((AgentParkoviska.width + 2 * AgentParkoviska.toA) / speed, message);
				}
				break;
			case B_END:
				if (zakaznik.odchadza()) {
					hold((AgentParkoviska.toA + AgentParkoviska.toB) / speed, message);
				} else {
					zakaznik.setPoloha(PolohaZakaznika.A);
					hold((AgentParkoviska.width + AgentParkoviska.toB + 2 * AgentParkoviska.toA) / speed, message);
				}
				break;
			case C_END:
				if (zakaznik.odchadza()) {
					hold((AgentParkoviska.toA + AgentParkoviska.toB + AgentParkoviska.toC) / speed, message);
				} else {
					zakaznik.setPoloha(PolohaZakaznika.A);
					hold((AgentParkoviska.width + AgentParkoviska.toB + AgentParkoviska.toC + 2 * AgentParkoviska.toA) / speed, message);
				}
				break;
			default:
				throw new IllegalStateException("This should not happened");
		}

	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecJazdy:
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