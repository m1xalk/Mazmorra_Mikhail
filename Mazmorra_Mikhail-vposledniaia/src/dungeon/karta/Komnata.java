package dungeon.karta;

import dungeon.personazhi.*;
import dungeon.veshchi.Predmet;
import java.util.ArrayList;

/**
 * Representa una habitación del mapa.
 */
public class Komnata {

    /**
     * Representa una habitación del mapa.
     */
    public enum TipoHabitacion { START, NORMAL, KLAD, MAGAZIN, BOSS, VYHOD }

    public int id;
    public int x;
    public int y;
    public TipoHabitacion tip;
    public boolean[] vyhodi;
    public boolean otkrita;
    public boolean poseschena;
    public boolean ochischena;
    public boolean razgrabena;

    public ArrayList<Vrag> vragi;
    public ArrayList<Predmet> lut;
    public Boss boss;
    public Torgovets torgovets;

    /**
     * Crea una habitación con posición y tipo.
     */
    public Komnata(int id, int x, int y, TipoHabitacion tip) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tip = tip;
        this.vyhodi = new boolean[4];
        this.vragi = new ArrayList<>();
        this.lut = new ArrayList<>();
    }

    /**
     * Comprueba si hay enemigos vivos.
     */
    public boolean estVragi() {
        for (Vrag v : vragi) {
            if (v.isZhiv()) return true;
        }
        return boss != null && boss.isZhiv();
    }

    /**
     * Devuelve el primer enemigo vivo.
     */
    public Vrag perviyVrag() {
        if (boss != null && boss.isZhiv()) return boss;
        for (Vrag v : vragi) {
            if (v.isZhiv()) return v;
        }
        return null;
    }

    /**
     * Devuelve el nombre visible de la habitación.
     */
    public String getNazvanieKomnaty() {
        if (tip == TipoHabitacion.START) return "inicio";
        if (tip == TipoHabitacion.KLAD) return "tesoro";
        if (tip == TipoHabitacion.MAGAZIN) return "tienda";
        if (tip == TipoHabitacion.BOSS) return "boss";
        if (tip == TipoHabitacion.VYHOD) return "salida";
        return "normal";
    }
}
