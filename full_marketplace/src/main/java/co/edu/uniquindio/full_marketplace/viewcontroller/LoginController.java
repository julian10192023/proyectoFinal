package co.edu.uniquindio.full_marketplace.viewcontroller;

import co.edu.uniquindio.full_marketplace.MainApplication;
import co.edu.uniquindio.full_marketplace.model.Usuario;
import co.edu.uniquindio.full_marketplace.singleton.ServicioMarketPlace;
import co.edu.uniquindio.full_marketplace.strategy.StrategyVendedor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    private ServicioMarketPlace servicio = ServicioMarketPlace.obtenerInstancia();

    @FXML
    public void initialize() {
        // Establecer la estrategia de autenticación por defecto
        servicio.establecerEstrategiaAutenticacion(new StrategyVendedor());
    }

    @FXML
    void onLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            Usuario usuarioAutenticado = servicio.autenticarUsuario(username, password);
            if (usuarioAutenticado != null) {
                try {
                    // El usuario ya está establecido en servicio.autenticarUsuario
                    System.out.println("Cargando MainView para usuario: " + usuarioAutenticado.getUsername());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
                    Scene scene = new Scene(loader.load(), 1024, 368);
                    Stage stage = (Stage) txtUsername.getScene().getWindow();

                    stage.setTitle("MarketPlace Social - " + usuarioAutenticado.getNombre());
                    stage.setScene(scene);
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarError("Error al cargar la vista principal: " + e.getMessage());
                }
            } else {
                mostrarError("Credenciales inválidas");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error en la autenticación: " + e.getMessage());
        }
    }

    @FXML
    void onRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Registro.fxml"));
            Scene scene = new Scene(loader.load(), 300, 400);
            Stage stage = MainApplication.getPrimaryStage();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar la vista de registro");
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}