package managers;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.TypZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;

import java.util.Random;

//meta! id="36"
public class ManagerSalonu extends Manager {

    private static final Random seedGenerator = new Random();
    private static final Random randPercentageTypZakaznika = new Random(seedGenerator.nextLong());
    private static final Random randPercentageCiseniePleti = new Random(seedGenerator.nextLong());

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
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        message.setSender(myAgent());
        if (zakaznik.isAutom()) {
            message.setCode(Mc.parkovanie);
            message.setAddressee(mySim().findAgent(Id.agentParkoviska));
        } else {
            message.setCode(Mc.obsluhaRecepia);
            message.setAddressee(mySim().findAgent(Id.agentRecepcie));
        }
        request(message);
    }

	//meta! sender="AgentRecepcie", id="17", type="Response"
	public void processObsluhaRecepia(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        message.setSender(myAgent());
        if (zakaznik.isObsluzeny() && zakaznik.isAutom()) {
            zakaznik.setOdchadza();
            message.setCode(Mc.parkovanie);
            message.setAddressee(mySim().findAgent(Id.agentParkoviska));
            request(message);
        } else if (zakaznik.isObsluzeny()) {
            message.setCode(Mc.obsluhaZakaznika);
            response(message);
        } else {
            double percentage = randPercentageTypZakaznika.nextDouble();
            if (percentage < 0.2) {
                zakaznik.setTypZakaznika(TypZakaznika.UCES);
                ((MySimulation) mySim()).getStatsVykonov()[0]++;
            } else if (percentage < 0.35) {
                ((MySimulation) mySim()).getStatsVykonov()[2]++;
                zakaznik.setTypZakaznika(TypZakaznika.LICENIE);
            } else {
                ((MySimulation) mySim()).getStatsVykonov()[4]++;
                zakaznik.setTypZakaznika(TypZakaznika.UCESAJLICENIE);
            }
            if (zakaznik.getTypZakaznika() == TypZakaznika.LICENIE || zakaznik.getTypZakaznika() == TypZakaznika.UCESAJLICENIE) {
                double percentage2 = randPercentageCiseniePleti.nextDouble();
                if (percentage2 < 0.35) {
                    ((MySimulation) mySim()).getStatsVykonov()[6]++;
                    zakaznik.setGoToHlbkoveLicenie(true);
                    zakaznik.setHlbkoveLicenie(true);
                }
            }
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

        MySimulation mySimulation = ((MySimulation) mySim());

        if (zakaznik.getTypZakaznika() == TypZakaznika.UCESAJLICENIE) {
            mySimulation.getStatsVykonov()[5]++;
            message.setCode(Mc.obsluhaLicenie);
            message.setAddressee(mySim().findAgent(Id.agentLicenia));
            request(message);
        } else {
            mySimulation.getStatsVykonov()[1]++;
            zakaznik.setObsluzeny();
            message.setCode(Mc.obsluhaRecepia);
            message.setAddressee(mySim().findAgent(Id.agentRecepcie));

            request(message);

            MyMessage msgCopy = new MyMessage((MyMessage) message);
            msgCopy.setCode(Mc.zmenaRadu);
            msgCopy.setAddressee(mySim().findAgent(Id.agentRecepcie));
            notice(msgCopy);
        }

    }

	//meta! sender="AgentLicenia", id="19", type="Response"
	public void processObsluhaLicenie(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        message.setSender(myAgent());

        MySimulation mySimulation = ((MySimulation) mySim());

        if (zakaznik.getTypZakaznika() != TypZakaznika.UCESAJLICENIE && !zakaznik.isGoToHlbkoveLicenie()) {
            mySimulation.getStatsVykonov()[3]++;
        }
        if (zakaznik.isGoToHlbkoveLicenie())
            mySimulation.getStatsVykonov()[7]++;
        if (zakaznik.isGoToHlbkoveLicenie()) {
            message.setCode(Mc.obsluhaLicenie);
            message.setAddressee(mySim().findAgent(Id.agentLicenia));
            zakaznik.setGoToHlbkoveLicenie(false);
            request(message);
        } else {
            zakaznik.setObsluzeny();
            message.setCode(Mc.obsluhaRecepia);
            message.setAddressee(mySim().findAgent(Id.agentRecepcie));
            request(message);

            MyMessage msgCopy = new MyMessage((MyMessage) message);
            msgCopy.setCode(Mc.zmenaRadu);
            msgCopy.setAddressee(mySim().findAgent(Id.agentRecepcie));
            notice(msgCopy);
        }

    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentParkoviska", id="62", type="Response"
	public void processParkovanie(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        if (zakaznik.odchadza()) {
            message.setCode(Mc.obsluhaZakaznika);
            response(message);
        } else {
            message.setCode(Mc.obsluhaRecepia);
            message.setAddressee(mySim().findAgent(Id.agentRecepcie));
            request(message);
        }
    }

	//meta! sender="AgentModelu", id="79", type="Notice"
	public void processUzavri(MessageForm message) {
        message.setAddressee(mySim().findAgent(Id.agentRecepcie));
        notice(message);
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.obsluhaRecepia:
			processObsluhaRecepia(message);
		break;

		case Mc.parkovanie:
			processParkovanie(message);
		break;

		case Mc.obsluhaUcesy:
			processObsluhaUcesy(message);
		break;

		case Mc.uzavri:
			processUzavri(message);
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