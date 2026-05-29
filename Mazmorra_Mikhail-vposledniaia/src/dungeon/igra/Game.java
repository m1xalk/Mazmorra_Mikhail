package dungeon.igra;

import dungeon.dannye.Statistika;
import dungeon.personazhi.*;
import dungeon.veshchi.*;
import dungeon.ekran.*;
import dungeon.utily.Napravlenie;
import dungeon.utily.KlassGeroya;
import dungeon.karta.*;

import java.util.ArrayList;

/**
 * Controla el flujo principal de la partida.
 */
public class Game {
    private ChitatelVvoda input;

    private Igrok player;

    private Etaj floor;

    private Komnata currentRoom;

    private ArrayList<String> log;

    private boolean playing;

    private boolean victory;

    /**
     * Prepara el lector y el registro de mensajes.
     */
    public Game() {
        input = new ChitatelVvoda();
        log = new ArrayList<>();
    }

    /**
     * Abre el menú principal.
     */
    public void start() {
        boolean exit = false;
        while (!exit) {
            Otrisovka.pokazatTitulnyEkran();
            String option = input.chitateKomandu();
            if (option.equals("1")) novayaIgra();
            else if (option.equals("2")) pokazatStat();
            else if (option.equals("3")) exit = true;
        }
    }

    /**
     * Crea una partida nueva.
     */
    private void novayaIgra() {
        Otrisovka.ochistEkran();
        String name = input.sprosit("Nombre del jugador: ");
        if (name.isEmpty()) name = "Heroe";

        Otrisovka.pokazatVyborKlassa();
        int option = input.chitatInt(KlassGeroya.values().length);
        if (option == -1) option = 1;
        KlassGeroya klassGeroya = KlassGeroya.values()[option - 1];

        player = new Igrok(name, klassGeroya);
        sozdatEtaj(1);
        playing = true;
        victory = false;
        log.clear();
        log.add("Comienza la aventura.");
        igrovoyTsikl();
    }

    /**
     * Genera un piso y coloca al jugador.
     */
    private void sozdatEtaj(int number) {
        floor = new Etaj(number);
        currentRoom = floor.getStartKomnata();
        player.setEtaj(number);
    }

    /**
     * Ejecuta el bucle de juego.
     */
    private void igrovoyTsikl() {
        while (playing && player.isZhiv() && !victory) {
            Otrisovka.otrisovat(floor, currentRoom, player, log);
            String command = input.chitateKomandu();
            obrabotKomandu(command);
        }

        if (victory) {
            Otrisovka.pokazatPobeda(player);
            Statistika.sohranit(player, true);
        } else if (!player.isZhiv()) {
            Otrisovka.pokazatGameOver(player);
            Statistika.sohranit(player, false);
        }
        input.sprosit("Pulsa ENTER para volver al menú...");
    }

    /**
     * Procesa la tecla introducida.
     */
    private void obrabotKomandu(String command) {
        if (command.equals("W")) dvigatsya(Napravlenie.SEVER);
        else if (command.equals("D")) dvigatsya(Napravlenie.VOSTOK);
        else if (command.equals("S")) dvigatsya(Napravlenie.YUG);
        else if (command.equals("A")) dvigatsya(Napravlenie.ZAPAD);
        else if (command.equals("F")) deystvie();
        else if (command.equals("Q")) menuInventar();
        else if (command.equals("B")) playing = false;
        else log.add("Comando no válido.");
    }

    /**
     * Intenta mover al jugador.
     */
    private void dvigatsya(Napravlenie direction) {
        if (currentRoom.estVragi()) {
            log.add("No puedes salir mientras hay enemigos.");
            return;
        }
        Komnata next = floor.dvigatsya(currentRoom, direction);
        if (next == currentRoom) log.add("No hay salida en esa dirección.");
        else currentRoom = next;
    }

    /**
     * Ejecuta la acción de la habitación.
     */
    private void deystvie() {
        if (currentRoom.estVragi()) {
            log.clear();
            Vrag enemy = currentRoom.perviyVrag();
            Bitva.atakovat(player, enemy, log);

            if (!enemy.isZhiv()) {
                player.addUbitogo();
                player.addZoloto(enemy.getNagrada());

                ArrayList<Predmet> lut = enemy.getLoot(floor.getNomerEtaja());
                currentRoom.lut.addAll(lut);

                if (lut.isEmpty()) {
                    log.add("Botín: nada");
                } else {
                    log.add("Botín: " + nazvaniyaPredmetov(lut));
                }
            }

            if (!currentRoom.estVragi()) {
                currentRoom.ochischena = true;
            }
            return;
        }

        if (!currentRoom.lut.isEmpty() && !currentRoom.razgrabena) {
            log.clear();
            ArrayList<String> picked = new ArrayList<>();
            for (Predmet item : currentRoom.lut) {
                player.getInventar().dobavit(item);
                picked.add(item.getNazvanie());
            }
            log.add("Objetos recogidos: " + String.join(", ", picked));
            currentRoom.razgrabena = true;
            currentRoom.lut.clear();
            return;
        }

        if (currentRoom.tip == Komnata.TipoHabitacion.MAGAZIN && currentRoom.torgovets != null) {
            menuTorgovtsa();
            return;
        }

        if (currentRoom.tip == Komnata.TipoHabitacion.VYHOD) {
            if (floor.getNomerEtaja() < 5) {
                sozdatEtaj(floor.getNomerEtaja() + 1);
                log.add("Bajas al piso " + floor.getNomerEtaja() + ".");
            }
            return;
        }

        if (currentRoom.tip == Komnata.TipoHabitacion.BOSS && !currentRoom.estVragi()) {
            victory = true;
            playing = false;
            return;
        }

        log.add("No hay nada que hacer aquí.");
    }

