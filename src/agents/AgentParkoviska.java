package agents;

import OSPABA.*;
import entities.pracovnik.Miesto;
import entities.zakaznik.Zakaznik;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="57"
public class AgentParkoviska extends Agent {


    public static final double toA = 13.0;
    public static final double toB = 10.0;
    public static final double toC = 8.0;
    public static final double width = 35.0;
    public static final double parkingSize = width / Config.miestRadu;

    protected Miesto[][] parkovisko;

    public AgentParkoviska(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        parkovisko = new Miesto[((MySimulation) mySim()).getPocetRadov()][Config.miestRadu];
        init();
        addOwnMessage(Mc.parkuj);
        addOwnMessage(Mc.koniecJazdy);
        addOwnMessage(Mc.koniecChodze);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        for (int i = 0; i < parkovisko.length; i++) {
            for (int j = 0; j < parkovisko[0].length; j++) {
                parkovisko[i][j] = new Miesto(i, j);
            }
        }
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerParkoviska(Id.managerParkoviska, mySim(), this);
		new ProcesChodze(Id.procesChodze, mySim(), this);
		new ProcesJazdy(Id.procesJazdy, mySim(), this);
		new ProcesParkovania(Id.procesParkovania, mySim(), this);
		addOwnMessage(Mc.parkovanie);
	}
	//meta! tag="end"


    public Miesto[][] parkovisko() {
        return parkovisko;
    }
}