package simulation;

import OSPABA.*;
import entities.pracovnik.Pracovnik;
import entities.zakaznik.Zakaznik;

public class MyMessage extends MessageForm {

    private Zakaznik zakaznik;
    private Pracovnik pracovnik;

    public MyMessage(Simulation sim, Zakaznik zakaznik) {
        super(sim);
        this.zakaznik = zakaznik;
        this.pracovnik = null;
    }

    public MyMessage(MyMessage original) {
        super(original);
        zakaznik = original.zakaznik;
        pracovnik = original.pracovnik;
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

    public Pracovnik getZamestnanec() {
        return pracovnik;
    }

    public void setZakaznik(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
    }

    public void setZamestnanec(Pracovnik pracovnik) {
        this.pracovnik = pracovnik;
    }
}