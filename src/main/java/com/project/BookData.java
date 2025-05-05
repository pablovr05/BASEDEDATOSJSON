package com.project;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import org.json.*;

public class BookData {

    static final String RUTA_FAVORITOS = "data/favoritos.json";
    private static final String RUTA_LISTAS = "data/listas.json";
    private static final String RUTA_HISTORIAL = "data/historial.json";

    // ------------------------------ LISTAS ------------------------------

    public static void crearLista(String nombre, String descripcion) {
        JSONObject listas = cargarJson(RUTA_LISTAS);
        if (listas.has(nombre)) {
            System.out.println(">> Ya existe una lista con ese nombre.");
            return;
        }

        JSONObject nuevaLista = new JSONObject();
        nuevaLista.put("fecha_creacion", LocalDate.now().toString());
        nuevaLista.put("descripcion", descripcion);
        nuevaLista.put("libros", new JSONArray());

        listas.put(nombre, nuevaLista);
        guardarJson(RUTA_LISTAS, listas);
    }

    public static void añadirLibroALista(String nombreLista, Libro libro) {
        JSONObject listas = cargarJson(RUTA_LISTAS);
        if (!listas.has(nombreLista)) {
            System.out.println(">> La lista no existe.");
            return;
        }

        JSONObject lista = listas.getJSONObject(nombreLista);
        JSONArray libros = lista.getJSONArray("libros");
        libros.put(libroToJson(libro));

        guardarJson(RUTA_LISTAS, listas);
    }

    public static void eliminarLista(String nombre) {
        JSONObject listas = cargarJson(RUTA_LISTAS);
        listas.remove(nombre);
        guardarJson(RUTA_LISTAS, listas);
    }

    public static void cambiarNombreLista(String actual, String nuevo) {
        JSONObject listas = cargarJson(RUTA_LISTAS);
        if (!listas.has(actual)) return;

        JSONObject datos = listas.getJSONObject(actual);
        listas.remove(actual);
        listas.put(nuevo, datos);
        guardarJson(RUTA_LISTAS, listas);
    }

    public static void cambiarDescripcionLista(String nombre, String nuevaDescripcion) {
        JSONObject listas = cargarJson(RUTA_LISTAS);
        if (!listas.has(nombre)) return;

        listas.getJSONObject(nombre).put("descripcion", nuevaDescripcion);
        guardarJson(RUTA_LISTAS, listas);
    }

    // ------------------------------ HISTORIAL ------------------------------

    

    public static void añadirLibroAEstado(String estado, Libro libro) {
        JSONObject historial = cargarJson(RUTA_HISTORIAL);
        if (!historial.has(estado)) historial.put(estado, new JSONArray());
        historial.getJSONArray(estado).put(libroToJson(libro));
        guardarJson(RUTA_HISTORIAL, historial);
    }

    public static void moverLibroDeEstado(String de, String a, int index) {
        JSONObject historial = cargarJson(RUTA_HISTORIAL);
        JSONArray origen = historial.optJSONArray(de);
        JSONArray destino = historial.optJSONArray(a);
        if (origen == null || destino == null || index < 0 || index >= origen.length()) return;

        JSONObject libro = origen.getJSONObject(index);
        destino.put(libro);
        origen.remove(index);

        guardarJson(RUTA_HISTORIAL, historial);
    }

    public static void borrarHistorial() {
        JSONObject vacio = new JSONObject();
        vacio.put("por_leer", new JSONArray());
        vacio.put("leyendo", new JSONArray());
        vacio.put("leido", new JSONArray());
        guardarJson(RUTA_HISTORIAL, vacio);
    }

    public static JSONObject obtenerHistorial() {
        return cargarJson(RUTA_HISTORIAL);
    }    

    // ------------------------------ FAVORITOS ------------------------------

    public static void añadirAFavoritos(Libro libro, String comentario, int calificacion) {
        JSONArray favoritos = cargarJsonArray(RUTA_FAVORITOS);
        JSONObject entrada = libroToJson(libro);
        entrada.put("comentario", comentario);
        entrada.put("calificacion", calificacion);
        favoritos.put(entrada);
        guardarJson(RUTA_FAVORITOS, favoritos);
    }

    public static void visualizarFavoritos() {
        JSONArray favoritos = cargarJsonArray(RUTA_FAVORITOS);

        if (favoritos.length() == 0) {
            System.out.println("No tienes libros favoritos aún.");
            return;
        }

        System.out.println("Tus libros favoritos son:");
        for (int i = 0; i < favoritos.length(); i++) {
            JSONObject libro = favoritos.getJSONObject(i);
            System.out.println("\n" + (i + 1) + ". " + libro.getString("titulo"));
            System.out.println("   Autor(es): " + libro.getJSONArray("autores").join(", "));
            System.out.println("   Descripción: " + libro.getString("descripcion"));
            System.out.println("   Comentario: " + libro.getString("comentario"));
            System.out.println("   Calificación: " + libro.getInt("calificacion"));
        }
    }

    // ------------------------------ UTILS ------------------------------

    private static JSONObject cargarJson(String ruta) {
        try {
            String contenido = Files.readString(Path.of(ruta));
            return new JSONObject(contenido);
        } catch (IOException e) {
            return new JSONObject(); // Si no existe, retornamos vacío
        }
    }

    static JSONArray cargarJsonArray(String ruta) {
        try {
            String contenido = Files.readString(Path.of(ruta));
            return new JSONArray(contenido);
        } catch (IOException e) {
            return new JSONArray(); // Si no existe, retornamos vacío
        }
    }

    private static void guardarJson(String ruta, JSONObject json) {
        try {
            Files.writeString(Path.of(ruta), json.toString(4));
        } catch (IOException e) {
            System.out.println("Error guardando JSON: " + e.getMessage());
        }
    }

    private static void guardarJson(String ruta, JSONArray jsonArray) {
        try {
            Files.writeString(Path.of(ruta), jsonArray.toString(4));
        } catch (IOException e) {
            System.out.println("Error guardando JSON: " + e.getMessage());
        }
    }

    private static JSONObject libroToJson(Libro libro) {
        JSONObject obj = new JSONObject();
        obj.put("titulo", libro.getTitulo());
        obj.put("autores", new JSONArray(libro.getAutores()));
        obj.put("descripcion", libro.getDescripcion());
        return obj;
    }

    
}
