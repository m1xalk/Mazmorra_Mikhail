package dungeon.karta;

import dungeon.personazhi.*;
import dungeon.veshchi.*;
import dungeon.utily.Napravlenie;

import java.util.ArrayList;
import java.util.Random;

/**
 * Genera y guarda un piso de la mazmorra.
 */
public class Etaj {
    public static final int MAP_W = 4;
    public static final int MAP_H = 4;
    private static final Random random = new Random();

    private int nomerEtaja;

    private Komnata[][] grid;

    private ArrayList<Komnata> rooms;

    private Komnata startRoom;

    /**
     * Crea un piso nuevo.
     */
    public Etaj(int nomerEtaja) {
        this.nomerEtaja = nomerEtaja;
        generirovat();
    }

    /**
     * Genera habitaciones conectadas.
     */
    private void generirovat() {
        grid = new Komnata[MAP_H][MAP_W];
        rooms = new ArrayList<>();

        boolean[][] used = new boolean[MAP_H][MAP_W];
        int startX = random.nextInt(MAP_W);
        int startY = random.nextInt(MAP_H);
        int targetKomnati = 7 + random.nextInt(9); // ES: 7-15 habitaciones / RU: 7-15 комнат

        ArrayList<int[]> frontier = new ArrayList<>();
        ArrayList<int[]> positions = new ArrayList<>();
        frontier.add(new int[]{startX, startY});
        positions.add(new int[]{startX, startY});
        used[startY][startX] = true;

        int[][] dirs = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        while (positions.size() < targetKomnati && !frontier.isEmpty()) {
            int index = random.nextInt(frontier.size());
            int[] current = frontier.remove(index);
            peremeshat(dirs);

            for (int[] dir : dirs) {
                int nx = current[0] + dir[0];
                int ny = current[1] + dir[1];
                if (nx >= 0 && nx < MAP_W && ny >= 0 && ny < MAP_H && !used[ny][nx]) {
                    used[ny][nx] = true;
                    frontier.add(new int[]{nx, ny});
                    positions.add(new int[]{nx, ny});
                    if (positions.size() >= targetKomnati) break;
                }
            }
        }

        for (int i = 0; i < positions.size(); i++) {
            int[] pos = positions.get(i);
            Komnata room = new Komnata(i + 1, pos[0], pos[1], Komnata.TipoHabitacion.NORMAL);
            grid[pos[1]][pos[0]] = room;
            rooms.add(room);
        }

        startRoom = rooms.get(0);
        startRoom.tip = Komnata.TipoHabitacion.START;
        startRoom.ochischena = true;

        soedinitKomnaty();
        naznachitKomnaty();
        zapolnitKomnaty();

        startRoom.otkrita = true;
        startRoom.poseschena = true;
        otkrytVokrug(startRoom);
    }

