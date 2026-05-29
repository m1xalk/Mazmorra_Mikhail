package dungeon.personazhi;

/**
 * Clase base común para jugador y enemigos.
 */
public abstract class Base {

protected String imya;
protected int hp;
protected int maxHp;
protected int atk;
protected int def;
protected String simvol;
protected String cvet;
protected boolean zhiv;

/**
 * Inicializa las estadísticas básicas.
 */
public Base(String imya, int hp, int atk, int def, String simvol, String cvet) {
this.imya = imya;
this.hp = hp;
this.maxHp = hp;
this.atk = atk;
this.def = def;
this.simvol = simvol;
this.cvet = cvet;
this.zhiv = true;
}

/**
 * Aplica daño teniendo en cuenta defensa.
 */
public int poluchitUron(int uron) {
int finalUron = uron - (getDef() / 2);
if (finalUron < 1) finalUron = 1;
hp -= finalUron;
if (hp <= 0) {
hp = 0;
zhiv = false;
}
return finalUron;
}

/**
 * Recupera vida sin superar el máximo.
 */
public int lechit(int cantidad) {
int antes = hp;
hp = hp + cantidad;
if (hp > maxHp) hp = maxHp;
return hp - antes;
}

public String getImya() { return imya; }
public int getHp() { return hp; }
public int getMaxHp() { return maxHp; }
public int getDef() { return def; }
public String getCvet() { return cvet; }
public boolean isZhiv() { return zhiv; }
}
