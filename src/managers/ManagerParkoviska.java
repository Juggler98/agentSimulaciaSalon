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
	public void processFinishProcesParkovania(MessageForm message) {
        ((MySimulation) mySim()).getStatsVykonov()[11]++;
        message.setCode(Mc.parkovanie);
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

	//meta! sender="ProcesObchodzky", id="104", type="Finish"
	public void processFinishProcesObchodzky(MessageForm message)
	{
	}

	//meta! sender="ProcesOdchodu", id="98", type="Finish"
	public void processFinishProcesOdchodu(MessageForm message)
	{
	}

	//meta! sender="ProcesChodze", id="100", type="Finish"
	public void processFinishProcesChodze(MessageForm message)
	{
	}

	//meta! sender="ProcesPrichodu", id="96", type="Finish"
	public void processFinishProcesPrichodu(MessageForm message)
	{
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
			switch (message.sender().id())
			{
			case Id.procesObchodzky:
				processFinishProcesObchodzky(message);
			break;

			case Id.procesParkovania:
				processFinishProcesParkovania(message);
			break;

			case Id.procesOdchodu:
				processFinishProcesOdchodu(message);
			break;

			case Id.procesChodze:
				processFinishProcesChodze(message);
			break;

			case Id.procesPrichodu:
				processFinishProcesPrichodu(message);
			break;
			}
		break;

		case Mc.parkovanie:
			processParkovanie(message);
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