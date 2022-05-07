package continualAssistants;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import myGenerators.EmpiricDiscrete;
import myGenerators.RandEmpiricDiscrete;
import myGenerators.RandUniformDiscrete;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="28"
public class ProcesObsluhyUcesy extends Process {

    private static final EmpiricDiscrete[] empiricDiscretesUcesZlozity = {new EmpiricDiscrete(30, 60, 0.4), new EmpiricDiscrete(61, 120, 0.6)};
    private static final EmpiricDiscrete[] empiricDiscretesUcesSvadobny = {new EmpiricDiscrete(50, 60, 0.2), new EmpiricDiscrete(61, 100, 0.3), new EmpiricDiscrete(101, 150, 0.5)};
    private static final RandUniformDiscrete randUcesJednoduchy = new RandUniformDiscrete(10, 30, Config.seedGenerator);
    private static final RandEmpiricDiscrete randUcesZlozity = new RandEmpiricDiscrete(empiricDiscretesUcesZlozity, Config.seedGenerator);
    private static final RandEmpiricDiscrete randUcesSvadobny = new RandEmpiricDiscrete(empiricDiscretesUcesSvadobny, Config.seedGenerator);
    private static final Random randPercentageTypUcesu = new Random(Config.seedGenerator.nextLong());

    public ProcesObsluhyUcesy(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentUcesov", id="29", type="Start"
	public void processStart(MessageForm message) {
        message.setCode(Mc.koniecObsluhyUcesy);

        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();

        double percentage = randPercentageTypUcesu.nextDouble();
        double holdTime;
        if (percentage < 0.4) {
            holdTime = randUcesJednoduchy.nextValue();
        } else if (percentage < 0.8) {
            holdTime = randUcesZlozity.nextValue();
        } else {
            holdTime = randUcesSvadobny.nextValue();
        }
        holdTime *= 60;

        zakaznik.setStavZakaznika(StavZakaznika.UCES);
        zakaznik.setCasZaciatkuObsluhy(1, mySim().currentTime());

        myAgent().addCasUcesov(holdTime);

        hold(holdTime, message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.koniecObsluhyUcesy:
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
    public AgentUcesov myAgent() {
        return (AgentUcesov) super.myAgent();
    }

}