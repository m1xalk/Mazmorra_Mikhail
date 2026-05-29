package dungeon.personazhi;

import dungeon.veshchi.Predmet;
import dungeon.veshchi.Predmety;
import dungeon.utily.Ansi;

import java.util.ArrayList;
import java.util.Random;

/**
 * Representa un enemigo normal.
 */
public class Vrag extends Base {

    /**
     * Representa un enemigo normal.
     */
    public enum TipoEnemigo {
        GOBLIN("Goblin", "g", Ansi.GREEN, 35, 18, 2, 8, 1),
        KRYSA("Rata", "r", Ansi.YELLOW, 24, 14, 1, 5, 1),
        SKELET("Esqueleto", "s", Ansi.WHITE, 45, 24, 3, 12, 1),
        TROLL("Troll", "T", Ansi.RED, 90, 34, 6, 20, 2);

        public final String nazvanie;
        public final String simvol;
        public final String cvet;
        public final int hp;
        public final int atk;
        public final int def;
        public final int nagrada;
        public final int minEtaj;

        TipoEnemigo(String nazvanie, String simvol, String cvet, int hp, int atk, int def,
                int nagrada, int minEtaj) {
            this.nazvanie = nazvanie;
            this.simvol = simvol;
            this.cvet = cvet;
            this.hp = hp;
            this.atk = atk;
            this.def = def;
            this.nagrada = nagrada;
            this.minEtaj = minEtaj;
        }
    }

    private static final Random rand = new Random();
    private TipoEnemigo tip;
    private int nagrZoloto;

    /**
     * Crea un enemigo escalado por piso.
     */
    public Vrag(TipoEnemigo tip, int etaj) {
        super(tip.nazvanie, masshtab(tip.hp, etaj), masshtab(tip.atk, etaj), masshtab(tip.def, etaj), tip.simvol, tip.cvet);
        this.tip = tip;
        this.nagrZoloto = masshtab(tip.nagrada, etaj);
    }

    /**
     * Aumenta estadísticas según el piso.
     */
    private static int masshtab(int base, int etaj) {
        return (int) Math.round(base * (1 + (etaj - 1) * 0.05));
    }

    /**
     * Devuelve un enemigo aleatorio válido.
     */
    public static Vrag sluchainiyVrag(int etaj) {
        ArrayList<TipoEnemigo> vozmozhnye = new ArrayList<>();
        vozmozhnye.add(TipoEnemigo.GOBLIN);
        vozmozhnye.add(TipoEnemigo.KRYSA);
        vozmozhnye.add(TipoEnemigo.SKELET);
        if (etaj >= 2) vozmozhnye.add(TipoEnemigo.TROLL);
        return new Vrag(vozmozhnye.get(rand.nextInt(vozmozhnye.size())), etaj);
    }

    /**
     * Devuelve el daño del enemigo.
     */
    public int ataka() {
        return atk;
    }

    /**
     * Genera botín al derrotarlo.
     */
    public ArrayList<Predmet> getLoot(int etaj) {
        ArrayList<Predmet> lut = new ArrayList<>();
        if (rand.nextInt(100) < 25) lut.add(Predmety.sluchainiyPredmet(etaj));
        return lut;
    }

    public TipoEnemigo getTipo() { return tip; }
    public int getNagrada() { return nagrZoloto; }
}
