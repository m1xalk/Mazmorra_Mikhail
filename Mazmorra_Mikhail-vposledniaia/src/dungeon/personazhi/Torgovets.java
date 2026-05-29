package dungeon.personazhi;

import dungeon.veshchi.*;
import java.util.ArrayList;

/**
 * Comerciante que vende objetos al jugador.
 */
public class Torgovets {

    private ArrayList<Predmet> predmeti;

    /**
     * Genera la tienda según el piso.
     */
    public Torgovets(int etaj) {
        predmeti = new ArrayList<>();
        predmeti.add(Predmety.sluchainoeZele(etaj));
        if (etaj >= 2) {
            predmeti.add(new Zele("Poción de restauración itogo", "Recupera 200 HP", 120, 200));
        }
        predmeti.add(Predmety.sluchainoeOruzhie(etaj));
        predmeti.add(Predmety.sluchainayaBronya(etaj));
    }

    public ArrayList<Predmet> getPredmeti() { return predmeti; }

    /**
     * Compra un objeto si el jugador puede pagarlo.
     */
    public boolean kupit(Igrok igrok, int indeks) {
        if (indeks >= 0 && indeks < predmeti.size()) {
            Predmet p = predmeti.get(indeks);
            if (igrok.tratitZoloto(p.getTsena())) {
                igrok.getInventar().dobavit(p);
                predmeti.remove(indeks);
                return true;
            }
        }
        return false;
    }
}
