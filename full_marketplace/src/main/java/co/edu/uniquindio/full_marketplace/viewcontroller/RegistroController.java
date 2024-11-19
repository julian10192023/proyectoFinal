package co.edu.uniquindio.full_marketplace.viewcontroller;

import co.edu.uniquindio.full_marketplace.singleton.ServicioMarketPlace;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import co.edu.uniquindio.full_marketplace.model.*;

public class RegistroController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCedula;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private CheckBox chkIsAdmin;
    @FXML private Label lblError;

    private ServicioMarketPlace servicio = ServicioMarketPlace.obtenerInstancia();

    @FXML
    protected void onRegistrar() {
        try {
            // Validar campos
            if (camposVacios()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String cedula = txtCedula.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText();
            boolean esAdmin = chkIsAdmin.isSelected();

            // Crear usuario según el tipo seleccionado
            Usuario nuevoUsuario;
            if (esAdmin) {
                nuevoUsuario = servicio.crearAdministrador(
                        nombre, apellidos, cedula, direccion, username, password);
            } else {
                nuevoUsuario = servicio.crearVendedor(
                        nombre, apellidos, cedula, direccion, username, password);
            }

            // Si el registro fue exitoso, mostrar mensaje y redirigir al login
            mostrarMensajeExito();
            irALogin();

        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al registrar usuario: " + e.getMessage());
        }
    }

    private boolean camposVacios() {
        return txtNombre.getText().trim().isEmpty() ||
                txtApellidos.getText().trim().isEmpty() ||
                txtCedula.getText().trim().isEmpty() ||
                txtDireccion.getText().trim().isEmpty() ||
                txtUsername.getText().trim().isEmpty() ||
                txtPassword.getText().isEmpty();
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
    }

    private void mostrarMensajeExito() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro Exitoso");
        alert.setHeaderText(null);
        alert.setContentText("Usuario registrado correctamente");
        alert.showAndWait();
    }

    @FXML
    protected void onCancelar() {
        irALogin();
    }

    private void irALogin() {
        try {
            //                FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/fxml/AdminTab.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            mostrarError("Error al cargar la vista de login: " + e.getMessage());
        }
    }
}