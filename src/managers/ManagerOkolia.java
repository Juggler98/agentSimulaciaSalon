package managers;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;
import sun.plugin2.message.Message;

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

        MyMessage msgCopy = new MyMessage((MyMessage) message);
        msgCopy.setAddressee(myAgent().findAssistant(Id.planovacUzavretia));
        startContinualAssistant(msgCopy);
    }

    //meta! sender="PlanovacPrichodyZakaznika", id="23", type="Finish"
    public void processFinishPlanovacPrichodyZakaznika(MessageForm message) {
        message.setCode(Mc.prichodZakaznika);
        message.setAddressee(mySim().findAgent(Id.agentModelu));
        notice(message);
    }

    //meta! sender="AgentModelu", id="6", type="Notice"
    public void processOdchodZakaznika(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        MySimulation mySimulation = ((MySimulation) mySim());
        zakaznik.setCasOdchodu(mySim().currentTime());
        zakaznik.setStavZakaznika(StavZakaznika.ODCHOD);
        mySimulation.addCas(0, zakaznik.getCasOdchodu() - zakaznik.getCasPrichodu());
        mySimulation.addXAvg(zakaznik.getCasOdchodu() - zakaznik.getCasPrichodu());
        mySimulation.getStatsVykonov()[9]++;

        if (zakaznik.isAutom()) {
            mySimulation.getFreeSlots().add(zakaznik.getZaparkovane());
        }

        // Ak je po zaverecnej a vsetci su obsluzeny zastavuje replikaciu
        if (mySim().currentTime() > Config.endTime && ((MySimulation) mySim()).getStatsVykonov()[8] == ((MySimulation) mySim()).getStatsVykonov()[9]) {
            mySim().stopReplication();
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! sender="PlanovacUzavretia", id="51", type="Finish"
    public void processFinishPlanovacUzavretia(MessageForm message) {
        message.setCode(Mc.uzavri);
        message.setAddressee(mySim().findAgent(Id.agentModelu));
        notice(message);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.planovacUzavretia:
                        processFinishPlanovacUzavretia(message);
                        break;

                    case Id.planovacPrichodyZakaznika:
                        processFinishPlanovacPrichodyZakaznika(message);
                        break;
                }
                break;

            case Mc.odchodZakaznika:
                processOdchodZakaznika(message);
                break;

            case Mc.init:
                processInit(message);
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