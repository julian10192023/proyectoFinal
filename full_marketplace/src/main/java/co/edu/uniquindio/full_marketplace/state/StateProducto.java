package co.edu.uniquindio.full_marketplace.state;

import co.edu.uniquindio.full_marketplace.model.Producto;

//estado de los productos
public interface StateProducto {
    void publicar(Producto producto);
    void vender(Producto producto);
    void cancelar(Producto producto);
}