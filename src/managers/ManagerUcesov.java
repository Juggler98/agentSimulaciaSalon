package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="9"
public class ManagerUcesov extends ManagerPracovnika {
    public ManagerUcesov(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="AgentSalonu", id="18", type="Request"
    public void processObsluhaUcesy(MessageForm message) {
        zacniObsluhu(message);
//        message.setAddressee(myAgent().findAssistant(Id.procesObsluhyUcesy));
//        startContinualAssistant(message);
    }

    //meta! sender="ProcesObsluhyUcesy", id="29", type="Finish"
    public void processFinish(MessageForm message) {
        ukonciObsluhu(message);
        message.setCode(Mc.obsluhaUcesy);
        message.setAddressee(mySim().findAgent(Id.agentSalonu));
        response(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.obsluhaUcesy:
                processObsluhaUcesy(message);
                break;

            case Mc.finish:
                processFinish(message);
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