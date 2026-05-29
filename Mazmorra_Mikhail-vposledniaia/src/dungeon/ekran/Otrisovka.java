package dungeon.ekran;

import dungeon.personazhi.Boss;
import dungeon.personazhi.Vrag;
import dungeon.personazhi.Igrok;
import dungeon.veshchi.Bronya;
import dungeon.veshchi.Inventar;
import dungeon.veshchi.Predmet;
import dungeon.veshchi.Zele;
import dungeon.veshchi.Oruzhie;
import dungeon.utily.Ansi;
import dungeon.utily.KlassGeroya;
import dungeon.karta.Etaj;
import dungeon.karta.Komnata;

import java.util.ArrayList;

/**
 * Dibuja la interfaz del juego en consola.
 */
public class Otrisovka {

    private static final int SCENE_W = 90;
    private static final int MAP_W = 31;
    private static final int TOTAL_W = SCENE_W + MAP_W + 4;

    /**
     * Dibuja la pantalla principal del turno.
     */
    public static void otrisovat(Etaj floor, Komnata current, Igrok player, ArrayList<String> log) {
        ochistEkran();
        zagolovok(floor.getNomerEtaja(), current);
        pechatRyadom(buildScene(current), buildMiniMap(floor, current));
        printHud(player);
        pechatLog(log);
    }

    /**
     * Muestra el inventario del jugador.
     */
    public static void pokazatInventar(Igrok player) {
        ochistEkran();

        Inventar inventory = player.getInventar();
        ArrayList<Predmet> gear = new ArrayList<>();
        ArrayList<Predmet> potions = new ArrayList<>();
        separateItems(inventory.getPredmeti(), gear, potions);

        pechatZheltuyuLiniyu('═');
        System.out.println(Ansi.negrita(Ansi.YELLOW, tsentr("INVENTARIO SIN LÍMITE  —  " + player.getImya(), TOTAL_W)));
        pechatZheltuyuLiniyu('═');

        printSnaryazhenie(inventory);
        printItemList("OBJETOS", gear, inventory.getPredmeti(), false);
        printItemList("POCIONES", potions, inventory.getPredmeti(), true);

        pechatSeruyuLiniyu('─');
        System.out.println(
                Ansi.negrita(Ansi.CYAN, "  [E] ") + Ansi.colorear(Ansi.WHITE, "Equipar   ")
                        + Ansi.negrita(Ansi.GREEN, "[R] ") + Ansi.colorear(Ansi.WHITE, "Usar poción   ")
                        + Ansi.negrita(Ansi.GRAY, "[X] ") + Ansi.colorear(Ansi.WHITE, "Cerrar")
        );
        pechatZheltuyuLiniyu('═');
    }

    /**
     * Muestra el menú principal.
     */
    public static void pokazatTitulnyEkran() {
        ochistEkran();
        System.out.println(Ansi.negrita(Ansi.YELLOW, "\n  DUNGEON CRAWLER\n"));
        System.out.println(Ansi.negrita(Ansi.RED, "  MAZMORRA — versión simplificada\n"));
        System.out.println(Ansi.negrita(Ansi.WHITE, "  [1] Nueva partida    [2] Estadísticas    [3] Salir\n"));
    }

    /**
     * Muestra las clases disponibles.
     */
    public static void pokazatVyborKlassa() {
        ochistEkran();
        System.out.println(Ansi.negrita(Ansi.YELLOW, "\n  ELIGE CLASE\n"));

        KlassGeroya[] classes = KlassGeroya.values();
        for (int i = 0; i < classes.length; i++) {
            KlassGeroya klass = classes[i];
            System.out.println(Ansi.negrita(klass.ansiColor, "  [" + (i + 1) + "] " + klass.simvol + " " + klass.nazvanie));
            System.out.println("       HP:" + klass.baseHp + "  DAÑO:" + klass.baseAtk + "  ARMADURA:" + klass.baseDef + "\n");
        }
    }

