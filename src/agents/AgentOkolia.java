package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="2"
public class AgentOkolia extends Agent {

    double casVSalone = 0;

    public AgentOkolia(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.novyZakaznik);
        addOwnMessage(Mc.zatvorenie);
        addOwnMessage(Mc.novyZakaznikAutom);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
        casVSalone = 0;
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerOkolia(Id.managerOkolia, mySim(), this);
		new PlanovacUzavretia(Id.planovacUzavretia, mySim(), this);
		new PlanovacPrichodyZakaznika(Id.planovacPrichodyZakaznika, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.odchodZakaznika);
	}
	//meta! tag="end"

    public void addCasVSalone(double cas) {
        casVSalone += cas;
    }

    public double getCasVSalone() {
        return casVSalone;
    }
}