package continualAssistants;

import OSPABA.*;
import entities.pracovnik.Miesto;
import entities.zakaznik.PolohaZakaznika;
import entities.zakaznik.StavZakaznika;
import entities.zakaznik.Zakaznik;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="65"
public class ProcesParkovania extends Process {

    public static final double speed = 12.0 / 3.6;
    private Miesto[][] parkovisko;

    public ProcesParkovania(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
        parkovisko = myAgent().parkovisko();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
        parkovisko = myAgent().parkovisko();
    }

    //meta! sender="AgentParkoviska", id="66", type="Start"
    public void processStart(MessageForm message) {
        message.setCode(Mc.parkuj);
        Zakaznik zakaznik = ((MyMessage) message).getZakaznik();

        zakaznik.setStavZakaznika(StavZakaznika.PARKOVANIE);

        if (zakaznik.odchadza()) {
            Miesto miesto = zakaznik.getMiesto();
            switch (miesto.getRad()) {
                case 0:
                    zakaznik.setPoloha(PolohaZakaznika.A_END);
                    break;
                case 1:
                    zakaznik.setPoloha(PolohaZakaznika.B_END);
                    break;
                case 2:
                    zakaznik.setPoloha(PolohaZakaznika.C_END);
                    break;
            }
            miesto.setZakaznik(null);
            hold((Config.miestRadu - miesto.getPozicia()) * AgentParkoviska.parkingSize / speed, message);
            zakaznik.setMiesto(null);
        } else {
            //TODO: create some strategies

            int strategia = 0;
            if (strategia == 0) {
                if (zakaznik.preskumane().contains(0) && zakaznik.preskumane().contains(1)) {
                    zakaznik.setMiesto(parkovisko[2][0]);
                } else if (zakaznik.preskumane().contains(0)) {
                    zakaznik.setMiesto(parkovisko[1][0]);
                } else {
                    zakaznik.setMiesto(parkovisko[0][0]);
                }
            } else if (strategia == 1) {
                //Ak je nejake miesto z prvych piatich volne ide dalej
                for (int i = 0; i < 5; i++) {

                    if (parkovisko[zakaznik.getMiesto().getRad()][i].getZakaznik() == null) {
                        zakaznik.setMiesto(parkovisko[zakaznik.getMiesto().getRad()][zakaznik.getMiesto().getPozicia() + 1]);
                        hold(AgentParkoviska.parkingSize / speed, message);
                        return;
                    }
                }
            } else {

            }
            hold(0, message);
        }
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.parkuj:
                Zakaznik zakaznik = ((MyMessage) message).getZakaznik();
                //parkovisko[1][3].setZakaznik(zakaznik);
                //parkovisko[0][7].setZakaznik(zakaznik);
                //Ak zakaznik neodchadza ide hladat volne miesto
                if (!zakaznik.odchadza()) {
                    //Ak je nejake miesto z prvych piatich volne ide dalej
                    for (int i = zakaznik.getMiesto().getPozicia() + 2; i < 5; i++) {
                        if (parkovisko[zakaznik.getMiesto().getRad()][i].getZakaznik() == null) {
                            zakaznik.setMiesto(parkovisko[zakaznik.getMiesto().getRad()][zakaznik.getMiesto().getPozicia() + 1]);
                            hold(AgentParkoviska.parkingSize / speed, message);
                            return;
                        }
                    }

                    //Ak je dane miesto obsadene posuva sa na dalsie
                    if (zakaznik.getMiesto().getZakaznik() != null) {
                        //Ak je na konci radu ide okolo prevadzky
                        if (zakaznik.getMiesto().getPozicia() == Config.miestRadu - 1) {
                            switch (zakaznik.getMiesto().getRad()) {
                                case 0:
                                    zakaznik.setPoloha(PolohaZakaznika.A_END);
                                    break;
                                case 1:
                                    zakaznik.setPoloha(PolohaZakaznika.B_END);
                                    break;
                                case 2:
                                    zakaznik.setPoloha(PolohaZakaznika.C_END);
                                    break;
                            }
                            zakaznik.incSpokojnost(50);
                            zakaznik.preskumane().add(zakaznik.getMiesto().getRad());
                            zakaznik.setMiesto(null);
                            if (zakaznik.preskumane().size() == parkovisko.length) {
                                zakaznik.setOdchadza();
                                zakaznik.setZaparkoval(false);
                            }
                        } else {
                            zakaznik.setMiesto(parkovisko[zakaznik.getMiesto().getRad()][zakaznik.getMiesto().getPozicia() + 1]);
                            hold(AgentParkoviska.parkingSize / speed, message);
                            return;
                        }
                    } else {
                        //Obsadi miesto
                        if (zakaznik.getMiesto().getPozicia() + 1 < Config.miestRadu && parkovisko[zakaznik.getMiesto().getRad()][zakaznik.getMiesto().getPozicia() + 1].getZakaznik() == null) {
                            zakaznik.setMiesto(parkovisko[zakaznik.getMiesto().getRad()][zakaznik.getMiesto().getPozicia() + 1]);
                            hold(AgentParkoviska.parkingSize / speed, message);
                            return;
                        } else {
                            ((MySimulation) mySim()).getStatsVykonov()[11]++;
                            zakaznik.setStavZakaznika(StavZakaznika.ZAPARKOVANE);
                            zakaznik.getMiesto().setZakaznik(zakaznik);
                            zakaznik.incSpokojnost((zakaznik.getMiesto().getRad() + 1) * Config.miestRadu - (zakaznik.getMiesto().getPozicia()));
                            ((MySimulation) mySim()).getStatsVykonov()[12] += zakaznik.getSpokojnost();
                        }
                    }
                }
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