    /**
     * Muestra la pantalla de derrota.
     */
    public static void pokazatGameOver(Igrok player) {
        ochistEkran();
        System.out.println(Ansi.negrita(Ansi.RED, "\n  FIN DEL JUEGO\n"));
        pechatItog(player, false);
    }

    /**
     * Muestra la pantalla de victoria.
     */
    public static void pokazatPobeda(Igrok player) {
        ochistEkran();
        System.out.println(Ansi.negrita(Ansi.YELLOW, "\n  ¡VICTORIA!\n"));
        pechatItog(player, true);
    }

    /**
     * Imprime la cabecera del piso actual.
     */
    private static void zagolovok(int floorNumber, Komnata room) {
        String leftText = "  MAZMORRA  PISO " + floorNumber + "/5";
        String rightText = "[ " + room.getNazvanieKomnaty().toUpperCase() + " ]  ";
        int gap = TOTAL_W - leftText.length() - rightText.length();

        if (gap < 2) {
            gap = 2;
        }

        System.out.println(
                Ansi.negrita(Ansi.YELLOW, leftText)
                        + Ansi.colorear(Ansi.GRAY, " " + povtorit('═', gap - 2) + " ")
                        + Ansi.negrita(Ansi.YELLOW, rightText)
        );
        pechatSeruyuLiniyu('═');
    }

    /**
     * Elige la escena según la habitación.
     */
    private static String[] buildScene(Komnata room) {
        if (room.tip == Komnata.TipoHabitacion.START) {
            return stsenaStart(room);
        }
        if (room.tip == Komnata.TipoHabitacion.VYHOD) {
            return stsenaVyhod(room);
        }
        if (room.tip == Komnata.TipoHabitacion.MAGAZIN) {
            return stsenaTorgovets(room);
        }
        if (room.tip == Komnata.TipoHabitacion.BOSS && room.boss != null && room.boss.isZhiv()) {
            return stsenaBoss(room);
        }
        if (room.estVragi()) {
            return stsenaVragi(room);
        }
        return stenaPustaya(room);
    }

    /**
     * Crea la escena de inicio.
     */
    private static String[] stsenaStart(Komnata room) {
        String[] art = {
                "",
                "",
                Ansi.colorear(Ansi.GREEN, "       +================================================================+"),
                Ansi.colorear(Ansi.GREEN, "       |                                                              |"),
                Ansi.colorear(Ansi.GREEN, "       |                    COMIENZO DEL CAMINO                       |"),
                Ansi.colorear(Ansi.YELLOW, "       |                  Explora la mazmorra                         |"),
                Ansi.colorear(Ansi.YELLOW, "       |                  y derrota al dragón                         |"),
                Ansi.colorear(Ansi.GREEN, "       |                                                              |"),
                Ansi.colorear(Ansi.GREEN, "       +================================================================+"),
                "",
                ""
        };
        return ramkaKomnaty(art, room.vyhodi, null, null);
    }

    /**
     * Crea la escena de salida.
     */
    private static String[] stsenaVyhod(Komnata room) {
        String[] art = {
                "",
                Ansi.colorear(Ansi.CYAN, "                         /                         "),
                Ansi.colorear(Ansi.CYAN, "                        /     SIGUIENTE PISO       "),
                Ansi.colorear(Ansi.CYAN, "                       /____________________________"),
                ""
        };
        return ramkaKomnaty(art, room.vyhodi, "ESCALERA — [F] bajar", null);
    }

    /**
     * Crea la escena del comerciante.
     */
    private static String[] stsenaTorgovets(Komnata room) {
        String[] art = {
                "",
                Ansi.colorear(Ansi.YELLOW, "       +================================================================+"),
                Ansi.colorear(Ansi.YELLOW, "       |                         MERCADER                              |"),
                Ansi.colorear(Ansi.YELLOW, "       |            Compra armas, armaduras y pociones                       |"),
                Ansi.colorear(Ansi.YELLOW, "       |                         $ $ $ $                               |"),
                Ansi.colorear(Ansi.YELLOW, "       +================================================================+"),
                ""
        };
        return ramkaKomnaty(art, room.vyhodi, "MERCADER — [F] abrir tienda", null);
    }

