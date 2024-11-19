package co.edu.uniquindio.full_marketplace.model;

import java.util.ArrayList;
import java.util.List;

public class Muro {
    private Mensaje mensaje;
    private List<Publicacion> publicaciones;

    public Muro(Mensaje mensaje) {
        this.mensaje = mensaje;
        this.publicaciones = new ArrayList<>();
    }

    public Mensaje getMensaje() { return mensaje; }
    public void setMensaje(Mensaje mensaje) { this.mensaje = mensaje; }
    public List<Publicacion> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<Publicacion> publicaciones) { this.publicaciones = publicaciones; }

}