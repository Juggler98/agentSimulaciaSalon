package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import entities.zakaznik.Zakaznik;
import myGenerators.RandExponential;
import simulation.*;
import agents.*;

//meta! id="22"
public class PlanovacPrichodyZakaznika extends Scheduler {

    private static final RandExponential randPrichod = new RandExponential(450);

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
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.novyZakaznik:
                MessageForm copy = message.createCopy();
                hold(randPrichod.nextValue(), copy);

                ((MyMessage) copy).setZakaznik(new Zakaznik(mySim().currentTime(), mySim()));
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