package dungeon;

import dungeon.igra.Game;
import dungeon.utily.Ansi;

/**
 * Punto de entrada del programa.
 */
public class Main {

    /**
     * Inicia el juego y controla errores fatales.
     */
    public static void main(String[] args) {
        if (System.getenv("NO_COLOR") != null) {
            System.out.println("[INFO] Colores ANSI desactivados por NO_COLOR.");
        }

        try {
            Game game = new Game();
            game.start();
        } catch (Exception e) {
            System.err.println(Ansi.colorear(Ansi.RED,
                    "\n[ERROR FATAL] El juego ha encontrado un error inesperado:"));
            System.err.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
