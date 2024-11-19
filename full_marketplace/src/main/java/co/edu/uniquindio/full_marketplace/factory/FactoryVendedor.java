package co.edu.uniquindio.full_marketplace.factory;

import co.edu.uniquindio.full_marketplace.model.Usuario;
import co.edu.uniquindio.full_marketplace.model.Vendedor;


public class FactoryVendedor implements FactoryUsuario {
    @Override
    public Usuario crearUsuario(String nombre, String apellidos, String cedula, String direccion,
        String nombreUsuario, String contrasena) {
        return new Vendedor(nombre, apellidos, cedula, direccion, nombreUsuario, contrasena);
    }
}