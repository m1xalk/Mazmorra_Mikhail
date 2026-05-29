package dungeon.veshchi;

/**
 * Clase base de todos los objetos.
 */
public abstract class Predmet {

protected String nazvanie;
protected String opisanie;
protected int tsena;

/**
 * Crea un objeto con nombre, descripción y precio.
 */
public Predmet(String nazvanie, String opisanie, int tsena) {
this.nazvanie = nazvanie;
this.opisanie = opisanie;
this.tsena = tsena;
}

public String getNazvanie() { return nazvanie; }
public String getOpisanie() { return opisanie; }
public int getTsena() { return tsena; }
/**
 * Devuelve el nombre visible.
 */
public String getColoredName() { return nazvanie; }

@Override
/**
 * Devuelve nombre y descripción.
 */
public String toString() {
return nazvanie + " - " + opisanie;
}
}
