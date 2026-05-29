package dungeon.veshchi;

/**
 * Armadura que aumenta la defensa del jugador.
 */
public class Bronya extends Predmet {

    /**
     * Armadura que aumenta la defensa del jugador.
     */
    public enum Slot {
        GOLOVA("Cabeza"),
        TELO("Torso"),
        NOGI("Piernas");

        private String nazvanie;

        Slot(String nazvanie) {
            this.nazvanie = nazvanie;
        }

        public String getNazvanie() {
            return nazvanie;
        }
    }

    private int zaschita;
    private Slot slot;
    private String redkost;

    /**
     * Crea una pieza de armadura.
     */
    public Bronya(String nazvanie, String opisanie, int tsena, int zaschita, Slot slot, String redkost) {
        super(nazvanie, opisanie, tsena);
        this.zaschita = zaschita;
        this.slot = slot;
        this.redkost = redkost;
    }

    /**
     * Devuelve defensa.
     */
    public int getZaschita() { return zaschita; }
    /**
     * Devuelve la parte equipada.
     */
    public Slot getSlot() { return slot; }
    /**
     * Devuelve rareza.
     */
    public String getRedkost() { return redkost; }
}
