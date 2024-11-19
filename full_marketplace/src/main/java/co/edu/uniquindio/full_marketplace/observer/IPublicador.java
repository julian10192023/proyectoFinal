package co.edu.uniquindio.full_marketplace.observer;

  //notifica por consola lo que pasa con la app
public interface IPublicador {
    void registrarObserver(ISubscriptor observer);
    void eliminarObserver(ISubscriptor observer);
    void notificarObservers(String mensaje);
}