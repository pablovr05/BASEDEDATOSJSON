package com.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiBook {

    // Colores para consola (ANSI escape codes)
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    public static void buscarLibroPorTitulo(String titulo) {
        try {
            String query = URLEncoder.encode(titulo, "UTF-8");
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query;

            // Convertimos el String en un URI y luego lo convertimos a URL
            URI uri = new URI(urlString);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.out.println("Error en la conexión: " + conn.getResponseCode());
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonString = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }

            conn.disconnect();
            mostrarResultados(jsonString.toString());

        } catch (IOException | java.net.URISyntaxException e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private static void mostrarResultados(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        JSONArray items = json.optJSONArray("items");

        if (items == null || items.length() == 0) {
            System.out.println(CYAN + "No se encontraron libros con ese título." + RESET);
            return;
        }

        System.out.println(CYAN + "Resultados encontrados:\n" + RESET);

        // Mostrar hasta 10 libros, numerados
        int totalLibros = Math.min(items.length(), 10);
        for (int i = 0; i < totalLibros; i++) {
            JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

            String titulo = volumeInfo.optString("title", "Sin título");
            JSONArray autores = volumeInfo.optJSONArray("authors");
            String descripcion = volumeInfo.optString("description", "Sin descripción");

            // Formato de cada libro
            System.out.println(BOLD + (i + 1) + ". " + RESET + BLUE + "Título: " + RESET + titulo); // Numeramos y destacamos el título
            if (autores != null) {
                System.out.print(BLUE + "   Autor(es): " + RESET);
                for (int j = 0; j < autores.length(); j++) {
                    System.out.print(GREEN + autores.getString(j) + RESET);
                    if (j < autores.length() - 1) System.out.print(", ");
                }
                System.out.println();
            } else {
                System.out.println(BLUE + "   Autor(es): " + RESET + "Desconocido");
            }

            System.out.println(BLUE + "   Descripción: " + RESET + (descripcion.length() > 200 ? descripcion.substring(0, 200) + "..." : descripcion));
            System.out.println(YELLOW + "--------------------------------------------------" + RESET);
        }
    }
}
