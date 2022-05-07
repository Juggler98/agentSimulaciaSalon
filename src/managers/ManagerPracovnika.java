package managers;

import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import agents.AgentLicenia;
import agents.AgentPracovnika;
import agents.AgentRecepcie;
import agents.AgentUcesov;
import entities.Pracovnik;
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
        if (agent.isRadEmpty() && agent.jeNiektoVolny() && this.checkRad(agent, mySimulation)) {
            Pracovnik novyPracovnik = agent.obsadZamestnanca();

            novyPracovnik.setObsluhujeZakaznika(zakaznik.getPoradie());
            novyPracovnik.setObsluhuje(true);
            novyPracovnik.setZaciatokObsluhy(mySim().currentTime());

            ((MyMessage) message).setZamestnanec(novyPracovnik);

            message.setAddressee(agent.getProces());
            startContinualAssistant(message);
        } else {
            if (agent instanceof AgentUcesov) {
                zakaznik.setStavZakaznika(StavZakaznika.RADUCES);
            } else if (agent instanceof AgentRecepcie) {
                zakaznik.setStavZakaznika(StavZakaznika.RADRECEPCIA);
            } else if (agent instanceof AgentLicenia) {
                zakaznik.setStavZakaznika(StavZakaznika.RADLICENIE);
            } else {
                throw new IllegalStateException("This should not happened");
            }
            if (mySim().currentTime() <= Config.endTime) {
                agent.getDlzkaRaduStat().addValue(mySim().currentTime(), agent.getRadSize());
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
        if (!agent.isRadEmpty() && agent.jeNiektoVolny() && this.checkRad(agent, mySimulation)) {
            Zakaznik novyZakaznik = agent.vyberZRadu();
            Pracovnik novyPracovnik = agent.obsadZamestnanca();

            novyPracovnik.setObsluhujeZakaznika(novyZakaznik.getPoradie());
            novyPracovnik.setObsluhuje(true);
            novyPracovnik.setZaciatokObsluhy(mySim().currentTime());

            MyMessage msgCopy = new MyMessage((MyMessage) message);

            msgCopy.setZamestnanec(novyPracovnik);
            msgCopy.setZakaznik(novyZakaznik);

            if (mySim().currentTime() <= Config.endTime) {
                agent.getDlzkaRaduStat().addValue(mySim().currentTime(), agent.getRadSize() + 1);
            }

            msgCopy.setAddressee(agent.getProces());
            startContinualAssistant(msgCopy);
        }
    }

    private boolean checkRad(AgentPracovnika agent, MySimulation simulation) {
        //Dovoli obsluhu len ak nezacina obsluha na recepcii, alebo nie je plna prevadzka alebo ide niekto platit
        return !(agent instanceof AgentRecepcie) || simulation.getDlzkaRaduUcesyLicenie() + ((AgentRecepcie) agent).getPocetObsluhovanychRecepcia() <= 10 || agent.ideNiektoPlatit();
    }

}
