package dungeon.dannye;

import dungeon.personazhi.Igrok;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Guarda y muestra las estadísticas de las partidas.
 */
public class Statistika {

    private static final String ARCHIVO = "statistics.txt";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Añade el resultado de una partida al archivo.
     */
    public static void sohranit(Igrok igrok, boolean pobeda) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            String data = LocalDateTime.now().format(FORMATO);
            String rezultat = pobeda ? "Victoria" : "Derrota";
            w.write(data + ";" + igrok.getImya() + ";" + rezultat + ";"
                    + igrok.getUbitye() + ";" + igrok.getOro() + ";" + igrok.getEtaj());
            w.newLine();
        } catch (IOException e) {
            System.out.println("No se pudo sohranit la estadística: " + e.getMessage());
        }
    }

    /**
     * Muestra el resumen y la tabla de partidas.
     */
    public static void pokazat() {
        File f = new File(ARCHIVO);
        if (!f.exists()) {
            System.out.println("Todavía no hay estadísticas guardadas.");
            return;
        }
        ArrayList<String[]> partii = chitat(f);
        if (partii.isEmpty()) {
            System.out.println("El archivo de estadísticas está vacío.");
            return;
        }
        itog(partii);
        tablitsa(partii);
    }

    /**
     * Lee el archivo de estadísticas.
     */
    private static ArrayList<String[]> chitat(File f) {
        ArrayList<String[]> partii = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String stroka;
            while ((stroka = r.readLine()) != null) {
                String[] chasti = stroka.split(";");
                if (chasti.length == 6) {
                    partii.add(chasti);
                } else if (chasti.length >= 7) {
                    partii.add(new String[] {chasti[0], chasti[1], chasti[2], chasti[4], chasti[5], chasti[6]});
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudieron leer las estadísticas: " + e.getMessage());
        }
        return partii;
    }

    /**
     * Calcula los datos generales de las partidas.
     */
    private static void itog(ArrayList<String[]> partii) {
        int pobedy = 0;
        int temp = 0; 
        int porazheniya = 0;
        int maxElim = 0;
        int maxPiso = 0;
        String mejorElim = "-";
        String mejorPiso = "-";

        for (String[] p : partii) {
            if (p[2].equalsIgnoreCase("Victoria")) {
                pobedy++;
            } else {
                porazheniya++;
            }
            int elim = vInt(p[3]);
            int etaj = vInt(p[5]);
            if (elim > maxElim) { maxElim = elim; mejorElim = p[1]; }
            if (etaj > maxPiso) { maxPiso = etaj; mejorPiso = p[1]; }
        }

        System.out.println("============================================");
        System.out.println("              ESTADÍSTICAS");
        System.out.println("============================================");
        System.out.println("Partidas jugadas : " + partii.size());
        System.out.println("Victorias        : " + pobedy);
        System.out.println("Derrotas         : " + porazheniya);
        System.out.println("Más bajas        : " + maxElim + " (" + mejorElim + ")");
        System.out.println("Mayor piso       : " + maxPiso + " (" + mejorPiso + ")");
        System.out.println();
    }

    /**
     * Imprime las partidas en formato de tabla.
     */
    private static void tablitsa(ArrayList<String[]> partii) {
        System.out.printf("%-17s %-12s %-10s %7s %6s %5s%n",
                "Fecha", "Jugador", "Resultado", "Bajas", "Oro", "Piso");
        System.out.println("---------------------------------------------------------------");
        for (String[] p : partii) {
            System.out.printf("%-17s %-12s %-10s %7s %6s %5s%n",
                    p[0], p[1], p[2], p[3], p[4], p[5]);
        }
    }

    /**
     * Convierte texto a número de forma segura.
     */
    private static int vInt(String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return 0; }
    }
}
