package managers;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.TypZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;

//meta! id="36"
public class ManagerSalonu extends Manager {
    public ManagerSalonu(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="AgentModelu", id="37", type="Request"
    public void processObsluhaZakaznika(MessageForm message) {
        message.setCode(Mc.obsluhaRecepia);
        message.setAddressee(mySim().findAgent(Id.agentRecepcie));
        message.setSender(myAgent());
        request(message);
    }

    //meta! sender="AgentRecepcie", id="17", type="Response"
    public void processObsluhaRecepia(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        message.setSender(myAgent());
        if (zakaznik.isObsluzeny()) {
            message.setCode(Mc.obsluhaZakaznika);
            message.setAddressee(mySim().findAgent(Id.agentModelu));
            response(message);
        } else {
            if (zakaznik.getTypZakaznika() == TypZakaznika.LICENIE) {
                message.setCode(Mc.obsluhaLicenie);
                message.setAddressee(mySim().findAgent(Id.agentLicenia));
            } else {
                message.setCode(Mc.obsluhaUcesy);
                message.setAddressee(mySim().findAgent(Id.agentUcesov));
            }
            request(message);
        }
    }

    //meta! sender="AgentUcesov", id="18", type="Response"
    public void processObsluhaUcesy(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        message.setSender(myAgent());
        if (zakaznik.getTypZakaznika() == TypZakaznika.UCESAJLICENIE) {
            message.setCode(Mc.obsluhaLicenie);
            message.setAddressee(mySim().findAgent(Id.agentLicenia));
        } else {
            zakaznik.setObsluzeny();
            message.setCode(Mc.obsluhaRecepia);
            message.setAddressee(mySim().findAgent(Id.agentRecepcie));
        }
        request(message);
    }

    //meta! sender="AgentLicenia", id="19", type="Response"
    public void processObsluhaLicenie(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        message.setSender(myAgent());
        if (zakaznik.isGoToHlbkoveLicenie()) {
            message.setCode(Mc.obsluhaLicenie);
            message.setAddressee(mySim().findAgent(Id.agentLicenia));
            zakaznik.setGoToHlbkoveLicenie(false);
        } else {
            zakaznik.setObsluzeny();
            message.setCode(Mc.obsluhaRecepia);
            message.setAddressee(mySim().findAgent(Id.agentRecepcie));
        }
        request(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.obsluhaUcesy:
                processObsluhaUcesy(message);
                break;

            case Mc.obsluhaRecepia:
                processObsluhaRecepia(message);
                break;

            case Mc.obsluhaLicenie:
                processObsluhaLicenie(message);
                break;

            case Mc.obsluhaZakaznika:
                processObsluhaZakaznika(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentSalonu myAgent() {
        return (AgentSalonu) super.myAgent();
    }

}
