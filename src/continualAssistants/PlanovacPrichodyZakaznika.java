package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import myGenerators.RandExponential;
import simulation.*;
import agents.*;

import java.util.Random;

//meta! id="22"
public class PlanovacPrichodyZakaznika extends Scheduler {

    private static final Random seedGenerator = new Random();
    private static final RandExponential randPrichod = new RandExponential(450, seedGenerator); //TODO should be 720 in Sem 3 but 450 in Sem 2
    private static final RandExponential randPrichodAutom = new RandExponential(450, seedGenerator); //should be 450

    public PlanovacPrichodyZakaznika(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
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
        if (((MySimulation) mySim()).ajParkovisko()) {
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

                zakaznik = new Zakaznik(mySim(), false);
                zakaznik.setCasPrichodu(mySim().currentTime());
                ((MyMessage) message).setZakaznik(zakaznik);
                ((MySimulation) mySim()).getZakaznici().add(zakaznik);
                zakaznik.setStavZakaznika(StavZakaznika.PRICHOD);
                assistantFinished(message);
                break;

            case Mc.novyZakaznikAutom:
                ((MySimulation) mySim()).getStatsVykonov()[10]++;
                holdTime = randPrichodAutom.nextValue();
                if (mySim().currentTime() + holdTime <= Config.endTime) {
                    MessageForm copy = message.createCopy();
                    hold(holdTime, copy);
                }

                zakaznik = new Zakaznik(mySim(), true);
                ((MyMessage) message).setZakaznik(zakaznik);
                ((MySimulation) mySim()).getZakaznici().add(zakaznik);
                zakaznik.setStavZakaznika(StavZakaznika.PARKOVANIE);
                assistantFinished(message);
                break;
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
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