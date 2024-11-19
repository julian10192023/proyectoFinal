package co.edu.uniquindio.full_marketplace.singleton;

import co.edu.uniquindio.full_marketplace.model.*;
import co.edu.uniquindio.full_marketplace.builder.BuilderReporte;
import co.edu.uniquindio.full_marketplace.composite.CompositeVendedores;
import co.edu.uniquindio.full_marketplace.decorator.ProductoDestacado;
import co.edu.uniquindio.full_marketplace.factory.FactoryAdministrador;
import co.edu.uniquindio.full_marketplace.factory.FactoryUsuario;
import co.edu.uniquindio.full_marketplace.factory.FactoryVendedor;
import co.edu.uniquindio.full_marketplace.observer.IPublicador;
import co.edu.uniquindio.full_marketplace.observer.ISubscriptor;
import co.edu.uniquindio.full_marketplace.proxy.ProxyAutorizacion;
import co.edu.uniquindio.full_marketplace.state.StateProducto;
import co.edu.uniquindio.full_marketplace.state.StatePublicado;
import co.edu.uniquindio.full_marketplace.strategy.StrategyAutenticacion;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
// instancia unica y aqui pusimos toda la logica de negocio
public class ServicioMarketPlace implements IPublicador {
    private static ServicioMarketPlace instancia;
    private final MarketPlace mercado;
    private StrategyAutenticacion strategyAutenticacion;
    private final List<ISubscriptor> subscriptores;
    private final ProxyAutorizacion proxyAutorizacion;
    private final Map<Producto, StateProducto> estadosProductos;
    Usuario usuarioActual = null;

    private ServicioMarketPlace() {
        this.mercado = MarketPlace.getInstance();
        this.subscriptores = new ArrayList<>();
        this.proxyAutorizacion = new ProxyAutorizacion(this);
        this.estadosProductos = new HashMap<>();
    }

    public static ServicioMarketPlace obtenerInstancia() {
        if (instancia == null) {
            instancia = new ServicioMarketPlace();
        }
        return instancia;
    }

    public MarketPlace getMarketplace() {
        return mercado;
    }

    public Usuario getUsuarioActual() {
        System.out.println("Obteniendo usuario actual: " +
                (usuarioActual != null ? usuarioActual.getUsername() : "null"));
        return this.usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        System.out.println("Usuario actual establecido: " +
                (usuario != null ? usuario.getUsername() : "null"));
    }

    // Métodos de gestión de usuarios
    public Usuario crearUsuario(FactoryUsuario factory, String nombre, String apellidos,
                                String cedula, String direccion, String nombreUsuario,
                                String contrasena) {
        List<Usuario> usuarios = mercado.getUsuarios();
        int contadorVendedores = 0;

        for (Usuario usuario : usuarios) {
            if (usuario instanceof Vendedor) {
                contadorVendedores++;
            }
        }

        if (contadorVendedores >= 10 && factory instanceof FactoryVendedor) {
            throw new IllegalStateException("Se alcanzó el número máximo de vendedores (10)");
        }

        Usuario nuevoUsuario = factory.crearUsuario(nombre, apellidos, cedula, direccion,
                nombreUsuario, contrasena);
        usuarios.add(nuevoUsuario);
        return nuevoUsuario;
    }

    // Métodos de gestión de red social
    public void agregarAliadoVendedor(Vendedor vendedor1, Vendedor vendedor2) {
        List<Vendedor> aliados = vendedor1.getVendedoresAliados();
        boolean yaEsAliado = false;

        for (Vendedor aliado : aliados) {
            if (aliado.equals(vendedor2)) {
                yaEsAliado = true;
                break;
            }
        }

        if (!yaEsAliado && aliados.size() < 10) {
            aliados.add(vendedor2);
            vendedor2.getVendedoresAliados().add(vendedor1);
            notificarObservers("Nueva alianza creada entre " + vendedor1.getNombre() +
                    " y " + vendedor2.getNombre());
        }
    }

    // Métodos para el Patrón Composite
    public CompositeVendedores crearGrupoVendedores(String nombreGrupo) {
        return new CompositeVendedores(nombreGrupo);
    }

    public void agregarVendedorAGrupo(CompositeVendedores grupo, Vendedor vendedor) {
        grupo.agregarVendedor(vendedor);
        notificarObservers("Vendedor " + vendedor.getNombre() + " agregado al grupo " + grupo.getNombreGrupo());
    }

    // Métodos para el Patrón Decorator
    public Producto decorarProductoDestacado(Producto producto) {
        return new ProductoDestacado(producto);
    }

    public boolean esProductoDestacado(Producto producto) {
        return producto instanceof ProductoDestacado;
    }

    // Métodos para el Patrón Factory
    public Usuario crearAdministrador(String nombre, String apellidos, String cedula,
                                      String direccion, String nombreUsuario, String contrasena) {
        FactoryUsuario factory = new FactoryAdministrador();
        Usuario usuario = factory.crearUsuario(nombre, apellidos, cedula, direccion, nombreUsuario, contrasena);
        mercado.getUsuarios().add(usuario);
        return usuario;
    }

    public Usuario crearVendedor(String nombre, String apellidos, String cedula,
                                 String direccion, String nombreUsuario, String contrasena) {
        FactoryUsuario factory = new FactoryVendedor();
        Usuario usuario = crearUsuario(factory, nombre, apellidos, cedula, direccion, nombreUsuario, contrasena);
        mercado.getUsuarios().add(usuario);
        return usuario;
    }

