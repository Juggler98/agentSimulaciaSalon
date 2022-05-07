package simulation;

import OSPABA.*;
import agents.*;
import entities.Pracovnik;
import entities.zakaznik.Zakaznik;

import java.util.ArrayList;
import java.util.Arrays;

public class MySimulation extends Simulation {

    private final double[] statsAllVykonov = new double[14]; //10 = autom 11 = zaparkovalo 12 = spokojnost 13 = obsadenost
    private final int[] statsVykonov = new int[10];
    private final String[] statsNames = {"Zadané účesy", "Spravené účesy", "Zadané Líčenia", "Spravené Líčenia", "Zadané účesy aj líčenia", "Spravené účesy aj líčenia", "Zadané čistenia", "Spravené čistenia", "Zadané objednávky", "Dokončené objednávky", "Čas na objednávku", "Čas v sálone", "Čas účesu", "Po zatvorení ešte", "Autom", "Zaparkovalo", "Zaparkovalo %", "Spokojnost", "Obsadenost %"};
    private final double[] celkoveCasy = new double[4];  //casStravenyVSalone, dlzkaCakaniaNaObjednavku, robenieUcesov, poOtvaracejDobe
    private final double[] celkoveDlzkyRadov = new double[3]; //recepcia, ucesy, licenie

    private final double[] xI = new double[2];
    private double xAvg = 0;

    private final ArrayList<Pracovnik> zamestnanci = new ArrayList<>();
    private final ArrayList<Zakaznik> zakaznici = new ArrayList<>();

    Properties properties;

    public MySimulation(Properties properties) {
        this.properties = properties;
        init();
    }

    @Override
    public void prepareSimulation() {
        super.prepareSimulation();
        // Create global statistcis
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...

        Zakaznik.init();
        Arrays.fill(statsVykonov, 0);
        xAvg = 0;

        zamestnanci.clear();
        zakaznici.clear();

        for (int i = 0; i < properties.getPocetRecepcnych(); i++) {
            zamestnanci.add(agentRecepcie().getZamestnanec(i));
        }
        for (int i = 0; i < properties.getPocetKadernicok(); i++) {
            zamestnanci.add(agentUcesov().getZamestnanec(i));
        }
        for (int i = 0; i < properties.getPocetKozmeticiek(); i++) {
            zamestnanci.add(agentLicenia().getZamestnanec(i));
        }

        agentModelu().spustiSimulaciu();
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        celkoveCasy[0] += agentOkolia().getCasVSalone() / statsVykonov[9];
        celkoveCasy[1] += agentRecepcie().getCasObjednavky() / statsVykonov[9];
        celkoveCasy[2] += agentUcesov().getCasUcesov() / (statsVykonov[0] + statsVykonov[4]);
        celkoveCasy[3] += currentTime() - Config.endTime;

        celkoveDlzkyRadov[0] += agentRecepcie().getDlzkaRaduStat().getValue();
        celkoveDlzkyRadov[1] += agentUcesov().getDlzkaRaduStat().getValue();
        celkoveDlzkyRadov[2] += agentLicenia().getDlzkaRaduStat().getValue();

        for (int i = 0; i < statsVykonov.length; i++) {
            statsAllVykonov[i] += statsVykonov[i];
        }

        statsAllVykonov[10] += agentParkoviska().getAutom().getValue();
        statsAllVykonov[11] += agentParkoviska().getZaparkovalo().getValue();
        statsAllVykonov[12] += agentParkoviska().getSpokojnost().getValue();
        statsAllVykonov[13] += agentParkoviska().getObsadenostStat().getValue();

        xI[0] += Math.pow(xAvg / statsVykonov[9], 2);
        xI[1] += xAvg / statsVykonov[9];

        super.replicationFinished();
    }

    @Override
    public void simulationFinished() {
        // Dysplay simulation results
        for (Pracovnik zamestnanec : zamestnanci) {
            zamestnanec.setVyuzitie(zamestnanec.getOdpracovanyCas() / currentTime());
        }
        super.simulationFinished();
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
		setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu()));
		setAgentSalonu(new AgentSalonu(Id.agentSalonu, this, agentModelu()));
		setAgentRecepcie(new AgentRecepcie(Id.agentRecepcie, this, agentSalonu()));
		setAgentUcesov(new AgentUcesov(Id.agentUcesov, this, agentSalonu()));
		setAgentLicenia(new AgentLicenia(Id.agentLicenia, this, agentSalonu()));
		setAgentParkoviska(new AgentParkoviska(Id.agentParkoviska, this, agentSalonu()));
	}

	private AgentModelu _agentModelu;

public AgentModelu agentModelu()
	{ return _agentModelu; }

	public void setAgentModelu(AgentModelu agentModelu)
	{_agentModelu = agentModelu; }

	private AgentOkolia _agentOkolia;

public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public void setAgentOkolia(AgentOkolia agentOkolia)
	{_agentOkolia = agentOkolia; }

	private AgentSalonu _agentSalonu;

public AgentSalonu agentSalonu()
	{ return _agentSalonu; }

	public void setAgentSalonu(AgentSalonu agentSalonu)
	{_agentSalonu = agentSalonu; }

	private AgentRecepcie _agentRecepcie;

public AgentRecepcie agentRecepcie()
	{ return _agentRecepcie; }

	public void setAgentRecepcie(AgentRecepcie agentRecepcie)
	{_agentRecepcie = agentRecepcie; }

	private AgentUcesov _agentUcesov;

public AgentUcesov agentUcesov()
	{ return _agentUcesov; }

	public void setAgentUcesov(AgentUcesov agentUcesov)
	{_agentUcesov = agentUcesov; }

	private AgentLicenia _agentLicenia;

public AgentLicenia agentLicenia()
	{ return _agentLicenia; }

	public void setAgentLicenia(AgentLicenia agentLicenia)
	{_agentLicenia = agentLicenia; }

	private AgentParkoviska _agentParkoviska;

public AgentParkoviska agentParkoviska()
	{ return _agentParkoviska; }

	public void setAgentParkoviska(AgentParkoviska agentParkoviska)
	{_agentParkoviska = agentParkoviska; }
	//meta! tag="end"


    public Properties properties() {
        return properties;
    }

    public int getDlzkaRaduUcesyLicenie() {
        return agentLicenia().getRadSize() + agentUcesov().getRadSize();
    }

    public String[] getStatsNames() {
        return statsNames;
    }

    public double[] getStatsAllVykonov() {
        return statsAllVykonov;
    }

    public int[] getStatsVykonov() {
        return statsVykonov;
    }

    public double[] getCelkoveDlzkyRadov() {
        return celkoveDlzkyRadov;
    }

    public double[] getCelkoveCasy() {
        return celkoveCasy;
    }

    public double[] getxI() {
        return xI;
    }

    public void addXAvg(double xAvg) {
        this.xAvg += xAvg;
    }

    public ArrayList<Pracovnik> getZamestnanci() {
        return zamestnanci;
    }

    public ArrayList<Zakaznik> getZakaznici() {
        return zakaznici;
    }
}