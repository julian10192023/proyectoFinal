package co.edu.uniquindio.full_marketplace.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Publicacion {
    private Producto producto;
    private LocalDateTime fechaPublicacion;
    private List<Comentario> comentarios;

    public Publicacion(Producto producto) {
        this.producto = producto;
        this.fechaPublicacion = LocalDateTime.now();
        this.comentarios = new ArrayList<>();
    }


    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public List<Comentario> getComentarios() { return comentarios; }
    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }
}