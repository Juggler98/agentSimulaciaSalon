package simulation;

import OSPABA.*;
import entities.Zamestnanec;
import entities.zakaznik.Zakaznik;

public class MyMessage extends MessageForm {

    private Zakaznik zakaznik;
    private Zamestnanec zamestnanec;

    public MyMessage(Simulation sim, Zakaznik zakaznik) {
        super(sim);
        this.zakaznik = zakaznik;
        this.zamestnanec = null;
    }

    public MyMessage(MyMessage original) {
        super(original);
        zakaznik = original.zakaznik;
        zamestnanec = original.zamestnanec;
        // copy() is called in superclass
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessage(this);
    }

    @Override
    protected void copy(MessageForm message) {
        super.copy(message);
        MyMessage original = (MyMessage) message;
        // Copy attributes
    }

    public Zakaznik getZakaznik() {
        return zakaznik;
    }

    public Zamestnanec getZamestnanec() {
        return zamestnanec;
    }

    public void setZakaznik(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
    }

    public void setZamestnanec(Zamestnanec zamestnanec) {
        this.zamestnanec = zamestnanec;
    }
}