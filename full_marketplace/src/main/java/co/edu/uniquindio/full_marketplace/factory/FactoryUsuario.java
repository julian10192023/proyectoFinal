package co.edu.uniquindio.full_marketplace.factory;

import co.edu.uniquindio.full_marketplace.model.Usuario;


public interface FactoryUsuario {
    Usuario crearUsuario(String nombre,
                         String apellidos,
                         String cedula,
                         String direccion,
                         String nombreUsuario,
                         String contrasena);
}