package dungeon.utily;

/**
 * Direcciones posibles de movimiento.
 */
public enum Napravlenie {
    SEVER(0, -1, "W"),
    VOSTOK(1, 0, "D"),
    YUG(0, 1, "S"),
    ZAPAD(-1, 0, "A");

    public final int dx;
    public final int dy;
    public final String tecla;

    Napravlenie(int dx, int dy, String tecla) {
        this.dx = dx;
        this.dy = dy;
        this.tecla = tecla;
    }
}
