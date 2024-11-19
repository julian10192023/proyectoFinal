package co.edu.uniquindio.full_marketplace.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarketPlace {
    private static MarketPlace instance;
    private List<Usuario> usuarios;
    private List<Producto> productos;
    private List<Publicacion> publicaciones;
    private List<Comentario> comentarios;
    private List<Muro> muros;
    private Usuario usuarioActual; // Agregar esta variable

    private MarketPlace() {
        this.usuarios = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.publicaciones = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.muros = new ArrayList<>();
        initializeDemoData();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public static MarketPlace getInstance() {
        if (instance == null) {
            instance = new MarketPlace();
        }
        return instance;
    }

    private void initializeDemoData() {

        Vendedor vendedor1 = new Vendedor("Juan", "Perez", "1234567", "Calle 1", "juan", "perez");
        Vendedor vendedor2 = new Vendedor("Maria", "Lopez", "7654321", "Calle 2", "maria", "lopez");
        Administrador administrador1 = new Administrador("Jose", "Obrador", "79845614", "Calle 3", "admin", "admin");


        Producto producto1 = new Producto("Portatil", "laptop.jpg", "Tecnología", 1200.0, vendedor1);
        Producto producto2 = new Producto("Celular", "phone.jpg", "Tecnología", 800.0, vendedor1);
        Producto producto3 = new Producto("Tablet", "tablet.jpg", "Tecnología", 500.0, vendedor2);
        Producto producto4 = new Producto("tennis", "", "Deporte", 500.0, vendedor2);


        vendedor1.getProductos().add(producto1);
        vendedor1.getProductos().add(producto2);
        vendedor2.getProductos().add(producto3);
        vendedor2.getProductos().add(producto4);


        Mensaje mensaje1 = new Mensaje("Hola este es mi muro espero los productos sean de tu agrado", vendedor1);
        Mensaje mensaje2 = new Mensaje("Hola a todos", vendedor2);

        Muro muro1 = new Muro(mensaje1);
        Muro muro2 = new Muro(mensaje2);


        vendedor1.getMuros().add(muro1);
        vendedor2.getMuros().add(muro2);


        Publicacion pub1 = new Publicacion(producto1);
        Publicacion pub2 = new Publicacion(producto2);
        Publicacion pub3 = new Publicacion(producto3);
        Publicacion pub4 = new Publicacion(producto4);


        muro1.getPublicaciones().add(pub1);
        muro1.getPublicaciones().add(pub2);
        muro2.getPublicaciones().add(pub3);
        muro2.getPublicaciones().add(pub4);


        Comentario comment1 = new Comentario("Buen producto", vendedor2);
        Comentario comment2 = new Comentario("Me interesa", vendedor1);


        pub1.getComentarios().add(comment1);
        pub3.getComentarios().add(comment2);



        vendedor1.getVendedoresAliados().add(vendedor2);
        vendedor2.getVendedoresAliados().add(vendedor1);


        vendedor1.getProductosLiked().add(producto3);
        vendedor2.getProductosLiked().add(producto1);


        usuarios.add(vendedor1);
        usuarios.add(vendedor2);
        usuarios.add(administrador1);
        productos.addAll(Arrays.asList(producto1, producto2, producto3));
        publicaciones.addAll(Arrays.asList(pub1, pub2, pub3));
        comentarios.addAll(Arrays.asList(comment1, comment2));
        muros.addAll(Arrays.asList(muro1, muro2));
    }


    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
    public List<Publicacion> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<Publicacion> publicaciones) { this.publicaciones = publicaciones; }
    public List<Comentario> getComentarios() { return comentarios; }
    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }
    public List<Muro> getMuros() { return muros; }
    public void setMuros(List<Muro> muros) { this.muros = muros; }
}