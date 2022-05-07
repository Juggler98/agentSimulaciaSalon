package continualAssistants;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import myGenerators.RandUniformContinuous;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="25"
public class ProcesObsluhyRecepcia extends Process {

    private static final RandUniformContinuous randObjednavka = new RandUniformContinuous(200 - 120, 200 + 120, Config.seedGenerator);
    private static final RandUniformContinuous randPlatba = new RandUniformContinuous(180 - 50, 180 + 50, Config.seedGenerator);

    public ProcesObsluhyRecepcia(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentRecepcie", id="26", type="Start"
	public void processStart(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        message.setCode(Mc.koniecObsluhyRecepcia);
        double holdTime;
        if (zakaznik.isObsluzeny()) {
            holdTime = randPlatba.nextValue();
            zakaznik.setCasZaciatkuObsluhy(4, mySim().currentTime());
            zakaznik.setStavZakaznika(StavZakaznika.PLATBA);
        } else {
            holdTime = randObjednavka.nextValue();
            myAgent().incPocetObsluhovanychRecepcia(1);
            ((MySimulation) mySim()).getStatsVykonov()[8]++;

            zakaznik.setCasZaciatkuObsluhy(0, mySim().currentTime());
            zakaznik.setStavZakaznika(StavZakaznika.OBJEDNAVKA);

            myAgent().addCasObjednavky(mySim().currentTime() - zakaznik.getCasPrichodu());
        }
        hold(holdTime, message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.koniecObsluhyRecepcia:
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
    public AgentRecepcie myAgent() {
        return (AgentRecepcie) super.myAgent();
    }

}