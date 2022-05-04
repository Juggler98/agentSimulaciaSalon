package managers;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;

//meta! id="8"
public class ManagerRecepcie extends ManagerPracovnika {
    public ManagerRecepcie(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentSalonu", id="17", type="Request"
	public void processObsluhaRecepia(MessageForm message) {
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        if (!zakaznik.isObsluzeny() && zakaznik.isAutom()) {
            zakaznik.setCasPrichodu(mySim().currentTime());
            zakaznik.setStavZakaznika(StavZakaznika.PRICHADZA);
        }
        zacniObsluhu(message);
    }

	//meta! sender="ProcesObsluhyRecepcia", id="26", type="Finish"
	public void processFinish(MessageForm message) {
        ukonciObsluhu(message);

		Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
        if (!zakaznik.isObsluzeny()) {
			myAgent().incPocetObsluhovanychRecepcia(-1);
		} else {
			zakaznik.setCasOdchodu(mySim().currentTime());
		}

        message.setCode(Mc.obsluhaRecepia);
        response(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentSalonu", id="76", type="Notice"
	public void processZmenaRadu(MessageForm message) {
        obsluzDalsieho(message);
    }

	//meta! sender="AgentSalonu", id="80", type="Notice"
	public void processUzavri(MessageForm message) {
        myAgent().uzavri();
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
		case Mc.zmenaRadu:
			processZmenaRadu(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.obsluhaRecepia:
			processObsluhaRecepia(message);
		break;

		case Mc.uzavri:
			processUzavri(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentRecepcie myAgent() {
        return (AgentRecepcie) super.myAgent();
    }

}