    /**
     * Crea la escena del jefe.
     */
    private static String[] stsenaBoss(Komnata room) {
        Boss boss = room.boss;
        String[] art = SpraityVragov.bossArt(boss.getTipoBoss());
        String title = Ansi.negrita(Ansi.RED, "BOSS: " + boss.getImya().toUpperCase());
        String hpText = Ansi.colorear(Ansi.RED, "HP ") + Ansi.negrita(Ansi.WHITE, boss.getHp() + "/" + boss.getMaxHp());
        return ramkaKomnaty(art, room.vyhodi, title, hpText);
    }

    /**
     * Crea la escena con enemigos.
     */
    private static String[] stsenaVragi(Komnata room) {
        Vrag enemy = room.perviyVrag();

        if (enemy == null) {
            return stenaPustaya(room);
        }

        int aliveEnemies = schitVragovZhivyh(room);
        String[] art = SpraityVragov.enemyArt(enemy.getTipo(), enemy.getCvet());
        String title = Ansi.negrita(enemy.getCvet(), enemy.getImya().toUpperCase());

        if (aliveEnemies > 1) {
            title += Ansi.colorear(Ansi.RED, "   [+" + (aliveEnemies - 1) + " enemigos]");
        }

        String hpText = Ansi.colorear(Ansi.RED, "HP ") + Ansi.negrita(Ansi.WHITE, enemy.getHp() + "/" + enemy.getMaxHp());
        return ramkaKomnaty(art, room.vyhodi, title, hpText);
    }

    /**
     * Crea una escena sin eventos.
     */
    private static String[] stenaPustaya(Komnata room) {
        boolean hasLoot = !room.lut.isEmpty() && !room.razgrabena;

        if (hasLoot) {
            String[] art = {
                    "",
                    Ansi.colorear(Ansi.YELLOW, "       +================================================================+"),
                    Ansi.colorear(Ansi.YELLOW, "       |                                                              |"),
                    Ansi.colorear(Ansi.YELLOW, "       |                 ** TESORO ENCONTRADO **                      |"),
                    Ansi.colorear(Ansi.YELLOW, "       |              Pulsa [F] para recogerlo                        |"),
                    Ansi.colorear(Ansi.YELLOW, "       |                                                              |"),
                    Ansi.colorear(Ansi.YELLOW, "       +================================================================+"),
                    ""
            };
            return ramkaKomnaty(art, room.vyhodi, "Hay objetos aquí", null);
        }

        String[] art = {
                "",
                Ansi.colorear(Ansi.GRAY, "   .        .        .        .        .        .        .        .   "),
                "",
                Ansi.colorear(Ansi.GRAY, "                        Habitación vacía...                           "),
                "",
                Ansi.colorear(Ansi.GRAY, "   .        .        .        .        .        .        .        .   "),
                ""
        };
        return ramkaKomnaty(art, room.vyhodi, null, null);
    }

    /**
     * Añade marco y puertas a la escena.
     */
    private static String[] ramkaKomnaty(String[] art, boolean[] exits, String title, String subtitle) {
        int innerWidth = SCENE_W - 2;
        ArrayList<String> lines = new ArrayList<>();

        lines.add(topBorder(innerWidth, exits[0]));

        for (String line : art) {
            lines.add(strokaSCentrom(line, innerWidth));
        }

        lines.add(bottomBorder(innerWidth, exits[2]));
        lines.add(sideDoors(innerWidth, exits[3], exits[1]));

        if (title != null) {
            lines.add(Ansi.negrita(Ansi.WHITE, "  " + title));
        }
        if (subtitle != null) {
            lines.add("  " + subtitle);
        }
        lines.add("");

        return lines.toArray(new String[0]);
    }

