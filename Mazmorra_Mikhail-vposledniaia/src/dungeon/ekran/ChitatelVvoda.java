package dungeon.ekran;

import java.util.Scanner;

/**
 * Lee comandos y datos escritos por teclado.
 */
public class ChitatelVvoda {

    private final Scanner scanner;

    /**
     * Crea el lector de entrada.
     */
    public ChitatelVvoda() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Lee una línea completa.
     */
    public String chitat() {
        try {
            return scanner.nextLine().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Lee la primera letra del comando.
     */
    public String chitateKomandu() {
        String line = chitat();
        return line.isEmpty() ? "" : line.toUpperCase().substring(0, 1);
    }

    /**
     * Lee un número dentro de un rango.
     */
    public int chitatInt(int max) {
        try {
            int n = Integer.parseInt(chitat());
            if (n >= 1 && n <= max) return n;
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }

    /**
     * Muestra una pregunta y lee la respuesta.
     */
    public String sprosit(String pregunta) {
        System.out.print(pregunta);
        return chitat();
    }

    /**
     * Cierra el lector de teclado.
     */
    public void zakryt() {
        scanner.close();
    }
}
