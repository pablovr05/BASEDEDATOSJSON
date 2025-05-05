package com.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiBook {

    public static List<Libro> buscarLibroPorTitulo(String titulo) {
        List<Libro> resultados = new ArrayList<>();

        try {
            String query = URLEncoder.encode(titulo, "UTF-8");
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query;

            URI uri = new URI(urlString);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.out.println("Error en la conexión: " + conn.getResponseCode());
                return resultados;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }

            conn.disconnect();

            JSONObject json = new JSONObject(jsonString.toString());
            JSONArray items = json.optJSONArray("items");

            if (items == null || items.length() == 0) return resultados;

            for (int i = 0; i < Math.min(items.length(), 10); i++) {
                JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

                String tituloLibro = volumeInfo.optString("title", "Sin título");
                JSONArray autoresJson = volumeInfo.optJSONArray("authors");
                List<String> autores = new ArrayList<>();
                if (autoresJson != null) {
                    for (int j = 0; j < autoresJson.length(); j++) {
                        autores.add(autoresJson.getString(j));
                    }
                }

                String descripcion = volumeInfo.optString("description", "Sin descripción");

                resultados.add(new Libro(tituloLibro, autores, descripcion));
            }

        } catch (IOException | java.net.URISyntaxException e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }

        return resultados;
    }
}
