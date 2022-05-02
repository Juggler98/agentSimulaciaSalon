package managers;

import OSPABA.*;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="57"
public class ManagerParkoviska extends Manager {
    public ManagerParkoviska(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="ProcesParkovania", id="66", type="Finish"
    public void processFinish(MessageForm message) {
        ((MySimulation) mySim()).getStatsVykonov()[11]++;
        message.setCode(Mc.parkovanie);
        message.setAddressee(mySim().findAgent(Id.agentSalonu));
        response(message);
    }

    //meta! sender="AgentSalonu", id="62", type="Request"
    public void processParkovanie(MessageForm message) {
        message.setAddressee(myAgent().findAssistant(Id.procesParkovania));
        startContinualAssistant(message);
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
            case Mc.parkovanie:
                processParkovanie(message);
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
    public AgentParkoviska myAgent() {
        return (AgentParkoviska) super.myAgent();
    }

}