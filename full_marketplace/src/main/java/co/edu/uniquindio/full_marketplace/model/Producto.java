package co.edu.uniquindio.full_marketplace.model;

public class Producto {
    private String nombre;
    private String imagen;
    private String categoria;
    private double precio;
    private EstadoProducto estado;
    private Vendedor vendedor;

    public Producto(String nombre, String imagen, String categoria, double precio, Vendedor vendedor) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
        this.precio = precio;
        this.estado = EstadoProducto.PUBLICADO;
        this.vendedor = vendedor;
    }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public EstadoProducto getEstado() { return estado; }
    public void setEstado(EstadoProducto estado) { this.estado = estado; }
    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }
}