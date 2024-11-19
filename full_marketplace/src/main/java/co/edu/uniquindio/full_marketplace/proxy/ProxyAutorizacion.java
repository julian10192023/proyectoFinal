package co.edu.uniquindio.full_marketplace.proxy;

import co.edu.uniquindio.full_marketplace.model.Administrador;
import co.edu.uniquindio.full_marketplace.model.Producto;
import co.edu.uniquindio.full_marketplace.model.Usuario;
import co.edu.uniquindio.full_marketplace.model.Vendedor;
import co.edu.uniquindio.full_marketplace.singleton.ServicioMarketPlace;

// para poner mensajes en publicaciones que no sean las nuestras
public class ProxyAutorizacion {
    private ServicioMarketPlace servicio;

    public ProxyAutorizacion(ServicioMarketPlace servicio) {
        this.servicio = servicio;
    }

    public boolean puedeModificarProducto(Usuario usuario, Producto producto) {
        if (usuario instanceof Administrador) return true;
        if (usuario instanceof Vendedor) {
            return producto.getVendedor().equals(usuario);
        }
        return false;
    }
}