package continualAssistants;

import OSPABA.*;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import myGenerators.RandUniformContinuous;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="65"
public class ProcesParkovania extends Process {

    private static final double vehicleSpeed = 20.0 / 3.6;
    private static final double vehicleSpeedParking = 12.0 / 3.6;
    private static final double width = 35.0;
    private static final double toA = 13.0;
    private static final double toB = 10.0;
    private static final double toC = 8.0;
    private static final double parkingSize = width / 15;


    private final RandUniformContinuous randPersonSpeed = new RandUniformContinuous(2.5 - 0.7, 2.5 + 0.7);

    public ProcesParkovania(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="AgentParkoviska", id="66", type="Start"
    public void processStart(MessageForm message) {
        //TODO: create some strategies
        message.setCode(Mc.koniecParkovania);
        MySimulation mySimulation = ((MySimulation) mySim());
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();

        int zaciatok = 1;
        int strategia = 1;
        int parkingLocation;
        double holdTime = 0;
        int spokojnost;
        switch (strategia) {
            case 1:
                zaciatok = 1;
                break;
            case 2:
                //zaciatok = n - (int) Math.ceil(2.0 * n / 3);
                break;
            case 3:
                //zaciatok = n - (int) Math.ceil(n / 2.0);
                break;
            case 4:
                break;
            case 5:
                break;
        }

        boolean zaparkovane = false;

//        for (int i = 1; i < mySimulation.getFreeSlots().size(); i++) {
//            Integer freeSlot = mySimulation.getFreeSlots().get(i);
//            if (freeSlot <= zaciatok) {
//                result = freeSlot;
//                zaparkovane = true;
//                break;
//            }
//        }

        if (!mySimulation.getFreeSlots().isEmpty()) {
            int miesto = mySimulation.getFreeSlots().poll();
            zakaznik.setZaparkovane(miesto);
            mySimulation.getFreeSlots().remove(0);

            int p = Config.pocetParkingMiestRadu;
            int miesto1 = miesto + 1;
            if (miesto <= p) {
                holdTime += ((width + toA) / vehicleSpeed) + (parkingSize * miesto1 / vehicleSpeedParking);
                holdTime += (p - miesto1) * parkingSize / randPersonSpeed.nextValue();
                spokojnost = p - miesto;
            } else if (miesto <= 30) {
                holdTime += ((width * 2 + toA * 2 + toB) / vehicleSpeed) + (parkingSize * miesto1 / vehicleSpeedParking) + width / vehicleSpeedParking;
                holdTime += (2 * p - miesto1) * parkingSize / randPersonSpeed.nextValue();
                spokojnost = 2 * p - miesto + 50 + p;
            } else {
                holdTime += ((width * 3 + toA * 3 + toB + 2 + toC) / vehicleSpeed) + (parkingSize * miesto1 / vehicleSpeedParking) + width / vehicleSpeedParking * 2;
                holdTime += (3 * p - miesto1) * parkingSize / randPersonSpeed.nextValue();
                spokojnost = 3 * p - miesto + 100 + 2 * p;
            }

            ((MySimulation) mySim()).getStatsVykonov()[12] += spokojnost;

            hold(holdTime, message);

        } else {
            zakaznik.setZaparkovane(-1);
            zakaznik.setStavZakaznika(StavZakaznika.NEZAPARKOVANE);
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.koniecParkovania:
                assistantFinished(message);
                break;
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public AgentParkoviska myAgent() {
        return (AgentParkoviska) super.myAgent();
    }

}