package com.project;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    // Colores para consola (ANSI escape codes)
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
                // Unix/Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            // Fallback
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
        System.out.println(CYAN + "=======================================" + RESET);
        System.out.println(BOLD + BLUE + "          BUSCAR UN LIBRO" + RESET);
        System.out.println(CYAN + "=======================================" + RESET);
        Scanner scanner = new Scanner(System.in);
        System.out.print(GREEN + "Ingrese el título del libro a buscar: " + RESET);
        String titulo = scanner.nextLine();
        ApiBook.buscarLibroPorTitulo(titulo);
        pausa();
    }
    

    public static void menuListas() {
        System.out.println(GREEN + ">> Agregar un nuevo libro (por implementar)." + RESET);
    }

    public static void historialLibros() {
        System.out.println(GREEN + ">> Eliminar un libro (por implementar)." + RESET);
    }

    public static void gestionFavoritos() {
        System.out.println(GREEN + ">> Listar todos los libros (por implementar)." + RESET);
    }

    public static void pausa() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(CYAN + "\nPresione Enter para continuar..." + RESET);
        scanner.nextLine();  // Detiene la ejecución hasta que el usuario presione Enter
    }
}
