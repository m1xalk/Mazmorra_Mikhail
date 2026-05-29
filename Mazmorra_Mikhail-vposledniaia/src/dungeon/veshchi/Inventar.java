package dungeon.veshchi;

import java.util.*;

/**
 * Guarda objetos y equipo del jugador.
 */
public class Inventar {

    private ArrayList<Predmet> predmeti;

    private Oruzhie oruzheEquip;
    private Bronya kaskaEquip;
    private Bronya teloEquip;
    private Bronya nogiEquip;

    /**
     * Crea una lista vacía de objetos.
     */
    public Inventar() {
        predmeti = new ArrayList<>();
    }

    /**
     * Añade un objeto.
     */
    public void dobavit(Predmet p) { predmeti.add(p); }

    public ArrayList<Predmet> getPredmeti() { return predmeti; }
    public Oruzhie getOruzhie() { return oruzheEquip; }
    public Bronya getKaska() { return kaskaEquip; }
    public Bronya getTelo() { return teloEquip; }
    public Bronya getNogi() { return nogiEquip; }

    /**
     * Equipa un arma por índice.
     */
    public void equipOruzhie(int i) {
        if (i >= 0 && i < predmeti.size() && predmeti.get(i) instanceof Oruzhie) {
            oruzheEquip = (Oruzhie) predmeti.get(i);
        }
    }

    /**
     * Equipa armadura por tipo.
     */
    public void equiparBronya(int i) {
        if (i >= 0 && i < predmeti.size() && predmeti.get(i) instanceof Bronya) {
            Bronya b = (Bronya) predmeti.get(i);
            if (b.getSlot() == Bronya.Slot.GOLOVA) {
                kaskaEquip = b;
            } else if (b.getSlot() == Bronya.Slot.TELO) {
                teloEquip = b;
            } else if (b.getSlot() == Bronya.Slot.NOGI) {
                nogiEquip = b;
            }
        }
    }

    /**
     * Devuelve el daño del arma.
     */
    public int getUronOruzhiya() {
        if (oruzheEquip == null) return 0;
        return oruzheEquip.getUron();
    }

    /**
     * Suma la defensa equipada.
     */
    public int getZaschitaBroni() {
        int itogo = 0;
        if (kaskaEquip != null) itogo += kaskaEquip.getZaschita();
        if (teloEquip != null) itogo += teloEquip.getZaschita();
        if (nogiEquip != null) itogo += nogiEquip.getZaschita();
        return itogo;
    }

    /**
     * Elimina un objeto del inventario.
     */
    public void eliminar(Predmet p) { predmeti.remove(p); }
}
