package dungeon.personazhi;

import dungeon.utily.Ansi;

/**
 * Representa al jefe final del juego.
 */
public class Boss extends Vrag {

    /**
     * Representa al jefe final del juego.
     */
    public enum TipoBoss {
        DRAGON("Dragón", "D", Ansi.RED + Ansi.BOLD, 260, 48, 12, 120);

        public final String nazvanie;
        public final String simvol;
        public final String cvet;
        public final int hp;
        public final int atk;
        public final int def;
        public final int nagrada;

        TipoBoss(String nazvanie, String simvol, String cvet, int hp, int atk, int def, int nagrada) {
            this.nazvanie = nazvanie;
            this.simvol = simvol;
            this.cvet = cvet;
            this.hp = hp;
            this.atk = atk;
            this.def = def;
            this.nagrada = nagrada;
        }
    }

    private TipoBoss tipBoss;

    /**
     * Crea el dragón final.
     */
    public Boss() {
        super(TipoEnemigo.TROLL, 5);
        tipBoss = TipoBoss.DRAGON;
        imya = tipBoss.nazvanie;
        hp = tipBoss.hp;
        maxHp = tipBoss.hp;
        atk = tipBoss.atk;
        def = tipBoss.def;
        simvol = tipBoss.simvol;
        cvet = tipBoss.cvet;
    }

    /**
     * Devuelve el tipo de jefe.
     */
    public TipoBoss getTipoBoss() { return tipBoss; }
    @Override public int getNagrada() { return tipBoss.nagrada; }
}