    /**
     * Une nombres de objetos en una cadena.
     */
    private String nazvaniyaPredmetov(ArrayList<Predmet> items) {
        ArrayList<String> names = new ArrayList<>();
        for (Predmet item : items) {
            names.add(item.getNazvanie());
        }
        return String.join(", ", names);
    }


    /**
     * Usa una poción de vida.
     */
    private void drinkZele(Zele potion) {
        int healed = player.lechit(potion.getLechenie());
        player.getInventar().eliminar(potion);
        log.add("Usas " + potion.getNazvanie() + " y recuperas " + healed + " HP.");
    }

    /**
     * Permite elegir una poción.
     */
    private void vybratZele() {
        ArrayList<Predmet> items = player.getInventar().getPredmeti();
        ArrayList<Zele> potions = new ArrayList<>();

        for (Predmet item : items) {
            if (item instanceof Zele) {
                potions.add((Zele) item);
            }
        }

        if (potions.isEmpty()) {
            log.add("No tienes pociones de vida.");
            return;
        }

        System.out.println();
        System.out.println("Elige una poción para usar:");
        for (int i = 0; i < potions.size(); i++) {
            Zele potion = potions.get(i);
            System.out.println("[" + (i + 1) + "] " + potion.getNazvanie()
                    + " - " + potion.getOpisanie());
        }
        System.out.println("[0] Cancelar");

        String line = input.sprosit("Poción: ");
        try {
            int option = Integer.parseInt(line);
            if (option == 0) {
                log.add("No usas ninguna poción.");
            } else if (option >= 1 && option <= potions.size()) {
                drinkZele(potions.get(option - 1));
            } else {
                log.add("Opción de poción no válida.");
            }
        } catch (NumberFormatException e) {
            log.add("Entrada no válida al elegir poción.");
        }
    }

    /**
     * Abre el menú de inventario.
     */
    private void menuInventar() {
        boolean open = true;
        while (open) {
            Otrisovka.pokazatInventar(player);
            String command = input.chitateKomandu();
            if (command.equals("X")) open = false;
            else if (command.equals("R")) {
                vybratZele();
                open = false;
            } else if (command.equals("E")) {
                int index = input.chitatInt(player.getInventar().getPredmeti().size());
                if (index != -1) {
                    Predmet item = player.getInventar().getPredmeti().get(index - 1);
                    if (item instanceof Oruzhie) {
                        player.getInventar().equipOruzhie(index - 1);
                        log.add("Arma equipada: " + item.getNazvanie());
                    } else if (item instanceof Bronya) {
                        Bronya armor = (Bronya) item;
                        player.getInventar().equiparBronya(index - 1);
                        log.add("Armadura equipada en " + armor.getSlot().getNazvanie() + ": " + item.getNazvanie());
                    } else {
                        log.add("Ese objeto no se puede equipar.");
                    }
                }
                open = false;
            }
        }
    }

    /**
     * Abre el menú del comerciante.
     */
    private void menuTorgovtsa() {
        Torgovets merchant = currentRoom.torgovets;
        Otrisovka.ochistEkran();
        System.out.println("TIENDA - Oro: " + player.getOro());
        ArrayList<Predmet> items = merchant.getPredmeti();
        for (int i = 0; i < items.size(); i++) {
            Predmet item = items.get(i);
            System.out.println("[" + (i + 1) + "] " + item.getNazvanie() + " - " + item.getTsena() + " oro");
        }
        System.out.println("[0] Salir");
        String line = input.sprosit("Comprar: ");
        try {
            int option = Integer.parseInt(line);
            if (option > 0 && option <= items.size()) {
                if (merchant.kupit(player, option - 1)) log.add("Compra realizada.");
                else log.add("No tienes suficiente oro.");
            }
        } catch (NumberFormatException e) {
            log.add("Entrada no válida en la tienda.");
        }
    }

    /**
     * Muestra las estadísticas guardadas.
     */
    private void pokazatStat() {
        Otrisovka.ochistEkran();
        Statistika.pokazat();
        input.sprosit("Pulsa ENTER para continuar...");
    }
}
