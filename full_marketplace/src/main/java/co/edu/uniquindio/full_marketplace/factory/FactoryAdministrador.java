package co.edu.uniquindio.full_marketplace.factory;

import co.edu.uniquindio.full_marketplace.model.Administrador;
import co.edu.uniquindio.full_marketplace.model.Usuario;

// lo usamos para crear administradores y vendedores
public class FactoryAdministrador implements FactoryUsuario {
    @Override
    public Usuario crearUsuario(String nombre, String apellidos, String cedula, String direccion,
                                String nombreUsuario, String contrasena) {
        return new Administrador(nombre, apellidos, cedula, direccion, nombreUsuario, contrasena);
    }
}