    /**
     * Crea el borde superior.
     */
    private static String topBorder(int width, boolean hasNorthExit) {
        if (!hasNorthExit) {
            return Ansi.colorear(Ansi.GRAY, "+" + povtorit('═', width) + "+");
        }

        int half = width / 2;
        return Ansi.colorear(Ansi.GRAY, "+" + povtorit('=', half - 3))
                + Ansi.negrita(Ansi.CYAN, "=[W]=")
                + Ansi.colorear(Ansi.GRAY, povtorit('=', width - half - 2) + "+");
    }

    /**
     * Crea el borde inferior.
     */
    private static String bottomBorder(int width, boolean hasSouthExit) {
        if (!hasSouthExit) {
            return Ansi.colorear(Ansi.GRAY, "|" + povtorit('_', width) + "|");
        }

        int half = width / 2;
        return Ansi.colorear(Ansi.GRAY, "|" + povtorit('_', half - 3))
                + Ansi.negrita(Ansi.CYAN, "_[S]_")
                + Ansi.colorear(Ansi.GRAY, povtorit('_', width - half - 2) + "|");
    }

    /**
     * Crea los laterales con puertas.
     */
    private static String sideDoors(int width, boolean hasWestExit, boolean hasEastExit) {
        String west = Ansi.colorear(Ansi.GRAY, "   ");
        String east = Ansi.colorear(Ansi.GRAY, "   ");

        if (hasWestExit) {
            west = Ansi.negrita(Ansi.CYAN, "[A]");
        }
        if (hasEastExit) {
            east = Ansi.negrita(Ansi.CYAN, "[D]");
        }

        return west + Ansi.colorear(Ansi.GRAY, "+" + povtorit('=', width - 6) + "+") + east;
    }

    /**
     * Centra una línea de texto.
     */
    private static String strokaSCentrom(String line, int width) {
        int rawWidth = Ansi.limpiar(line).length();
        int emptySpace = width - rawWidth;

        if (emptySpace < 0) {
            emptySpace = 0;
        }

        int leftPadding = emptySpace / 2;
        int rightPadding = emptySpace - leftPadding;

        return Ansi.colorear(Ansi.GRAY, "|")
                + povtorit(' ', leftPadding)
                + line
                + povtorit(' ', rightPadding)
                + Ansi.colorear(Ansi.GRAY, "|");
    }

    /**
     * Construye el minimapa del piso.
     */
    private static String[] buildMiniMap(Etaj floor, Komnata current) {
        Komnata[][] grid = floor.getGrid();
        ArrayList<String> lines = new ArrayList<>();

        lines.add(Ansi.negrita(Ansi.YELLOW, "+" + povtorit('═', MAP_W) + "+"));
        lines.add(Ansi.negrita(Ansi.YELLOW, "|") + tsentr("  MAPA  ", MAP_W) + Ansi.negrita(Ansi.YELLOW, "|"));
        lines.add(strokaNapravleniya('W'));

        for (int y = 0; y < Etaj.MAP_H; y++) {
            lines.add(buildMapRoomRow(grid, current, y));

            if (y < Etaj.MAP_H - 1) {
                lines.add(buildMapVerticalConnections(grid, y));
            }
        }

        lines.add(strokaNapravleniya('S'));
        addMapLegend(lines);
        addMapControls(lines);
        lines.add(Ansi.negrita(Ansi.YELLOW, "+" + povtorit('═', MAP_W) + "+"));

        return lines.toArray(new String[0]);
    }

    /**
     * Crea una fila de habitaciones del mapa.
     */
    private static String buildMapRoomRow(Komnata[][] grid, Komnata current, int y) {
        StringBuilder row = new StringBuilder();

        for (int x = 0; x < Etaj.MAP_W; x++) {
            Komnata room = getRoom(grid, y, x);
            Komnata nextRoom = getRoom(grid, y, x + 1);

            row.append(mapCell(room, current));

            if (x < Etaj.MAP_W - 1) {
                row.append(hasHorizontalConnection(room, nextRoom) ? "===" : "   ");
            }
        }

        return strokaMapsKlavishami(row.toString(), y);
    }

