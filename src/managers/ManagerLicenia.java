package managers;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="10"
public class ManagerLicenia extends ManagerPracovnika {
    public ManagerLicenia(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="ProcesObsluhyLicenie", id="32", type="Finish"
	public void processFinish(MessageForm message) {
        ukonciObsluhu(message);

        message.setCode(Mc.obsluhaLicenie);
        response(message);
    }

	//meta! sender="AgentSalonu", id="19", type="Request"
	public void processObsluhaLicenie(MessageForm message) {
        zacniObsluhu(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.obsluhaLicenie:
			processObsluhaLicenie(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentLicenia myAgent() {
        return (AgentLicenia) super.myAgent();
    }

}