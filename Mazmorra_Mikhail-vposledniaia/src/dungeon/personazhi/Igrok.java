package dungeon.personazhi;

import dungeon.veshchi.Inventar;
import dungeon.veshchi.Zele;
import dungeon.veshchi.Oruzhie;
import dungeon.utily.KlassGeroya;

/**
 * Guarda los datos del jugador.
 */
public class Igrok extends Base {

private KlassGeroya klassGeroya;
private int zoloto;
private int ubitye;
private int tekuschiyEtaj;
private Inventar inventar;

/**
 * Crea el jugador con su clase inicial.
 */
public Igrok(String imya, KlassGeroya klassGeroya) {
super(imya, klassGeroya.baseHp, klassGeroya.baseAtk, klassGeroya.baseDef, "@", klassGeroya.ansiColor);
this.klassGeroya = klassGeroya;
this.zoloto = 50;
this.ubitye = 0;
this.tekuschiyEtaj = 1;
this.inventar = new Inventar();

inventar.dobavit(new Oruzhie("Espada oxidada", "Arma inicial", 0, 4));
inventar.dobavit(new Zele("Poción de vida", "Recupera 35 HP", 25, 35));
inventar.equipOruzhie(0);
}

/**
 * Calcula el daño total del jugador.
 */
public int ataka() {
return atk + inventar.getUronOruzhiya();
}

@Override
/**
 * Suma defensa base y armadura.
 */
public int getDef() {
return def + inventar.getZaschitaBroni();
}

/**
 * Añade oro al jugador.
 */
public void addZoloto(int cantidad) { zoloto += cantidad; }

/**
 * Gasta oro si hay suficiente.
 */
public boolean tratitZoloto(int cantidad) {
if (zoloto >= cantidad) {
zoloto -= cantidad;
return true;
}
return false;
}

/**
 * Suma un enemigo derrotado.
 */
public void addUbitogo() { ubitye++; }
/**
 * Actualiza el piso actual.
 */
public void setEtaj(int etaj) { this.tekuschiyEtaj = etaj; }
public KlassGeroya getKlass() { return klassGeroya; }
public int getOro() { return zoloto; }
public int getUbitye() { return ubitye; }
public int getEtaj() { return tekuschiyEtaj; }
public Inventar getInventar() { return inventar; }
}
