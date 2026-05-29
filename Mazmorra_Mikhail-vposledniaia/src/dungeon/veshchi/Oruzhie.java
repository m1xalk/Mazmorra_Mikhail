package dungeon.veshchi;

/**
 * Arma que aumenta el daño del jugador.
 */
public class Oruzhie extends Predmet {

    private int uron;

    /**
     * Crea un arma con daño.
     */
    public Oruzhie(String nazvanie, String opisanie, int tsena, int uron) {
        super(nazvanie, opisanie, tsena);
        this.uron = uron;
    }

    /**
     * Devuelve el daño del arma.
     */
    public int getUron() { return uron; }
}
