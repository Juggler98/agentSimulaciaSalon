package simulation;

import OSPABA.*;
import agents.*;
import entities.pracovnik.Pracovnik;
import entities.zakaznik.Zakaznik;

import java.util.ArrayList;
import java.util.Arrays;

public class MySimulation extends Simulation {

    private final int[] statsVykonov = new int[10];
    private final double[] statsAllVykonov = new double[10];
    private final String[] statsNames = {"Zadané účesy", "Spravené účesy", "Zadané Líčenia", "Spravené Líčenia", "Zadané účesy aj líčenia", "Spravené účesy aj líčenia", "Zadané čistenia", "Spravené čistenia", "Zadané objednávky", "Dokončené objednávky", "Čas na objednávku", "Čas v sálone", "Čas účesu", "Po zatvorení ešte"};

    private final double[] casy = new double[5]; //casStravenyVSalone, dlzkaCakaniaNaObjednavku, robenieUcesov, poOtvaracejDobe
    private final double[] celkoveCasy = new double[5];

    private final double[] dlzkyRadov = new double[3]; //recepcia, ucesy, licenie
    private final double[] celkoveDlzkyRadov = new double[3];

    private final double[] xI = new double[2];
    private double xAvg = 0;

    private int pocetObsluhovanychRecepcia = 0;

    private final ArrayList<Pracovnik> zamestnanci = new ArrayList<>();
    private final ArrayList<Zakaznik> zakaznici = new ArrayList<>();

    public final int pocetRecepcnych;
    public final int pocetKadernicok;
    public final int pocetKozmeticiek;

    public MySimulation(int pocetRecepcnych, int pocetKadernicok, int pocetKozmeticiek) {
        this.pocetRecepcnych = pocetRecepcnych;
        this.pocetKadernicok = pocetKadernicok;
        this.pocetKozmeticiek = pocetKozmeticiek;
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

        agentRecepcie().inicializuj(pocetRecepcnych);
        agentUcesov().inicializuj(pocetKadernicok);
        agentLicenia().inicializuj(pocetKozmeticiek);

        Zakaznik.pocetZakaznikov = 0;

        Arrays.fill(statsVykonov, 0);
        Arrays.fill(casy, 0);
        Arrays.fill(dlzkyRadov, 0);

        xAvg = 0;

        pocetObsluhovanychRecepcia = 0;

        zamestnanci.clear();
        zakaznici.clear();
        for (int i = 0; i < pocetRecepcnych; i++) {
            zamestnanci.add(agentRecepcie().getZamestnanec(i));
        }
        for (int i = 0; i < pocetKadernicok; i++) {
            zamestnanci.add(agentUcesov().getZamestnanec(i));
        }
        for (int i = 0; i < pocetKozmeticiek; i++) {
            zamestnanci.add(agentLicenia().getZamestnanec(i));
        }

        agentModelu().spustiSimulaciu();
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...
        celkoveCasy[0] += casy[0] / statsVykonov[9];
        celkoveCasy[1] += casy[1] / statsVykonov[9];
        celkoveCasy[2] += casy[2] / (statsVykonov[0] + statsVykonov[4]);
        celkoveCasy[3] += currentTime() - Config.endTime;

        celkoveDlzkyRadov[0] += dlzkyRadov[0] / (Config.endTime + 1);
        celkoveDlzkyRadov[1] += dlzkyRadov[1] / (Config.endTime + 1);
        celkoveDlzkyRadov[2] += dlzkyRadov[2] / (Config.endTime + 1);

        for (int i = 0; i < statsVykonov.length; i++) {
            statsAllVykonov[i] += statsVykonov[i];
        }

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
    private void init() {
        setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
        setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu()));
        setAgentSalonu(new AgentSalonu(Id.agentSalonu, this, agentModelu()));
        setAgentRecepcie(new AgentRecepcie(Id.agentRecepcie, this, agentSalonu()));
        setAgentUcesov(new AgentUcesov(Id.agentUcesov, this, agentSalonu()));
        setAgentLicenia(new AgentLicenia(Id.agentLicenia, this, agentSalonu()));
    }

    private AgentModelu _agentModelu;

    public AgentModelu agentModelu() {
        return _agentModelu;
    }

    public void setAgentModelu(AgentModelu agentModelu) {
        _agentModelu = agentModelu;
    }

    private AgentOkolia _agentOkolia;

    public AgentOkolia agentOkolia() {
        return _agentOkolia;
    }

    public void setAgentOkolia(AgentOkolia agentOkolia) {
        _agentOkolia = agentOkolia;
    }

    private AgentSalonu _agentSalonu;

    public AgentSalonu agentSalonu() {
        return _agentSalonu;
    }

    public void setAgentSalonu(AgentSalonu agentSalonu) {
        _agentSalonu = agentSalonu;
    }

    private AgentRecepcie _agentRecepcie;

    public AgentRecepcie agentRecepcie() {
        return _agentRecepcie;
    }

    public void setAgentRecepcie(AgentRecepcie agentRecepcie) {
        _agentRecepcie = agentRecepcie;
    }

    private AgentUcesov _agentUcesov;

    public AgentUcesov agentUcesov() {
        return _agentUcesov;
    }

    public void setAgentUcesov(AgentUcesov agentUcesov) {
        _agentUcesov = agentUcesov;
    }

    private AgentLicenia _agentLicenia;

    public AgentLicenia agentLicenia() {
        return _agentLicenia;
    }

    public void setAgentLicenia(AgentLicenia agentLicenia) {
        _agentLicenia = agentLicenia;
    }
    //meta! tag="end"

    public int getDlzkaRaduUcesyLicenie() {
        return agentLicenia().getRadSize() + agentUcesov().getRadSize();
    }

    public int getPocetObsluhovanychRecepcia() {
        return pocetObsluhovanychRecepcia;
    }

    public void incPocetObsluhovanychRecepcia(int dlzka) {
        this.pocetObsluhovanychRecepcia += dlzka;
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

    public void addCas(int index, double cas) {
        casy[index] += cas;
    }

    public void addDlzkaRadu(int index, double dlzkaRadu) {
        dlzkyRadov[index] += dlzkaRadu;
    }

    public double[] getDlzkyRadov() {
        return dlzkyRadov;
    }

    public double[] getCelkoveDlzkyRadov() {
        return celkoveDlzkyRadov;
    }

    public double[] getCasy() {
        return casy;
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