package co.edu.uniquindio.full_marketplace.strategy;

import co.edu.uniquindio.full_marketplace.model.MarketPlace;
import co.edu.uniquindio.full_marketplace.model.Usuario;
import co.edu.uniquindio.full_marketplace.model.Administrador;

//Autentificacion de usuario
public class StrategyAdministrador implements StrategyAutenticacion {
    @Override
    public Usuario autenticar(String nombreUsuario, String contrasena) {
        MarketPlace mercado = MarketPlace.getInstance();
        for (Usuario usuario : mercado.getUsuarios()) {
            if (usuario instanceof Administrador &&
                    usuario.getUsername().equals(nombreUsuario) &&
                    usuario.getPassword().equals(contrasena)) {
                return usuario;
            }
        }
        return null;
    }
}