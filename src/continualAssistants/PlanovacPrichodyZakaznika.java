package continualAssistants;

import OSPABA.*;
import entities.zakaznik.PolohaZakaznika;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import myGenerators.RandExponential;
import myGenerators.RandUniformContinuous;
import simulation.*;
import agents.*;

import java.util.Random;

//meta! id="22"
public class PlanovacPrichodyZakaznika extends Scheduler {

    private static RandExponential randPrichod = new RandExponential(720, Config.seedGenerator); //should be 720 in Sem 3 but 450 in Sem 2
    private static RandExponential randPrichodAutom = new RandExponential(450, Config.seedGenerator); //should be 450

    private static final RandUniformContinuous randPersonSpeed = new RandUniformContinuous(2.5 - 0.7, 2.5 + 0.7, Config.seedGenerator);

    public PlanovacPrichodyZakaznika(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
        boolean sem2 = false;
        boolean ad = ((MySimulation) mySim()).properties().isReklamy();
        double viacPrichodov = (ad ? 1.2 : 1);
        if (sem2) {
            randPrichod = new RandExponential(450 / viacPrichodov, Config.seedGenerator);
        } else {
            randPrichod = new RandExponential(720 / viacPrichodov, Config.seedGenerator);
        }
        randPrichodAutom = new RandExponential(450 / viacPrichodov, Config.seedGenerator);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentOkolia", id="23", type="Start"
	public void processStart(MessageForm message) {
        message.setCode(Mc.novyZakaznik);
        hold(randPrichod.nextValue(), message);

        MessageForm copy = message.createCopy();
        copy.setCode(Mc.novyZakaznikAutom);

        //if false -> without parking
        if (((MySimulation) mySim()).properties().ajParkovisko()) {
            hold(randPrichodAutom.nextValue(), copy);
        }
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        double holdTime;
        Zakaznik zakaznik;
        switch (message.code()) {
            case Mc.novyZakaznik:
                holdTime = randPrichod.nextValue();
                if (mySim().currentTime() + holdTime <= Config.endTime) {
                    MessageForm copy = message.createCopy();
                    hold(holdTime, copy);
                }

                zakaznik = new Zakaznik(mySim(), false, -1);
                zakaznik.setCasPrichodu(mySim().currentTime());
                ((MyMessage) message).setZakaznik(zakaznik);
                ((MySimulation) mySim()).getZakaznici().add(zakaznik);
                zakaznik.setStavZakaznika(StavZakaznika.PRICHADZA);
                assistantFinished(message);
                break;

            case Mc.novyZakaznikAutom:
                holdTime = randPrichodAutom.nextValue();
                if (mySim().currentTime() + holdTime <= Config.endTime) {
                    MessageForm copy = message.createCopy();
                    hold(holdTime, copy);
                }

                zakaznik = new Zakaznik(mySim(), true, randPersonSpeed.nextValue());
                ((MyMessage) message).setZakaznik(zakaznik);
                ((MySimulation) mySim()).getZakaznici().add(zakaznik);
                zakaznik.setStavZakaznika(StavZakaznika.PARKOVANIE);
                zakaznik.setPoloha(PolohaZakaznika.START);
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
    public AgentOkolia myAgent() {
        return (AgentOkolia) super.myAgent();
    }

}