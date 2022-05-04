package agents;

import OSPABA.*;
import entities.pracovnik.TypPracovnika;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="9"
public class AgentUcesov extends AgentPracovnika {
    public AgentUcesov(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent, TypPracovnika.UCES);
        init();
        addOwnMessage(Mc.koniecObsluhyUcesy);
        setProces((ContinualAssistant) findAssistant(Id.procesObsluhyUcesy));
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();

        // Setup component for the next replication
        inicializuj(((MySimulation) mySim()).pocetKadernicok);
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerUcesov(Id.managerUcesov, mySim(), this);
		new ProcesObsluhyUcesy(Id.procesObsluhyUcesy, mySim(), this);
		addOwnMessage(Mc.obsluhaUcesy);
	}
	//meta! tag="end"

    @Override
    public int getPocetPracovnikov() {
        return ((MySimulation) mySim()).pocetKadernicok;
    }
}