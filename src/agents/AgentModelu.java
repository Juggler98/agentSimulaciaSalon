package agents;

import OSPABA.*;
import simulation.*;
import managers.*;

//meta! id="1"
public class AgentModelu extends Agent {
    public AgentModelu(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.init);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new ManagerModelu(Id.managerModelu, mySim(), this);
        addOwnMessage(Mc.obsluhaZakaznika);
        addOwnMessage(Mc.prichodZakaznika);
    }
    //meta! tag="end"

    public void spustiSimulaciu() {
        MyMessage message = new MyMessage(mySim(), null);
        message.setCode(Mc.init);
        message.setAddressee(this);
        manager().notice(message);
    }
}