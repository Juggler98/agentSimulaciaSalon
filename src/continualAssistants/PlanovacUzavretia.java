package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="50"
public class PlanovacUzavretia extends Scheduler {
    public PlanovacUzavretia(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

	//meta! sender="AgentOkolia", id="51", type="Start"
	public void processStart(MessageForm message) {
        message.setCode(Mc.zatvorenie);
        hold(Config.endTime, message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.zatvorenie:
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