    /**
     * Crea conexiones verticales del mapa.
     */
    private static String buildMapVerticalConnections(Komnata[][] grid, int y) {
        StringBuilder row = new StringBuilder();

        for (int x = 0; x < Etaj.MAP_W; x++) {
            Komnata room = getRoom(grid, y, x);
            Komnata below = getRoom(grid, y + 1, x);
            row.append(hasVerticalConnection(room, below) ? " |  " : "    ");

            if (x < Etaj.MAP_W - 1) {
                row.append("   ");
            }
        }

        return strokaMapy(row.toString());
    }

    /**
     * Devuelve el símbolo de una habitación.
     */
    private static String mapCell(Komnata room, Komnata current) {
        if (room == null || !room.otkrita) {
            return "    ";
        }
        return "[" + roomEmoji(room, room.id == current.id) + "]";
    }

    /**
     * Comprueba conexión horizontal.
     */
    private static boolean hasHorizontalConnection(Komnata room, Komnata nextRoom) {
        return room != null && nextRoom != null && room.otkrita && nextRoom.otkrita && room.vyhodi[1];
    }

    /**
     * Comprueba conexión vertical.
     */
    private static boolean hasVerticalConnection(Komnata room, Komnata below) {
        return room != null && below != null && room.otkrita && below.otkrita && room.vyhodi[2];
    }

    /**
     * Añade ayuda de controles al minimapa.
     */
    private static String strokaMapsKlavishami(String row, int y) {
        String content = "  " + row + otstavMapy(row) + "  ";

        if (y == Etaj.MAP_H / 2) {
            return Ansi.negrita(Ansi.CYAN, "A") + content + Ansi.negrita(Ansi.CYAN, "D");
        }
        return Ansi.colorear(Ansi.WHITE, "|") + content + Ansi.colorear(Ansi.WHITE, "|");
    }

    /**
     * Formatea una línea del minimapa.
     */
    private static String strokaMapy(String row) {
        return Ansi.colorear(Ansi.WHITE, "|") + "  " + row + otstavMapy(row) + "  " + Ansi.colorear(Ansi.WHITE, "|");
    }

    /**
     * Ajusta el ancho visual del mapa.
     */
    private static String otstavMapy(String row) {
        int width = vizShirina(Ansi.limpiar(row));
        int spaces = MAP_W - 4 - width;

        if (spaces < 0) {
            spaces = 0;
        }

        return povtorit(' ', spaces);
    }

    /**
     * Muestra una tecla de movimiento.
     */
    private static String strokaNapravleniya(char direction) {
        int middle = MAP_W / 2;
        return Ansi.colorear(Ansi.YELLOW, "+")
                + Ansi.colorear(Ansi.YELLOW, povtorit('-', middle - 1))
                + Ansi.negrita(Ansi.CYAN, String.valueOf(direction))
                + Ansi.colorear(Ansi.YELLOW, povtorit('-', MAP_W - middle))
                + Ansi.colorear(Ansi.YELLOW, "+");
    }

    /**
     * Añade la leyenda del minimapa.
     */
    private static void addMapLegend(ArrayList<String> lines) {
        lines.add(strokaLegendi("[𖨆 ]tú  ", "[🏠]inicio", "[🌀]salida"));
        lines.add(strokaLegendi("[😈]jefe ", "[🏪]tienda", "[🎁]tesoro"));
        lines.add(strokaLegendi("[🟥]enemigos", "[⬜]libre", "[❓]sin vis"));
        lines.add(Ansi.colorear(Ansi.YELLOW, "+" + povtorit('-', MAP_W) + "+"));
    }

    /**
     * Añade los controles del minimapa.
     */
    private static void addMapControls(ArrayList<String> lines) {
        lines.add(strokaUpravleniya("[F]", "Ataque / Acción  ", Ansi.GREEN));
        lines.add(strokaUpravleniya("[Q]", "Inventario         ", Ansi.YELLOW));
        lines.add(strokaUpravleniya("[B]", "Terminar partida   ", Ansi.GRAY));
    }

