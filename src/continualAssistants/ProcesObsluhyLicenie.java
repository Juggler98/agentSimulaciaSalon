package continualAssistants;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import myGenerators.RandTriangular;
import myGenerators.RandUniformDiscrete;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="31"
public class ProcesObsluhyLicenie extends Process {

	private final RandTriangular randHlbkoveCistenie = new RandTriangular(360, 900, 540);
	private final RandUniformDiscrete randLicenieJednoduche = new RandUniformDiscrete(10, 25);
	private final RandUniformDiscrete randLicenieZlozite = new RandUniformDiscrete(20, 100);
	private final Random randPercentageTypLicenia = new Random();

    public ProcesObsluhyLicenie(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentLicenia", id="32", type="Start"
	public void processStart(MessageForm message) {
		message.setCode(Mc.koniecObsluhyLicenie);
		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
		double holdTime;
		if (zakaznik.isGoToHlbkoveLicenie()) {
			holdTime = randHlbkoveCistenie.nextValue();
			zakaznik.setStavZakaznika(StavZakaznika.HLBKOVECISTENIE);
			zakaznik.setCasZaciatkuObsluhy(2, mySim().currentTime());
		} else {
			zakaznik.setStavZakaznika(StavZakaznika.LICENIE);
			zakaznik.setCasZaciatkuObsluhy(3, mySim().currentTime());
			double percentage = randPercentageTypLicenia.nextDouble();
			if (percentage < 0.3) {
				holdTime = randLicenieJednoduche.nextValue();
			} else {
				holdTime = randLicenieZlozite.nextValue();
			}
			holdTime *= 60;
		}
		hold(holdTime, message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
			case Mc.koniecObsluhyLicenie:
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
    public AgentLicenia myAgent() {
        return (AgentLicenia) super.myAgent();
    }

}