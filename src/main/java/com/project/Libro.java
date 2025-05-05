package com.project;

import java.util.List;

public class Libro {
    private String titulo;
    private List<String> autores;
    private String descripcion;

    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";

    public Libro(String titulo, List<String> autores, String descripcion) {
        this.titulo = titulo;
        this.autores = autores;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<String> getAutores() {
        return autores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String toStringResumen() {
        String autoresStr = (autores != null && !autores.isEmpty()) ? String.join(", ", autores) : "Desconocido";
        String desc = (descripcion != null && !descripcion.isEmpty()) 
            ? (descripcion.length() > 200 ? descripcion.substring(0, 200) + "..." : descripcion) 
            : "Sin descripción";

        StringBuilder sb = new StringBuilder();
        sb.append(MAGENTA + "──────────────────────────────────────────────" + RESET + "\n");
        sb.append(BOLD + GREEN + "Título: " + RESET + titulo + "\n");
        sb.append(BOLD + CYAN + "Autor(es): " + RESET + autoresStr + "\n");
        sb.append(BOLD + YELLOW + "Descripción: " + RESET + desc + "\n");

        return sb.toString();
    }
}
