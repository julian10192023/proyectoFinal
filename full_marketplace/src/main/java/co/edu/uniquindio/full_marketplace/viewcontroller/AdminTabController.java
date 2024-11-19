package co.edu.uniquindio.full_marketplace.viewcontroller;

import co.edu.uniquindio.full_marketplace.model.Producto;
import co.edu.uniquindio.full_marketplace.model.Usuario;
import co.edu.uniquindio.full_marketplace.model.Vendedor;
import co.edu.uniquindio.full_marketplace.singleton.ServicioMarketPlace;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class AdminTabController {
    @FXML private TextArea txtEstadisticas;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private ComboBox<String> cboTipoReporte;

    private ServicioMarketPlace servicio = ServicioMarketPlace.obtenerInstancia();

    @FXML
    void initialize() {
        cboTipoReporte.getItems().addAll(
                "Productos por fecha",
                "Top 10 productos con más me gusta",
                "Mensajes entre vendedores"
        );
    }

    @FXML
    void onGenerarReporte() {
        String tipoReporte = cboTipoReporte.getValue();

        if (tipoReporte == null) {
            mostrarError("Por favor seleccione un tipo de reporte");
            return;
        }

        StringBuilder contenido = new StringBuilder();

        switch (tipoReporte) {
            case "Productos por fecha":
                if (dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null) {
                    mostrarError("Por favor seleccione ambas fechas (inicio y fin) para generar el reporte");
                    return;
                }

                LocalDateTime inicio = dpFechaInicio.getValue().atStartOfDay();
                LocalDateTime fin = dpFechaFin.getValue().atTime(23, 59, 59);

                if (inicio.isAfter(fin)) {
                    mostrarError("La fecha de inicio no puede ser posterior a la fecha fin");
                    return;
                }

                List<Producto> productos = servicio.obtenerProductosEntreFechas(inicio, fin);

                contenido.append("Productos publicados entre ")
                        .append(inicio.toLocalDate())
                        .append(" y ")
                        .append(fin.toLocalDate())
                        .append(":\n\n");

                for (Producto p : productos) {
                    contenido.append("- ")
                            .append(p.getNombre())
                            .append(" ($")
                            .append(p.getPrecio())
                            .append(")\n");
                }
                break;

            case "Top 10 productos con más me gusta":
                List<Producto> topProductos = servicio.obtenerTopProductosMeGusta(10);
                contenido.append("Top 10 productos con más me gusta:\n\n");

                for (int i = 0; i < topProductos.size(); i++) {
                    Producto p = topProductos.get(i);
                    contenido.append(i + 1)
                            .append(". ")
                            .append(p.getNombre())
                            .append("\n");
                }
                break;

            case "Mensajes entre vendedores":
                List<Usuario> usuarios = servicio.getMarketplace().getUsuarios();
                contenido.append("Mensajes entre vendedores:\n\n");

                for (Usuario u1 : usuarios) {
                    if (u1 instanceof Vendedor) {
                        for (Usuario u2 : usuarios) {
                            if (u2 instanceof Vendedor && !u1.equals(u2)) {
                                int mensajes = servicio.contarMensajesEntreVendedores(
                                        (Vendedor)u1, (Vendedor)u2
                                );
                                if (mensajes > 0) {
                                    contenido.append(u1.getNombre())
                                    .append(" → ")
                                    .append(u2.getNombre())
                                    .append(": ")
                                    .append(mensajes)
                                    .append(" mensajes\n");
                                }
                            }
                        }
                    }
                }
                break;
        }

        txtEstadisticas.setText(contenido.toString());
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void onExportar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt")
        );

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            servicio.exportarEstadisticas(
                    file.getAbsolutePath(),
                    "Reporte de " + cboTipoReporte.getValue(),
                    "Administrador",
                    txtEstadisticas.getText()
            );

            mostrarMensaje("Reporte exportado exitosamente");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}