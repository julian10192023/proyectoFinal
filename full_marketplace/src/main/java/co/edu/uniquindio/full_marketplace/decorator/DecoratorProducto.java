package co.edu.uniquindio.full_marketplace.decorator;

import co.edu.uniquindio.full_marketplace.model.Producto;

// agregamos funcionalidades sin necesidad de modificar el objeto original
// en este caso agregamos la fecha y hora en el momento en el que se inicia la app

abstract class DecoratorProducto extends Producto {
    protected Producto productoDecorado;

    public DecoratorProducto(Producto producto) {
        super(producto.getNombre(), producto.getImagen(), producto.getCategoria(),
                producto.getPrecio(), producto.getVendedor());
        this.productoDecorado = producto;
    }
}