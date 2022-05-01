package managers;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;

//meta! id="2"
public class ManagerOkolia extends Manager {
    public ManagerOkolia(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentModelu", id="4", type="Notice"
	public void processInit(MessageForm message) {
        message.setAddressee(myAgent().findAssistant(Id.planovacPrichodyZakaznika));
        startContinualAssistant(message);
    }

	//meta! sender="PlanovacPrichodyZakaznika", id="23", type="Finish"
	public void processFinish(MessageForm message) {
        message.setCode(Mc.prichodZakaznika);
        message.setAddressee(mySim().findAgent(Id.agentModelu));
        notice(message);
    }

	//meta! sender="AgentModelu", id="6", type="Notice"
	public void processOdchodZakaznika(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        zakaznik.setCasOdchodu(mySim().currentTime());
        zakaznik.setStavZakaznika(StavZakaznika.ODCHOD);
        ((MySimulation) mySim()).addCas(0, zakaznik.getCasOdchodu() - zakaznik.getCasPrichodu());
        ((MySimulation) mySim()).addXAvg(zakaznik.getCasOdchodu() - zakaznik.getCasPrichodu());
        ((MySimulation) mySim()).getStatsVykonov()[9]++;
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
		case Mc.odchodZakaznika:
			processOdchodZakaznika(message);
		break;

		case Mc.init:
			processInit(message);
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
    public AgentOkolia myAgent() {
        return (AgentOkolia) super.myAgent();
    }

}