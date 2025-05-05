package com.project;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            clearConsole();
            mostrarMenu();
            System.out.print(CYAN + "Seleccione una opción: " + RESET);
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    clearConsole();
                    buscarLibro();
                    break;
                case "2":
                    menuListas();
                    break;
                case "3":
                    historialLibros();
                    break;
                case "4":
                    gestionFavoritos();
                    break;
                case "5":
                    System.out.println(CYAN + "\nGracias por usar BookManager. ¡Hasta la próxima!\n" + RESET);
                    salir = true;
                    break;
                default:
                    System.out.println(YELLOW + ">> Opción inválida. Presione Enter para continuar..." + RESET);
                    scanner.nextLine();
            }
        }

        scanner.close();
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public static void mostrarMenu() {
        System.out.println(CYAN + "=======================================" + RESET);
        System.out.println(BOLD + GREEN + "          GESTOR DE LIBROS" + RESET);
        System.out.println(CYAN + "=======================================" + RESET);
        System.out.println("  " + YELLOW + "1. Buscar un libro" + RESET);
        System.out.println("  " + YELLOW + "2. Menú de listas" + RESET);
        System.out.println("  " + YELLOW + "3. Historial de libros" + RESET);
        System.out.println("  " + YELLOW + "4. Gestión de favoritos" + RESET);
        System.out.println("  " + YELLOW + "5. Salir" + RESET);
        System.out.println(CYAN + "=======================================" + RESET + "\n");
    }

    public static void buscarLibro() {
        Scanner scanner = new Scanner(System.in);
        boolean volver = false;

        while (!volver) {
            clearConsole();
            System.out.println(CYAN + "=======================================" + RESET);
            System.out.println(BOLD + BLUE + "         BUSCAR UN LIBRO" + RESET);
            System.out.println(CYAN + "=======================================" + RESET);
            System.out.println("  " + YELLOW + "1. Buscar por título" + RESET);
            System.out.println("  " + YELLOW + "2. Volver al menú principal" + RESET);
            System.out.println(CYAN + "=======================================" + RESET);
            System.out.print(CYAN + "Seleccione una opción: " + RESET);
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print(GREEN + "Ingrese el título: " + RESET);
                    String titulo = scanner.nextLine();
                    List<Libro> resultados = ApiBook.buscarLibroPorTitulo(titulo);
                    seleccionarLibro(resultados);
                    break;
                case "2":
                    volver = true;
                    break;
                default:
                    System.out.println(YELLOW + ">> Opción inválida. Presione Enter para continuar..." + RESET);
                    scanner.nextLine();
            }
        }
    }

    public static void seleccionarLibro(List<Libro> libros) {
        Scanner scanner = new Scanner(System.in);

        if (libros.isEmpty()) {
            System.out.println(YELLOW + ">> No se encontraron libros. Presione Enter para volver..." + RESET);
            scanner.nextLine();
            return;
        }

        clearConsole();
        System.out.println(CYAN + "\nResultados encontrados:" + RESET);
        for (int i = 0; i < libros.size(); i++) {
            System.out.println("  " + YELLOW + (i + 1) + ". " + RESET + libros.get(i).toStringResumen());
        }

        System.out.print(CYAN + "\nIngrese el número del libro para seleccionar o 0 para volver: " + RESET);
        String entrada = scanner.nextLine();

        try {
            int opcion = Integer.parseInt(entrada);
            if (opcion == 0) return;
            if (opcion < 1 || opcion > libros.size()) {
                System.out.println(YELLOW + ">> Número inválido. Presione Enter para continuar..." + RESET);
                scanner.nextLine();
                return;
            }

            Libro libroSeleccionado = libros.get(opcion - 1);
            mostrarSubmenuLibro(libroSeleccionado);

        } catch (NumberFormatException e) {
            System.out.println(YELLOW + ">> Entrada no válida. Presione Enter para continuar..." + RESET);
            scanner.nextLine();
        }
    }

    public static void mostrarSubmenuLibro(Libro libro) {
        Scanner scanner = new Scanner(System.in);
        boolean volver = false;

        while (!volver) {
            clearConsole();
            System.out.println(CYAN + "Libro seleccionado:\n" + BOLD + libro.toStringResumen() + RESET);
            System.out.println("  " + YELLOW + "1. Añadir a favoritos" + RESET);
            System.out.println("  " + YELLOW + "2. Añadir a una lista personalizada" + RESET);
            System.out.println("  " + YELLOW + "3. Marcar como leído" + RESET);
            System.out.println("  " + YELLOW + "4. Volver a buscar otro libro" + RESET);
            System.out.println("  " + YELLOW + "5. Volver al menú principal" + RESET);
            System.out.print(CYAN + "Seleccione una opción: " + RESET);
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print(GREEN + "Comentario: " + RESET);
                    String comentario = scanner.nextLine();
                    System.out.print(GREEN + "Calificación (0-10): " + RESET);
                    int calificacion = Integer.parseInt(scanner.nextLine());
                    BookData.añadirAFavoritos(libro, comentario, calificacion);
                    System.out.println(GREEN + ">> Libro añadido a favoritos." + RESET);
                    pausa();
                    break;
                case "2":
                    System.out.print(GREEN + "Nombre de la lista: " + RESET);
                    String lista = scanner.nextLine();
                    BookData.añadirLibroALista(lista, libro);
                    System.out.println(GREEN + ">> Libro añadido a la lista \"" + lista + "\"." + RESET);
                    pausa();
                    break;
                case "3":
                    BookData.añadirLibroAEstado("leido", libro);
                    System.out.println(GREEN + ">> Libro marcado como leído." + RESET);
                    pausa();
                    break;
                case "4":
                    buscarLibro();
                    volver = true;
                    break;
                case "5":
                    volver = true;
                    break;
                default:
                    System.out.println(YELLOW + ">> Opción inválida. Presione Enter para continuar..." + RESET);
                    scanner.nextLine();
            }
        }
    }

    public static void menuListas() {
        Scanner scanner = new Scanner(System.in);
        boolean volver = false;

        while (!volver) {
            clearConsole();
            System.out.println(CYAN + "======= MENÚ DE LISTAS =======" + RESET);
            System.out.println("  " + YELLOW + "1. Crear nueva lista" + RESET);
            System.out.println("  " + YELLOW + "2. Eliminar lista" + RESET);
            System.out.println("  " + YELLOW + "3. Cambiar nombre de lista" + RESET);
            System.out.println("  " + YELLOW + "4. Cambiar descripción de lista" + RESET);
            System.out.println("  " + YELLOW + "5. Volver" + RESET);
            System.out.print(CYAN + "Seleccione una opción: " + RESET);
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print(GREEN + "Nombre de la lista: " + RESET);
                    String nombre = scanner.nextLine();
                    System.out.print(GREEN + "Descripción: " + RESET);
                    String descripcion = scanner.nextLine();
                    BookData.crearLista(nombre, descripcion);
                    System.out.println(GREEN + ">> Lista creada." + RESET);
                    pausa();
                    break;
                case "2":
                    System.out.print(GREEN + "Nombre de la lista a eliminar: " + RESET);
                    BookData.eliminarLista(scanner.nextLine());
                    System.out.println(GREEN + ">> Lista eliminada." + RESET);
                    pausa();
                    break;
                case "3":
                    System.out.print(GREEN + "Nombre actual de la lista: " + RESET);
                    String actual = scanner.nextLine();
                    System.out.print(GREEN + "Nuevo nombre: " + RESET);
                    String nuevo = scanner.nextLine();
                    BookData.cambiarNombreLista(actual, nuevo);
                    System.out.println(GREEN + ">> Nombre cambiado." + RESET);
                    pausa();
                    break;
                case "4":
                    System.out.print(GREEN + "Nombre de la lista: " + RESET);
                    String lista = scanner.nextLine();
                    System.out.print(GREEN + "Nueva descripción: " + RESET);
                    BookData.cambiarDescripcionLista(lista, scanner.nextLine());
                    System.out.println(GREEN + ">> Descripción actualizada." + RESET);
                    pausa();
                    break;
                case "5":
                    volver = true;
                    break;
                default:
                    System.out.println(YELLOW + ">> Opción inválida." + RESET);
                    pausa();
            }
        }
    }

    public static void historialLibros() {
        Scanner scanner = new Scanner(System.in);
        boolean volver = false;
    
        while (!volver) {
            clearConsole();
            System.out.println(CYAN + "======= HISTORIAL DE LECTURA =======" + RESET);
            System.out.println("  " + YELLOW + "1. Ver historial" + RESET);
            System.out.println("  " + YELLOW + "2. Mover libro de estado" + RESET);
            System.out.println("  " + YELLOW + "3. Borrar historial" + RESET);
            System.out.println("  " + YELLOW + "4. Volver" + RESET);
            System.out.print(CYAN + "Seleccione una opción: " + RESET);
            String opcion = scanner.nextLine();
    
            switch (opcion) {
                case "1":
                    mostrarHistorial();
                    break;
                case "2":
                    moverLibroEnHistorial();
                    break;
                case "3":
                    BookData.borrarHistorial();
                    System.out.println(GREEN + ">> Historial borrado." + RESET);
                    pausa();
                    break;
                case "4":
                    volver = true;
                    break;
                default:
                    System.out.println(YELLOW + ">> Opción inválida." + RESET);
                    pausa();
            }
        }
    }

    public static void mostrarHistorial() {
        JSONObject historial = BookData.obtenerHistorial();

        System.out.println(CYAN + "\n======= HISTORIAL COMPLETO =======" + RESET);
        for (String estado : Arrays.asList("por_leer", "leyendo", "leido")) {
            System.out.println("\n" + YELLOW + estado.toUpperCase().replace("_", " ") + ":" + RESET);
            JSONArray libros = historial.optJSONArray(estado);
            if (libros == null || libros.length() == 0) {
                System.out.println("  (sin libros)");
            } else {
                for (int i = 0; i < libros.length(); i++) {
                    JSONObject libro = libros.getJSONObject(i);
                    System.out.println("  " + (i + 1) + ". " + libro.getString("titulo"));
                }
            }
        }

        pausa();
    }

    public static void moverLibroEnHistorial() {
        Scanner scanner = new Scanner(System.in);
        JSONObject historial = BookData.obtenerHistorial();
    
        System.out.println(YELLOW + "Estados disponibles: por_leer, leyendo, leido" + RESET);
        System.out.print(GREEN + "Estado origen: " + RESET);
        String origen = scanner.nextLine().trim();
    
        if (!historial.has(origen)) {
            System.out.println(YELLOW + ">> Estado inválido." + RESET);
            pausa();
            return;
        }
    
        JSONArray libros = historial.getJSONArray(origen);
        if (libros.length() == 0) {
            System.out.println(YELLOW + ">> No hay libros en este estado." + RESET);
            pausa();
            return;
        }
    
        System.out.println(CYAN + "\nLibros en '" + origen + "':" + RESET);
        for (int i = 0; i < libros.length(); i++) {
            JSONObject libro = libros.getJSONObject(i);
            System.out.println("  " + (i + 1) + ". " + libro.getString("titulo"));
        }
    
        System.out.print(GREEN + "Seleccione el número del libro a mover: " + RESET);
        int index;
        try {
            index = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println(YELLOW + ">> Entrada inválida." + RESET);
            pausa();
            return;
        }
    
        if (index < 0 || index >= libros.length()) {
            System.out.println(YELLOW + ">> Índice fuera de rango." + RESET);
            pausa();
            return;
        }
    
        System.out.print(GREEN + "Estado destino: " + RESET);
        String destino = scanner.nextLine().trim();
    
        if (!historial.has(destino)) {
            System.out.println(YELLOW + ">> Estado destino inválido." + RESET);
            pausa();
            return;
        }
    
        BookData.moverLibroDeEstado(origen, destino, index);
        System.out.println(GREEN + ">> Libro movido exitosamente." + RESET);
        pausa();
    }
    

    

    public static void gestionFavoritos() {
        Scanner scanner = new Scanner(System.in);
        clearConsole();
        System.out.println(CYAN + "=====================================" + RESET);
        System.out.println(BOLD + BLUE + "        FAVORTIOS" + RESET);
        System.out.println(CYAN + "=====================================" + RESET);

        // Cargar los favoritos
        JSONArray favoritos = BookData.cargarJsonArray(BookData.RUTA_FAVORITOS);

        if (favoritos.length() == 0) {
            System.out.println(YELLOW + ">> No tienes libros favoritos." + RESET);
        } else {
            // Mostrar favoritos
            for (int i = 0; i < favoritos.length(); i++) {
                JSONObject libro = favoritos.getJSONObject(i);
                String titulo = libro.getString("titulo");
                JSONArray autores = libro.getJSONArray("autores");
                String comentario = libro.getString("comentario");
                int calificacion = libro.getInt("calificacion");

                System.out.println("\n" + BOLD + GREEN + (i + 1) + ". " + RESET + titulo);
                System.out.println("  " + YELLOW + "Autores: " + RESET + autores.join(", "));
                System.out.println("  " + YELLOW + "Comentario: " + RESET + comentario);
                System.out.println("  " + YELLOW + "Calificación: " + RESET + calificacion);
            }
        }

        pausa();
    }


    public static void pausa() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(CYAN + "\nPresione Enter para continuar..." + RESET);
        scanner.nextLine();
    }
}
