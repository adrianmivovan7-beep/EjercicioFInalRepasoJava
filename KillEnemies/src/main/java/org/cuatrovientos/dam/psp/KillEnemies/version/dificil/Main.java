package org.cuatrovientos.dam.psp.KillEnemies.version.dificil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {

            Scanner teclado = new Scanner(System.in);

            Hero hero;
            ArrayList<Character> personajes;

            if (new File("partida.ser").exists()) {

                Object[] partida = cargarPartida();

                hero = (Hero) partida[0];
                personajes = (ArrayList<Character>) partida[1];

            } else {

                String nombre = pedirNombre(teclado);

                hero = new Hero(nombre);

                personajes = crearPersonajes();

                Collections.shuffle(personajes);
            }

            mostrarContadores(personajes);
            showCharacters(personajes);

            int opcion;

            do {

                if (!hayEnemigos(personajes)) {

                    System.out.println();
                    System.out.println("¡ENHORABUENA " + hero.getNombre() + "!");
                    System.out.println("¡Has conseguido derrotar a todos los enemigos!");

                    borrarPartida();

                    break;
                }

                opcion = pedirOpcion(teclado);

                if (opcion == 3) {

                    guardarPartida(hero, personajes);

                    System.out.println("Partida guardada. ¡Hasta pronto!");

                    break;
                }

                if (personajes.size() == 0) {
                    break;
                }

                int indice = pedirIndice(teclado, personajes);

                realizarAccion(hero, personajes, opcion, indice);

                guardarPartida(hero, personajes);

                System.out.println();
                showCharacters(personajes);

            } while (opcion != 3);

            System.out.println();
            hero.mostrarEstadisticas();

            teclado.close();

        } catch (Exception e) {

            System.out.println();
            System.out.println("Ha ocurrido un error inesperado.");
            System.out.println("El programa no puede continuar.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String pedirNombre(Scanner teclado) {
        System.out.print("Introduce el nombre de tu héroe: ");
        return teclado.nextLine();
    }

    public static ArrayList<Character> crearPersonajes() {

        ArrayList<Character> personajes = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            personajes.add(new Friend());
        }

        for (int i = 0; i < 5; i++) {
            personajes.add(new Enemy());
        }

        return personajes;
    }

    public static void mostrarContadores(ArrayList<Character> personajes) {

        int amigos = 0;
        int enemigos = 0;

        for (Character personaje : personajes) {

            if (personaje.isEnemy()) {
                enemigos++;
            } else {
                amigos++;
            }
        }

        System.out.println();
        System.out.println("Número de amigos: " + amigos);
        System.out.println("Número de enemigos: " + enemigos);
    }

    public static void showCharacters(ArrayList<Character> personajes) {

        System.out.println();
        System.out.println("----- PERSONAJES -----");

        for (int i = 0; i < personajes.size(); i++) {

            Character personaje = personajes.get(i);

            if (personaje.isEnemy()) {
                System.out.println("Índice " + i + ": Enemigo");
            } else {
                System.out.println("Índice " + i + ": Amigo");
            }
        }
    }

    public static int pedirOpcion(Scanner teclado) {

        int opcion = 0;

        while (opcion != 1 && opcion != 2 && opcion != 3) {

            System.out.println();
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Atacar");
            System.out.println("2. Defender");
            System.out.println("3. Salir");
            System.out.print("Introduce una opción: ");

            try {

                opcion = teclado.nextInt();

                if (opcion != 1 && opcion != 2 && opcion != 3) {

                    System.out.println(
                        "Opción no válida. Escribe 1 para atacar, 2 para defender o 3 para salir."
                    );
                }

            } catch (Exception e) {

                System.out.println(
                    "Entrada no válida. Por favor, introduce un número (1, 2 o 3)."
                );

                teclado.nextLine();
            }
        }

        return opcion;
    }

    public static int pedirIndice(Scanner teclado,
                                  ArrayList<Character> personajes) {

        int indice = -1;

        while (indice < 0 || indice >= personajes.size()) {

            System.out.print("Introduce el índice del personaje: ");

            try {

                indice = teclado.nextInt();

                if (indice < 0 || indice >= personajes.size()) {

                    System.out.println(
                        "Ese índice no existe. Elige un índice de la lista."
                    );
                }

            } catch (Exception e) {

                System.out.println(
                    "Entrada no válida. Por favor, introduce un número."
                );

                teclado.nextLine();
            }
        }

        return indice;
    }

    public static void realizarAccion(Hero hero,
                                      ArrayList<Character> personajes,
                                      int opcion,
                                      int indice) {

        Character personaje = personajes.get(indice);

        if (opcion == 1) {

            if (personaje.isEnemy()) {

                hero.attack((Enemy) personaje);

            } else {

                System.out.println("¡Has atacado a un amigo!");
            }

            personajes.remove(indice);

        } else if (opcion == 2) {

            if (personaje.isEnemy()) {

                System.out.println("¡Has defendido a un enemigo!");

                personajes.add(personaje);

            } else {

                hero.defend((Friend) personaje);
            }
        }
    }

    public static boolean hayEnemigos(ArrayList<Character> personajes) {

        for (Character personaje : personajes) {

            if (personaje.isEnemy()) {
                return true;
            }
        }

        return false;
    }

    public static void guardarPartida(Hero hero,
                                      ArrayList<Character> personajes) {

        try {

            ObjectOutputStream salida = new ObjectOutputStream(
                new FileOutputStream("partida.ser")
            );

            salida.writeObject(hero);
            salida.writeObject(personajes);

            salida.close();

            System.out.println("Partida guardada correctamente.");

        } catch (Exception e) {

            System.out.println("No se ha podido guardar la partida.");
        }
    }

    public static Object[] cargarPartida() {

        try {

            ObjectInputStream entrada = new ObjectInputStream(
                new FileInputStream("partida.ser")
            );

            Hero hero = (Hero) entrada.readObject();

            ArrayList<Character> personajes =
                (ArrayList<Character>) entrada.readObject();

            entrada.close();

            System.out.println("Partida cargada correctamente.");

            return new Object[]{hero, personajes};

        } catch (Exception e) {

            System.out.println("No se ha podido cargar la partida.");

            return null;
        }
    }

    public static void borrarPartida() {

        File archivo = new File("partida.ser");

        if (archivo.exists()) {

            archivo.delete();

            System.out.println("La partida guardada ha sido eliminada.");
        }
    }
}