package managers;

import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.AgentPracovnika;
import agents.AgentRecepcie;
import entities.pracovnik.Pracovnik;
import entities.pracovnik.TypPracovnika;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.Config;
import simulation.MyMessage;
import simulation.MySimulation;

public abstract class ManagerPracovnika extends Manager {

    public ManagerPracovnika(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void processMessage(MessageForm messageForm) {

    }

    protected void zacniObsluhu(MessageForm message) {
        AgentPracovnika agent = (AgentPracovnika) myAgent();
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        // Ak je rad prazdny a je volny zamestnanec zacne sa obsluha, inak ide zakaznik do radu
        MySimulation mySimulation = ((MySimulation) mySim());
        if (agent.isRadEmpty() && agent.jeNiektoVolny() && ((agent.getTypPracovnika() != TypPracovnika.RECEPCIA) || (mySimulation.getDlzkaRaduUcesyLicenie() + ((AgentRecepcie) agent).getPocetObsluhovanychRecepcia() <= 10 || agent.ideNiektoPlatit()))) {
            Pracovnik novyPracovnik = agent.obsadZamestnanca();

            novyPracovnik.setObsluhujeZakaznika(zakaznik.getPoradie());
            novyPracovnik.setObsluhuje(true);
            novyPracovnik.setZaciatokObsluhy(mySim().currentTime());

            ((MyMessage) message).setZamestnanec(novyPracovnik);

            message.setAddressee(agent.getProces());
            startContinualAssistant(message);
        } else {
            int radIndex = 0;
            switch (agent.getTypPracovnika()) {
                case UCES:
                    radIndex = 1;
                    zakaznik.setStavZakaznika(StavZakaznika.RADUCES);
                    break;
                case LICENIE:
                    radIndex = 2;
                    zakaznik.setStavZakaznika(StavZakaznika.RADLICENIE);
                    break;
                case RECEPCIA:
                    radIndex = 0;
                    zakaznik.setStavZakaznika(StavZakaznika.RADRECEPCIA);
                    break;
            }
            if (mySim().currentTime() <= Config.endTime) {
                ((MySimulation) mySim()).addDlzkaRadu(radIndex, agent.getRadSize() * (mySim().currentTime() - agent.getLastRadChange()));
                agent.setLastRadChange(mySim().currentTime());
            }
            agent.pridajDoRadu(zakaznik);
        }
    }

    protected void ukonciObsluhu(MessageForm message) {
        AgentPracovnika agent = (AgentPracovnika) myAgent();

        Pracovnik pracovnik = ((MyMessage) message).getZamestnanec();
        pracovnik.setObsluhuje(false);
        pracovnik.addOdpracovanyCas(mySim().currentTime() - pracovnik.getZaciatokObsluhy());
        pracovnik.setVyuzitie(pracovnik.getOdpracovanyCas() / mySim().currentTime());
        agent.uvolniZamestnanca(pracovnik);

        obsluzDalsieho(message);
    }

    protected void obsluzDalsieho(MessageForm message) {
        AgentPracovnika agent = (AgentPracovnika) myAgent();
        // Ak je niekto v rade a je volny pracovnik spusti obsluhu dalsieho zakaznika
        MySimulation mySimulation = ((MySimulation) mySim());
        if (!agent.isRadEmpty() && agent.jeNiektoVolny() && ((agent.getTypPracovnika() != TypPracovnika.RECEPCIA) || (mySimulation.getDlzkaRaduUcesyLicenie() + ((AgentRecepcie) agent).getPocetObsluhovanychRecepcia() <= 10 || agent.ideNiektoPlatit()))) {
            Zakaznik novyZakaznik = agent.vyberZRadu();
            Pracovnik novyPracovnik = agent.obsadZamestnanca();

            novyPracovnik.setObsluhujeZakaznika(novyZakaznik.getPoradie());
            novyPracovnik.setObsluhuje(true);
            novyPracovnik.setZaciatokObsluhy(mySim().currentTime());

            MyMessage msgCopy = new MyMessage((MyMessage) message);

            msgCopy.setZamestnanec(novyPracovnik);
            msgCopy.setZakaznik(novyZakaznik);

            int radIndex = 0;
            switch (agent.getTypPracovnika()) {
                case UCES:
                    radIndex = 1;
                    break;
                case LICENIE:
                    radIndex = 2;
                    break;
                case RECEPCIA:
                    radIndex = 0;
                    break;
            }

            if (mySim().currentTime() <= Config.endTime) {
                mySimulation.addDlzkaRadu(radIndex, (agent.getRadSize() + 1) * (mySim().currentTime() - agent.getLastRadChange()));
                agent.setLastRadChange(mySim().currentTime());
            }

            msgCopy.setAddressee(agent.getProces());
            startContinualAssistant(msgCopy);
        }
    }

}
