package managers;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
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
		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
		if (zakaznik.getMiesto() != null) {
			message.setAddressee(myAgent().findAssistant(Id.procesChodze));
		} else {
			message.setAddressee(myAgent().findAssistant(Id.procesJazdy));
		}
		startContinualAssistant(message);
    }

	//meta! sender="AgentSalonu", id="62", type="Request"
	public void processParkovanie(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        if (zakaznik.odchadza()) {
            message.setAddressee(myAgent().findAssistant(Id.procesChodze));
        } else {
            message.setAddressee(myAgent().findAssistant(Id.procesJazdy));
        }
        startContinualAssistant(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="ProcesJazdy", id="104", type="Finish"
	public void processFinishProcesJazdy(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
		if (zakaznik.odchadza()) {
			message.setCode(Mc.parkovanie);
			response(message);
		} else {
			message.setAddressee(myAgent().findAssistant(Id.procesParkovania));
			startContinualAssistant(message);
		}

	}

	//meta! sender="ProcesChodze", id="100", type="Finish"
	public void processFinishProcesChodze(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
		if (zakaznik.odchadza()) {
            message.setAddressee(myAgent().findAssistant(Id.procesParkovania));
            startContinualAssistant(message);
		} else {
			message.setCode(Mc.parkovanie);
            response(message);
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
			switch (message.sender().id())
			{
			case Id.procesJazdy:
				processFinishProcesJazdy(message);
			break;

			case Id.procesParkovania:
				processFinishProcesParkovania(message);
			break;

			case Id.procesChodze:
				processFinishProcesChodze(message);
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