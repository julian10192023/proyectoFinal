package co.edu.uniquindio.full_marketplace.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    private String nombre;
    private String apellidos;
    private String cedula;
    private String direccion;
    private String username;
    private String password;
    private List<Producto> productosLiked;

    public Usuario(String nombre, String apellidos, String cedula, String direccion, String username, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.direccion = direccion;
        this.username = username;
        this.password = password;
        this.productosLiked = new ArrayList<>();
    }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Producto> getProductosLiked() { return productosLiked; }
    public void setProductosLiked(List<Producto> productosLiked) { this.productosLiked = productosLiked; }
}
