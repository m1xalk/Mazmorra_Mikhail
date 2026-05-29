package dungeon.igra;

import dungeon.personazhi.Vrag;
import dungeon.personazhi.Igrok;
import java.util.ArrayList;

/**
 * Gestiona un turno de combate.
 */
public class Bitva {

    /**
     * Resuelve el ataque del jugador y la respuesta del enemigo.
     */
    public static void atakovat(Igrok igrok, Vrag vrag, ArrayList<String> log) {
        int uron = igrok.ataka();
        int realUron = vrag.poluchitUron(uron);
        log.add("Daño causado: " + realUron);

        if (!vrag.isZhiv()) {
            return;
        }

        int uronVraga = vrag.ataka();
        int polucheno = igrok.poluchitUron(uronVraga);
        log.add("Daño recibido: " + polucheno);
    }
}
