package dungeon.utily;

/**
 * Utilidades para colores ANSI en consola.
 */
public final class

Ansi {

    /**
     * Evita crear objetos de utilidad.
     */
    private Ansi() {}

    public static final String RESET  = "\u001B[0m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";
    public static final String GRAY   = "\u001B[90m";
    public static final String BOLD   = "\u001B[1m";

    /**
     * Aplica color a un texto.
     */
    public static String colorear(String color, String texto) {
        return color + texto + RESET;
    }

    /**
     * Aplica negrita y color.
     */
    public static String negrita(String color, String texto) {
        return BOLD + color + texto + RESET;
    }

    /**
     * Quita códigos ANSI del texto.
     */
    public static String limpiar(String texto) {
        return texto.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
