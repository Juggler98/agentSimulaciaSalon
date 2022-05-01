package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="1"
public class ManagerModelu extends Manager {
    public ManagerModelu(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentOkolia", id="5", type="Notice"
	public void processPrichodZakaznika(MessageForm message) {
        message.setCode(Mc.obsluhaZakaznika);
        message.setAddressee(mySim().findAgent(Id.agentSalonu));
        request(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.init:
                message.setAddressee(mySim().findAgent(Id.agentOkolia));
                notice(message);
                break;
        }
    }

	//meta! sender="AgentSalonu", id="37", type="Response"
	public void processObsluhaZakaznika(MessageForm message) {
        message.setCode(Mc.odchodZakaznika);
        message.setAddressee(mySim().findAgent(Id.agentOkolia));
        message.setSender(myAgent());
        notice(message);
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.prichodZakaznika:
			processPrichodZakaznika(message);
		break;

		case Mc.obsluhaZakaznika:
			processObsluhaZakaznika(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentModelu myAgent() {
        return (AgentModelu) super.myAgent();
    }

}