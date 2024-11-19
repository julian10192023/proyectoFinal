package co.edu.uniquindio.full_marketplace.viewcontroller;

import co.edu.uniquindio.full_marketplace.model.Administrador;
import co.edu.uniquindio.full_marketplace.model.Usuario;
import co.edu.uniquindio.full_marketplace.model.Vendedor;
import co.edu.uniquindio.full_marketplace.singleton.ServicioMarketPlace;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class MainViewController {
    @FXML private TabPane tabPane;

    private ServicioMarketPlace servicio = ServicioMarketPlace.obtenerInstancia();

    @FXML
    public void initialize() {
        try {
            Usuario usuarioActual = servicio.getUsuarioActual();

            if (usuarioActual instanceof Administrador) {
                FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/fxml/AdminTab.fxml"));
                Tab adminTab = new Tab("Administración", adminLoader.load());
                tabPane.getTabs().add(adminTab);
            } else {
                System.out.println("Iniciando MainViewController con usuario: " +
                        (usuarioActual != null ? usuarioActual.getUsername() : "null"));

                if (usuarioActual instanceof Vendedor) {
                    Vendedor vendedorActual = (Vendedor) usuarioActual;


                    System.out.println("Vendedor actual: " + vendedorActual.getNombre());
                    System.out.println("Cantidad de contactos: " +
                            vendedorActual.getVendedoresAliados().size());

                    // Cargar la tab del usuario actual
                    cargarTabVendedor(vendedorActual);

                    // Luego cargar las tabs de los contactos
                    for (Vendedor contacto : vendedorActual.getVendedoresAliados()) {
                        System.out.println("Cargando contacto: " + contacto.getNombre());
                        cargarTabVendedor(contacto);
                    }
                }

                // Guardar referencia al controlador en el TabPane
                tabPane.setUserData(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error cargando las pestañas: " + e.getMessage());
        }
    }

    public void cargarTabVendedor(Vendedor vendedor) throws IOException {
        if (vendedor == null) {
            System.err.println("Intento de cargar tab con vendedor null");
            return;
        }

        // Verificar si la tab ya existe
        String tabId = "tab-" + vendedor.getUsername();
        if (tabPane.getTabs().stream().anyMatch(tab -> tabId.equals(tab.getId()))) {
            System.out.println("Tab ya existe para: " + vendedor.getUsername());
            return;
        }

        System.out.println("Cargando nueva tab para: " + vendedor.getUsername());

        FXMLLoader vendedorLoader = new FXMLLoader(getClass().getResource("/fxml/VendedorTab.fxml"));
        Node vendedorContent = vendedorLoader.load();

        VendedorTabController controller = vendedorLoader.getController();
        controller.setTabPane(tabPane);
        controller.setVendedor(vendedor);

        Tab vendedorTab = new Tab(vendedor.getNombre(), vendedorContent);
        vendedorTab.setId(tabId);
        tabPane.getTabs().add(vendedorTab);
    }

    public void removerTabVendedor(String username) {
        String tabId = "tab-" + username;
        tabPane.getTabs().removeIf(tab -> tabId.equals(tab.getId()));
    }
}