    /**
     * Elige el icono de una habitación.
     */
    private static String roomEmoji(Komnata room, boolean isCurrent) {
        if (isCurrent) {
            return "𖨆 ";
        }
        if (!room.poseschena) {
            return "❓";
        }
        if (room.tip == Komnata.TipoHabitacion.START) {
            return "🏠";
        }
        if (room.tip == Komnata.TipoHabitacion.VYHOD) {
            return "🌀";
        }
        if (room.tip == Komnata.TipoHabitacion.MAGAZIN) {
            return "🏪";
        }
        if (room.tip == Komnata.TipoHabitacion.KLAD) {
            if (room.razgrabena) {
                return "⬜";
            }
            return "🎁";
        }
        if (room.tip == Komnata.TipoHabitacion.BOSS) {
            if (room.boss == null || !room.boss.isZhiv()) {
                return "💀";
            }
            return "😈";
        }
        if (room.ochischena || !room.estVragi()) {
            return "⬜";
        }
        return "🟥";
    }

    /**
     * Obtiene una habitación si existe.
     */
    private static Komnata getRoom(Komnata[][] grid, int y, int x) {
        if (y < 0 || y >= grid.length) {
            return null;
        }
        if (x < 0 || x >= grid[y].length) {
            return null;
        }
        return grid[y][x];
    }

    /**
     * Formatea una línea de leyenda.
     */
    private static String strokaLegendi(String first, String second, String third) {
        String content = first + "  " + second + "  " + third;
        return "|" + content + povtorit(' ', Math.max(0, MAP_W - vizShirina(content))) + "|";
    }

    /**
     * Formatea un control de teclado.
     */
    private static String strokaUpravleniya(String key, String label, String color) {
        String content = "  " + Ansi.negrita(color, key) + " " + label;
        int width = vizShirina(Ansi.limpiar(content));
        return "|" + content + povtorit(' ', Math.max(0, MAP_W - width)) + "|";
    }

    /**
     * Muestra vida, oro y equipo del jugador.
     */
    private static void printHud(Igrok player) {
        Inventar inventory = player.getInventar();

        Oruzhie weapon = inventory.getOruzhie();
        Bronya helmet = inventory.getKaska();
        Bronya chest = inventory.getTelo();
        Bronya legs = inventory.getNogi();

        pechatZheltuyuLiniyu('═');
        System.out.println(Ansi.negrita(player.getCvet(), "  " + player.getKlass().simvol + "  "
                + player.getImya().toUpperCase() + "   " + player.getKlass().nazvanie));
        pechatSeruyuLiniyu('─');
        System.out.println(Ansi.negrita(Ansi.RED, "  HP ") + Ansi.negrita(Ansi.WHITE, player.getHp() + "/" + player.getMaxHp()));
        System.out.println(Ansi.negrita(Ansi.YELLOW, "  DAÑO " + player.ataka())
                + Ansi.negrita(Ansi.CYAN, "   ARMADURA " + player.getDef())
                + Ansi.negrita(Ansi.YELLOW, "   ORO " + player.getOro())
                + Ansi.negrita(Ansi.RED, "   BAJAS " + player.getUbitye()));
        System.out.println(Ansi.colorear(Ansi.GRAY, "  Arma: ") + itemName(weapon)
                + Ansi.colorear(Ansi.GRAY, "   Cabeza: ") + itemName(helmet)
                + Ansi.colorear(Ansi.GRAY, "   Torso: ") + itemName(chest)
                + Ansi.colorear(Ansi.GRAY, "   Piernas: ") + itemName(legs));
        pechatZheltuyuLiniyu('═');
    }

    /**
     * Muestra el equipo equipado.
     */
    private static void printSnaryazhenie(Inventar inventory) {
        System.out.println(Ansi.negrita(Ansi.WHITE, "\n  EQUIPO"));
        pechatSeruyuLiniyu('─');
        printOruzhie(inventory.getOruzhie());
        printArmorLine("Cabeza", inventory.getKaska());
        printArmorLine("Torso", inventory.getTelo());
        printArmorLine("Piernas", inventory.getNogi());
    }

