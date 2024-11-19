package co.edu.uniquindio.full_marketplace.model;

import java.time.LocalDateTime;

public class Comentario {
    private String contenido;
    private LocalDateTime fechaCreacion;
    private Vendedor autor;

    public Comentario(String contenido, Vendedor autor) {
        this.contenido = contenido;
        this.fechaCreacion = LocalDateTime.now();
        this.autor = autor;
    }


    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Vendedor getAutor() { return autor; }
    public void setAutor(Vendedor autor) { this.autor = autor; }
}