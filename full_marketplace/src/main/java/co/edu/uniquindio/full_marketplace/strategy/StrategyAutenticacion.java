package co.edu.uniquindio.full_marketplace.strategy;

import co.edu.uniquindio.full_marketplace.model.Usuario;

//Autentificacion de usuario

public interface StrategyAutenticacion {
    Usuario autenticar(String nombreUsuario, String contrasena);
}