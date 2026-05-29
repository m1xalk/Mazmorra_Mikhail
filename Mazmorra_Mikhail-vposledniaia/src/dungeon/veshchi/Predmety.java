package dungeon.veshchi;

import java.util.*;

/**
 * Crea objetos aleatorios del juego.
 */
public class Predmety {

    private static final Random rand = new Random();

    /**
     * Devuelve un objeto aleatorio.
     */
    public static Predmet sluchainiyPredmet(int etaj) {
        int x = rand.nextInt(3);
        if (x == 0) return sluchainoeOruzhie(etaj);
        if (x == 1) return sluchainayaBronya(etaj);
        return sluchainoeZele(etaj);
    }

    /**
     * Genera una poción según el piso.
     */
    public static Zele sluchainoeZele(int etaj) {
        if (etaj >= 2 && rand.nextInt(100) < 25) {
            return new Zele("Poción de restauración itogo", "Recupera 200 HP", 120, 200);
        }
        if (etaj >= 2 && rand.nextBoolean()) {
            return new Zele("Poción grande", "Recupera 55 HP", 45, 55);
        }
        return new Zele("Poción de vida", "Recupera 35 HP", 25, 35);
    }

    /**
     * Genera un arma según el piso.
     */
    public static Oruzhie sluchainoeOruzhie(int etaj) {
        Oruzhie[] oruzhiya = {
            new Oruzhie("Espada corta", "+6 de daño", 35, 6),
            new Oruzhie("Hacha vieja", "+8 de daño", 45, 8),
            new Oruzhie("Espada de hierro", "+12 de daño", 75, 12),
            new Oruzhie("Mandoble", "+16 de daño", 110, 16)
        };
        return oruzhiya[Math.min(oruzhiya.length - 1, rand.nextInt(Math.min(oruzhiya.length, etaj + 1)))];
    }

    /**
     * Genera una armadura según el piso.
     */
    public static Bronya sluchainayaBronya(int etaj) {
        Bronya[] obychnye = {
            new Bronya("Gorro de cuero", "Cabeza común +2 DEF", 30, 2, Bronya.Slot.GOLOVA, "Común"),
            new Bronya("Chaleco de cuero", "Torso común +4 DEF", 45, 4, Bronya.Slot.TELO, "Común"),
            new Bronya("Botas de cuero", "Piernas común +2 DEF", 30, 2, Bronya.Slot.NOGI, "Común")
        };
        Bronya[] redkaya = {
            new Bronya("Casco de hierro", "Cabeza redkaya +4 DEF", 75, 4, Bronya.Slot.GOLOVA, "Rara"),
            new Bronya("Cota de malla", "Torso redkaya +7 DEF", 100, 7, Bronya.Slot.TELO, "Rara"),
            new Bronya("Grebas de hierro", "Piernas raras +4 DEF", 75, 4, Bronya.Slot.NOGI, "Rara")
        };
        Bronya[] legendarnaya = {
            new Bronya("Corona del guardián", "Cabeza legendarnaya +6 DEF", 140, 6, Bronya.Slot.GOLOVA, "Legendaria"),
            new Bronya("Coraza del dragón", "Torso legendarnaya +11 DEF", 220, 11, Bronya.Slot.TELO, "Legendaria"),
            new Bronya("Grebas del campeón", "Piernas legendarias +6 DEF", 140, 6, Bronya.Slot.NOGI, "Legendaria")
        };

        Bronya[][] gruppy = { obychnye, redkaya, legendarnaya };
        int maxGruppa = Math.min(2, etaj / 2);
        int gruppa = rand.nextInt(maxGruppa + 1);
        return gruppy[gruppa][rand.nextInt(gruppy[gruppa].length)];
    }

    /**
     * Genera botín bueno para tesoros.
     */
    public static ArrayList<Predmet> horoshiyLut(int etaj) {
        ArrayList<Predmet> lut = new ArrayList<>();
        lut.add(horosheeOruzhie(etaj));
        lut.add(horoshayaBronya(etaj));
        lut.add(new Zele("Poción grande", "Recupera 55 HP", 45, 55));
        if (etaj >= 2 && rand.nextBoolean()) {
            lut.add(new Zele("Poción de restauración itogo", "Recupera 200 HP", 120, 200));
        }
        if (etaj >= 3) {
            lut.add(sluchainiyPredmet(etaj));
        }
        return lut;
    }

    /**
     * Devuelve un arma garantizada.
     */
    private static Oruzhie horosheeOruzhie(int etaj) {
        if (etaj <= 1) return new Oruzhie("Espada corta", "+6 de daño", 35, 6);
        if (etaj == 2) return new Oruzhie("Hacha vieja", "+8 de daño", 45, 8);
        if (etaj == 3) return new Oruzhie("Espada de hierro", "+12 de daño", 75, 12);
        return new Oruzhie("Mandoble", "+16 de daño", 110, 16);
    }

    /**
     * Devuelve una armadura garantizada.
     */
    private static Bronya horoshayaBronya(int etaj) {
        Bronya.Slot slot = Bronya.Slot.values()[rand.nextInt(Bronya.Slot.values().length)];
        if (etaj <= 1) {
            if (slot == Bronya.Slot.GOLOVA) return new Bronya("Gorro de cuero", "Cabeza común +2 DEF", 30, 2, slot, "Común");
            if (slot == Bronya.Slot.TELO) return new Bronya("Chaleco de cuero", "Torso común +4 DEF", 45, 4, slot, "Común");
            return new Bronya("Botas de cuero", "Piernas común +2 DEF", 30, 2, slot, "Común");
        }
        if (etaj <= 3) {
            if (slot == Bronya.Slot.GOLOVA) return new Bronya("Casco de hierro", "Cabeza redkaya +4 DEF", 75, 4, slot, "Rara");
            if (slot == Bronya.Slot.TELO) return new Bronya("Cota de malla", "Torso redkaya +7 DEF", 100, 7, slot, "Rara");
            return new Bronya("Grebas de hierro", "Piernas raras +4 DEF", 75, 4, slot, "Rara");
        }
        if (slot == Bronya.Slot.GOLOVA) return new Bronya("Corona del guardián", "Cabeza legendarnaya +6 DEF", 140, 6, slot, "Legendaria");
        if (slot == Bronya.Slot.TELO) return new Bronya("Coraza del dragón", "Torso legendarnaya +11 DEF", 220, 11, slot, "Legendaria");
        return new Bronya("Grebas del campeón", "Piernas legendarias +6 DEF", 140, 6, slot, "Legendaria");
    }
}