    /**
     * Imprime el arma equipada.
     */
    private static void printOruzhie(Oruzhie weapon) {
        System.out.println(Ansi.colorear(Ansi.RED, "  Arma     : ") + weaponText(weapon));
    }

    /**
     * Imprime una pieza de armadura.
     */
    private static void printArmorLine(String label, Bronya armor) {
        System.out.println(Ansi.colorear(Ansi.CYAN, "  " + label + vyravnivat(label) + ": ") + armorText(armor));
    }

    /**
     * Alinea una etiqueta de texto.
     */
    private static String vyravnivat(String label) {
        int spaces = 8 - label.length();
        if (spaces < 0) {
            spaces = 1;
        }
        return povtorit(' ', spaces);
    }

    /**
     * Devuelve el texto del arma.
     */
    private static String weaponText(Oruzhie weapon) {
        if (weapon == null) {
            return Ansi.colorear(Ansi.GRAY, "ninguna");
        }
        return weapon.getColoredName() + Ansi.colorear(Ansi.GRAY, "  [" + weapon.getUron() + " ATK]");
    }

    /**
     * Devuelve el texto de la armadura.
     */
    private static String armorText(Bronya armor) {
        if (armor == null) {
            return Ansi.colorear(Ansi.GRAY, "ninguna");
        }
        return armor.getColoredName() + Ansi.colorear(Ansi.GRAY, "  [" + armor.getSlot().getNazvanie()
                + " | " + armor.getRedkost() + " | " + armor.getZaschita() + " DEF]");
    }

    /**
     * Separa equipo y pociones.
     */
    private static void separateItems(ArrayList<Predmet> allItems, ArrayList<Predmet> gear, ArrayList<Predmet> potions) {
        for (Predmet item : allItems) {
            if (item instanceof Zele) {
                potions.add(item);
            } else {
                gear.add(item);
            }
        }
    }

    /**
     * Imprime una lista de objetos.
     */
    private static void printItemList(String title, ArrayList<Predmet> itemsToPrint, ArrayList<Predmet> originalItems, boolean potions) {
        System.out.println(Ansi.negrita(Ansi.WHITE, "\n  " + title + " (" + itemsToPrint.size() + ")"));
        pechatSeruyuLiniyu('─');

        if (itemsToPrint.isEmpty()) {
            if (potions) {
                System.out.println(Ansi.colorear(Ansi.GRAY, "  Sin pociones"));
            } else {
                System.out.println(Ansi.colorear(Ansi.GRAY, "  Vacío"));
            }
            return;
        }

        for (Predmet item : itemsToPrint) {
            printPredmet(item, originalItems, potions);
        }
    }

    /**
     * Imprime un objeto del inventario.
     */
    private static void printPredmet(Predmet item, ArrayList<Predmet> originalItems, boolean potion) {
        int index = originalItems.indexOf(item) + 1;
        String indexColor = potion ? Ansi.GREEN : Ansi.WHITE;

        String text = Ansi.negrita(indexColor, "  [" + index + "]  ")
                + item.getColoredName();

        if (potion) {
            text += Ansi.colorear(Ansi.GRAY, "  — " + item.getOpisanie());
        } else {
            text += itemExtraInfo(item);
            text += Ansi.colorear(Ansi.GRAY, "  — vender: " + (item.getTsena() / 2) + "g");
        }

        System.out.println(text);
    }

    /**
     * Añade información extra del objeto.
     */
    private static String itemExtraInfo(Predmet item) {
        if (item instanceof Bronya) {
            Bronya armor = (Bronya) item;
            return Ansi.colorear(Ansi.GRAY, "  [" + armor.getSlot().getNazvanie()
                    + " | " + armor.getRedkost() + " | " + armor.getZaschita() + " DEF]");
        }
        if (item instanceof Oruzhie) {
            Oruzhie weapon = (Oruzhie) item;
            return Ansi.colorear(Ansi.GRAY, "  [" + weapon.getUron() + " ATK]");
        }
        return "";
    }

