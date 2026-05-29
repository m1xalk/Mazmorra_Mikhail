package dungeon.utily;

/**
 * Clases iniciales que puede elegir el jugador.
 */
public enum KlassGeroya {
    VOIN("Guerrero", "⚔", Ansi.RED, 120, 18, 8),
    RAZBOINIK("Pícaro", "🗡", Ansi.GREEN, 95, 22, 5),
    RYTSAR("Caballero", "🛡", Ansi.CYAN, 140, 14, 12);

    public final String nazvanie;
    public final String simvol;
    public final String ansiColor;
    public final int baseHp;
    public final int baseAtk;
    public final int baseDef;

    KlassGeroya(String nazvanie, String simvol, String ansiColor, int baseHp, int baseAtk, int baseDef) {
        this.nazvanie = nazvanie;
        this.simvol = simvol;
        this.ansiColor = ansiColor;
        this.baseHp = baseHp;
        this.baseAtk = baseAtk;
        this.baseDef = baseDef;
    }
}
