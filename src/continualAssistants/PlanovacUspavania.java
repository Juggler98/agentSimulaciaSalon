package continualAssistants;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Scheduler;
import OSPABA.Simulation;
import simulation.Mc;

public class PlanovacUspavania extends Scheduler {

    public PlanovacUspavania(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code())
        {
            case Mc.start:
                message.setCode(Mc.finish);
                hold(1, message);

                break;
            case Mc.finish:
                MessageForm copy = message.createCopy();
                hold(1, copy);
                break;

        }
    }
}
