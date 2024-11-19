package co.edu.uniquindio.full_marketplace.decorator;

import co.edu.uniquindio.full_marketplace.model.Producto;

import java.time.LocalDateTime;


public class ProductoDestacado extends DecoratorProducto {
    private LocalDateTime fechaDestacado;

    public ProductoDestacado(Producto producto) {
        super(producto);
        this.fechaDestacado = LocalDateTime.now();
    }
}