    /**
     * Devuelve el nombre visible del objeto.
     */
    private static String itemName(Predmet item) {
        if (item == null) {
            return "ninguna";
        }
        return item.getNazvanie();
    }

    /**
     * Imprime el resumen final.
     */
    private static void pechatItog(Igrok player, boolean victory) {
        System.out.println("  Nombre : " + player.getImya());
        System.out.println("  Clase  : " + player.getKlass().nazvanie);
        System.out.println("  Piso   : " + player.getEtaj());
        System.out.println("  Bajas  : " + player.getUbitye());
        System.out.println("  Oro    : " + player.getOro());

        if (victory) {
            System.out.println("  Resultado: Victoria");
        } else {
            System.out.println("  Resultado: Derrota");
        }
    }

    /**
     * Muestra los últimos mensajes.
     */
    private static void pechatLog(ArrayList<String> log) {
        int firstLine = log.size() - 5;
        if (firstLine < 0) {
            firstLine = 0;
        }

        for (int i = firstLine; i < log.size(); i++) {
            System.out.println("  " + log.get(i));
        }
    }

    /**
     * Imprime escena y mapa en paralelo.
     */
    private static void pechatRyadom(String[] left, String[] right) {
        int rows = Math.max(left.length, right.length);

        for (int i = 0; i < rows; i++) {
            String leftLine = "";
            String rightLine = "";

            if (i < left.length) {
                leftLine = left[i];
            }
            if (i < right.length) {
                rightLine = right[i];
            }

            int padding = SCENE_W - Ansi.limpiar(leftLine).length();
            if (padding < 0) {
                padding = 0;
            }

            System.out.println(leftLine + povtorit(' ', padding) + " " + rightLine);
        }
    }

    /**
     * Cuenta enemigos vivos.
     */
    private static int schitVragovZhivyh(Komnata room) {
        int count = 0;

        for (Vrag enemy : room.vragi) {
            if (enemy.isZhiv()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Imprime una línea amarilla.
     */
    private static void pechatZheltuyuLiniyu(char character) {
        System.out.println(Ansi.colorear(Ansi.YELLOW, povtorit(character, TOTAL_W)));
    }

    /**
     * Imprime una línea gris.
     */
    private static void pechatSeruyuLiniyu(char character) {
        System.out.println(Ansi.colorear(Ansi.GRAY, povtorit(character, TOTAL_W)));
    }

    /**
     * Centra texto según el ancho.
     */
    private static String tsentr(String text, int width) {
        int textWidth = vizShirina(Ansi.limpiar(text));
        int padding = width - textWidth;

        if (padding < 0) {
            padding = 0;
        }

        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;
        return povtorit(' ', leftPadding) + text + povtorit(' ', rightPadding);
    }

    /**
     * Calcula el ancho visual del texto.
     */
    private static int vizShirina(String text) {
        int width = 0;

        for (int i = 0; i < text.length(); ) {
            int codePoint = text.codePointAt(i);

            if (shirokiySimvol(codePoint)) {
                width += 2;
            } else {
                width += 1;
            }

            i += Character.charCount(codePoint);
        }

        return width;
    }

    /**
     * Detecta caracteres anchos.
     */
    private static boolean shirokiySimvol(int codePoint) {
        return (codePoint >= 0x1F300 && codePoint <= 0x1FFFF)
                || (codePoint >= 0x2600 && codePoint <= 0x27FF)
                || (codePoint >= 0x2B00 && codePoint <= 0x2BFF);
    }

    /**
     * Repite un carácter.
     */
    private static String povtorit(char character, int count) {
        if (count <= 0) {
            return "";
        }
        return String.valueOf(character).repeat(count);
    }

    /**
     * Limpia la consola.
     */
    public static void ochistEkran() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }
}
