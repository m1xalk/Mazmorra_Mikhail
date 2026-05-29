package dungeon.veshchi;

/**
 * Poción que cura vida al usarse.
 */
public class Zele extends Predmet {

    private int lechenie;

    /**
     * Crea una poción con curación.
     */
    public Zele(String nazvanie, String opisanie, int tsena, int lechenie) {
        super(nazvanie, opisanie, tsena);
        this.lechenie = lechenie;
    }

    /**
     * Devuelve la curación.
     */
    public int getLechenie() { return lechenie; }
}
