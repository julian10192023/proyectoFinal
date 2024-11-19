package co.edu.uniquindio.full_marketplace.state;

import co.edu.uniquindio.full_marketplace.model.EstadoProducto;
import co.edu.uniquindio.full_marketplace.model.Producto;

//estado de los productos
public class StatePublicado implements StateProducto {
    @Override
    public void publicar(Producto producto) {
        // ya se publico
    }

    @Override
    public void vender(Producto producto) {
        producto.setEstado(EstadoProducto.VENDIDO);
    }

    @Override
    public void cancelar(Producto producto) {
        producto.setEstado(EstadoProducto.CANCELADO);
    }
}