    /**
     * Mezcla las direcciones de generación.
     */
    private void peremeshat(int[][] dirs) {
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = dirs[i];
            dirs[i] = dirs[j];
            dirs[j] = temp;
        }
    }

    /**
     * Marca las salidas entre habitaciones.
     */
    private void soedinitKomnaty() {
        for (Komnata room : rooms) {
            room.vyhodi[0] = getKomnataAt(room.x, room.y - 1) != null;
            room.vyhodi[1] = getKomnataAt(room.x + 1, room.y) != null;
            room.vyhodi[2] = getKomnataAt(room.x, room.y + 1) != null;
            room.vyhodi[3] = getKomnataAt(room.x - 1, room.y) != null;
        }
    }

    /**
     * Asigna habitaciones especiales.
     */
    private void naznachitKomnaty() {
        Komnata farthest = dalshayaKomnata();
        if (nomerEtaja == 5) {
            farthest.tip = Komnata.TipoHabitacion.BOSS;
        } else {
            farthest.tip = Komnata.TipoHabitacion.VYHOD;
            farthest.ochischena = true;
        }

        ArrayList<Komnata> normalRooms = getNormalnyeKomnaty();
        if (!normalRooms.isEmpty()) vziatSluchainuyu(normalRooms).tip = Komnata.TipoHabitacion.MAGAZIN;
        if (!normalRooms.isEmpty()) vziatSluchainuyu(normalRooms).tip = Komnata.TipoHabitacion.KLAD;
        if (normalRooms.size() > 2 && random.nextInt(100) < 45) {
            vziatSluchainuyu(normalRooms).tip = Komnata.TipoHabitacion.KLAD;
        }
    }

    /**
     * Busca la habitación más lejana.
     */
    private Komnata dalshayaKomnata() {
        Komnata farthest = startRoom;
        int maxDistance = 0;
        for (Komnata room : rooms) {
            int distance = Math.abs(room.x - startRoom.x) + Math.abs(room.y - startRoom.y);
            if (distance > maxDistance) {
                maxDistance = distance;
                farthest = room;
            }
        }
        return farthest;
    }

    /**
     * Obtiene habitaciones normales.
     */
    private ArrayList<Komnata> getNormalnyeKomnaty() {
        ArrayList<Komnata> normalRooms = new ArrayList<>();
        for (Komnata room : rooms) {
            if (room.tip == Komnata.TipoHabitacion.NORMAL) normalRooms.add(room);
        }
        return normalRooms;
    }

    /**
     * Toma una habitación aleatoria.
     */
    private Komnata vziatSluchainuyu(ArrayList<Komnata> list) {
        return list.remove(random.nextInt(list.size()));
    }

    /**
     * Busca una habitación por coordenadas.
     */
    private Komnata getKomnataAt(int x, int y) {
        if (x < 0 || x >= MAP_W || y < 0 || y >= MAP_H) return null;
        return grid[y][x];
    }

    /**
     * Añade enemigos, tesoros y tienda.
     */
    private void zapolnitKomnaty() {
        for (Komnata room : rooms) {
            if (room.tip == Komnata.TipoHabitacion.NORMAL) {
                int enemies = random.nextInt(3); // ES: 0-2 vragi / RU: 0-2 врага
                for (int i = 0; i < enemies; i++) {
                    room.vragi.add(Vrag.sluchainiyVrag(nomerEtaja));
                }
                if (enemies == 0) room.ochischena = true;
            } else if (room.tip == Komnata.TipoHabitacion.KLAD) {
                room.lut.addAll(Predmety.horoshiyLut(nomerEtaja));
            } else if (room.tip == Komnata.TipoHabitacion.MAGAZIN) {
                room.torgovets = new Torgovets(nomerEtaja);
                room.ochischena = true;
            } else if (room.tip == Komnata.TipoHabitacion.BOSS) {
                room.boss = new Boss();
            }
        }
    }

    /**
     * Devuelve la habitación destino.
     */
    public Komnata dvigatsya(Komnata current, Napravlenie direction) {
        int nx = current.x;
        int ny = current.y;
        if (direction == Napravlenie.SEVER && current.vyhodi[0]) ny--;
        else if (direction == Napravlenie.VOSTOK && current.vyhodi[1]) nx++;
        else if (direction == Napravlenie.YUG && current.vyhodi[2]) ny++;
        else if (direction == Napravlenie.ZAPAD && current.vyhodi[3]) nx--;
        else return current;

        Komnata next = getKomnataAt(nx, ny);
        if (next == null) return current;
        next.otkrita = true;
        next.poseschena = true;
        otkrytVokrug(next);
        return next;
    }

    /**
     * Descubre habitaciones cercanas.
     */
    private void otkrytVokrug(Komnata room) {
        for (int y = room.y - 1; y <= room.y + 1; y++) {
            for (int x = room.x - 1; x <= room.x + 1; x++) {
                Komnata near = getKomnataAt(x, y);
                if (near != null) near.otkrita = true;
            }
        }
    }

    public int getNomerEtaja() { return nomerEtaja; }
    public Komnata[][] getGrid() { return grid; }
    public Komnata getStartKomnata() { return startRoom; }
}