    // Métodos para el Patrón Observer
    @Override
    public void registrarObserver(ISubscriptor subscriptor) {
        subscriptores.add(subscriptor);
    }

    @Override
    public void eliminarObserver(ISubscriptor subscriptor) {
        subscriptores.remove(subscriptor);
    }

    @Override
    public void notificarObservers(String mensaje) {
        for (ISubscriptor subscriptor : subscriptores) {
            subscriptor.actualizar(mensaje);
        }
    }

    // Métodos para el Patrón Proxy
    public boolean verificarAutorizacion(Usuario usuario, Producto producto, String operacion) {
        return proxyAutorizacion.puedeModificarProducto(usuario, producto);
    }

    // Métodos para el Patrón State
    public void cambiarEstadoProducto(Producto producto, EstadoProducto nuevoEstado) {
        StateProducto estadoActual = estadosProductos.getOrDefault(producto, new StatePublicado());

        switch (nuevoEstado) {
            case PUBLICADO:
                estadoActual.publicar(producto);
                break;
            case VENDIDO:
                estadoActual.vender(producto);
                break;
            case CANCELADO:
                estadoActual.cancelar(producto);
                break;
        }

        estadosProductos.put(producto, estadoActual);
        notificarObservers("Producto " + producto.getNombre() + " cambió a estado: " + nuevoEstado);
    }

    // Métodos para el Patrón Strategy
    public void establecerEstrategiaAutenticacion(StrategyAutenticacion estrategia) {
        this.strategyAutenticacion = estrategia;
    }

    public Usuario autenticarUsuario(String nombreUsuario, String contrasena) {
        if (strategyAutenticacion == null) {
            throw new IllegalStateException("Estrategia de autenticación no establecida");
        }

        Usuario usuario = strategyAutenticacion.autenticar(nombreUsuario, contrasena);
        if (usuario != null) {
            this.usuarioActual = usuario;  // Establece el usuario actual aquí
            System.out.println("Usuario autenticado y establecido: " + usuario.getUsername());
        }
        return usuario;
    }

    // Métodos adicionales de negocio
    public List<Producto> obtenerProductosVendedor(Vendedor vendedor) {
        return vendedor.getProductos();
    }

    public List<Producto> obtenerProductosAliados(Vendedor vendedor) {
        List<Producto> productosAliados = new ArrayList<>();
        for (Vendedor aliado : vendedor.getVendedoresAliados()) {
            productosAliados.addAll(aliado.getProductos());
        }
        return productosAliados;
    }

    public void publicarComentario(Vendedor autor, Producto producto, String contenido) {
        if (!puedeComentarProducto(autor, producto)) {
            throw new IllegalStateException("No tiene permiso para comentar este producto");
        }

        Comentario comentario = new Comentario(contenido, autor);
        for (Publicacion pub : mercado.getPublicaciones()) {
            if (pub.getProducto().equals(producto)) {
                pub.getComentarios().add(comentario);
                notificarObservers("Nuevo comentario en producto: " + producto.getNombre());
                break;
            }
        }
    }

    private boolean puedeComentarProducto(Vendedor autor, Producto producto) {
        return autor.getVendedoresAliados().contains(producto.getVendedor());
    }

    public void darMeGusta(Vendedor vendedor, Producto producto) {
        if (!vendedor.getProductosLiked().contains(producto)) {
            vendedor.getProductosLiked().add(producto);
            notificarObservers(vendedor.getNombre() + " dio Me gusta a: " + producto.getNombre());
        }
    }

    public void quitarMeGusta(Vendedor vendedor, Producto producto) {
        if (vendedor.getProductosLiked().remove(producto)) {
            notificarObservers(vendedor.getNombre() + " quitó su Me gusta de: " + producto.getNombre());
        }
    }

    // Métodos de estadísticas
    public int contarMensajesEntreVendedores(Vendedor vendedor1, Vendedor vendedor2) {
        int contador = 0;
        for (Muro muro : mercado.getMuros()) {
            if (muro.getMensaje().getAutor().equals(vendedor1) &&
                    vendedor2.getVendedoresAliados().contains(vendedor1)) {
                contador++;
            }
        }
        return contador;
    }

    public List<Producto> obtenerProductosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return mercado.getPublicaciones().stream()
                .filter(pub -> pub.getFechaPublicacion().isAfter(inicio) &&
                        pub.getFechaPublicacion().isBefore(fin))
                .map(Publicacion::getProducto)
                .collect(Collectors.toList());
    }

    public List<Producto> obtenerTopProductosMeGusta(int limite) {
        Map<Producto, Long> conteoLikes = new HashMap<>();

        for (Usuario usuario : mercado.getUsuarios()) {
            for (Producto producto : usuario.getProductosLiked()) {
                conteoLikes.merge(producto, 1L, Long::sum);
            }
        }

        return conteoLikes.entrySet().stream()
                .sorted(Map.Entry.<Producto, Long>comparingByValue().reversed())
                .limit(limite)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Métodos de estadísticas
    public void exportarEstadisticas(String rutaArchivo, String titulo, String nombreUsuario,
                                     String contenido) {
        BuilderReporte builder = new BuilderReporte()
                .conTitulo(titulo)
                .conUsuario(nombreUsuario)
                .conContenido(contenido);

        String reporte = builder.construir();

        try (PrintWriter escritor = new PrintWriter(new FileWriter(rutaArchivo))) {
            escritor.println(reporte);
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar estadísticas", e);
        }
    }
}