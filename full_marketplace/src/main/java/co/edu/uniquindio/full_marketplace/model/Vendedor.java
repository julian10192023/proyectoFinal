package co.edu.uniquindio.full_marketplace.model;

import java.util.ArrayList;
import java.util.List;

public class Vendedor extends Usuario {
    private List<Muro> muros;
    private List<Producto> productos;
    private List<Vendedor> vendedoresAliados;

    public Vendedor(String nombre, String apellidos, String cedula, String direccion, String username, String password) {
        super(nombre, apellidos, cedula, direccion, username, password);
        this.muros = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.vendedoresAliados = new ArrayList<>();
    }


    public List<Muro> getMuros() { return muros; }
    public void setMuros(List<Muro> muros) { this.muros = muros; }
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
    public List<Vendedor> getVendedoresAliados() { return vendedoresAliados; }
    public void setVendedoresAliados(List<Vendedor> vendedoresAliados) { this.vendedoresAliados = vendedoresAliados; }
}