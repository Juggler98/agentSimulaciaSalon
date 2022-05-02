package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="8"
public class ManagerRecepcie extends ManagerPracovnika {
    public ManagerRecepcie(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="AgentSalonu", id="17", type="Request"
    public void processObsluhaRecepia(MessageForm message) {
        zacniObsluhu(message);
        //message.setAddressee(myAgent().findAssistant(Id.procesObsluhyRecepcia));
        //startContinualAssistant(message);
    }

    //meta! sender="ProcesObsluhyRecepcia", id="26", type="Finish"
    public void processFinish(MessageForm message) {
        ukonciObsluhu(message);

        message.setCode(Mc.obsluhaRecepia);
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
            case Mc.finish:
                processFinish(message);
                break;

            case Mc.obsluhaRecepia:
                processObsluhaRecepia(message);
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