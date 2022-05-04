package agents;

import OSPABA.*;
import entities.pracovnik.Miesto;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="57"
public class AgentParkoviska extends Agent {

    protected Miesto[][] parkovisko;

    public AgentParkoviska(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
        addOwnMessage(Mc.koniecParkovania);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        parkovisko = new Miesto[((MySimulation) mySim()).getPocetRadov()][Config.miestRadu];
        for (int i = 0; i < parkovisko.length; i++) {
            for (int j = 0; j < parkovisko[0].length; j++) {
                parkovisko[i][j] = new Miesto(i + 1, j + 1);
            }
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerParkoviska(Id.managerParkoviska, mySim(), this);
		new ProcesChodze(Id.procesChodze, mySim(), this);
		new ProcesObchodzky(Id.procesObchodzky, mySim(), this);
		new ProcesParkovania(Id.procesParkovania, mySim(), this);
		new ProcesOdchodu(Id.procesOdchodu, mySim(), this);
		new ProcesPrichodu(Id.procesPrichodu, mySim(), this);
		addOwnMessage(Mc.parkovanie);
	}
	//meta! tag="end"


    public Miesto[][] parkovisko() {
        return parkovisko;
    }
}