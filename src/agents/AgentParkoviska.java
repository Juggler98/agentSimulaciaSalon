package agents;

import OSPABA.*;
import entities.Miesto;
import simulation.*;
import managers.*;
import continualAssistants.*;
import stats.DiscreteStat;
import stats.WeightStat;

//meta! id="57"
public class AgentParkoviska extends Agent {

    public static final double toA = 13.0;
    public static final double toB = 10.0;
    public static final double toC = 8.0;
    public static final double width = 35.0;
    public static final double parkingSize = width / Config.miestRadu;

    protected Miesto[][] parkovisko;

    private final DiscreteStat autom = new DiscreteStat();
    private final DiscreteStat zaparkovalo = new DiscreteStat();
    private final DiscreteStat spokojnost = new DiscreteStat();
    private final WeightStat obsadenostStat = new WeightStat();

    public AgentParkoviska(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        parkovisko = new Miesto[((MySimulation) mySim()).properties().getPocetRadov()][Config.miestRadu];
        init();
        addOwnMessage(Mc.parkuj);
        addOwnMessage(Mc.koniecJazdy);
        addOwnMessage(Mc.koniecChodze);
        addOwnMessage(Mc.koniecRampy);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        autom.init();
        zaparkovalo.init();
        spokojnost.init();
        obsadenostStat.init();

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
		new ProcesRampy(Id.procesRampy, mySim(), this);
		addOwnMessage(Mc.parkovanie);
	}
	//meta! tag="end"


    public Miesto[][] parkovisko() {
        return parkovisko;
    }

    public DiscreteStat getAutom() {
        return autom;
    }

    public DiscreteStat getZaparkovalo() {
        return zaparkovalo;
    }

    public DiscreteStat getSpokojnost() {
        return spokojnost;
    }

    public WeightStat getObsadenostStat() {
        return obsadenostStat;
    }

    public void obsadenostChange(int start) {
        double pocetObsadenych = start + obsadene();
        obsadenostStat.addValue(mySim().currentTime(), pocetObsadenych / kapacita());
    }

    public double getAktualObsadenost() {
        return obsadene() / kapacita();
    }

    private double obsadene() {
        int pocetObsadenych = 0;
        for (int i = 0; i < parkovisko.length; i++) {
            for (int j = 0; j < parkovisko[0].length; j++) {
                if (parkovisko[i][j].getZakaznik() != null) {
                    pocetObsadenych++;
                }
            }
        }
        return 1.0 * pocetObsadenych;
    }

    private double kapacita() {
        return parkovisko.length * parkovisko[0].length;
    }
}