package co.edu.uniquindio.full_marketplace.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// se hace la plantilla para el reporte
public class BuilderReporte {
    private String titulo;
    private String usuario;
    private String contenido;
    private LocalDateTime fecha;

    public BuilderReporte conTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public BuilderReporte conUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    public BuilderReporte conContenido(String contenido) {
        this.contenido = contenido;
        return this;
    }

    public String construir() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("<Título>").append(titulo).append("\n");
        reporte.append("<fecha>Fecha: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        reporte.append("<Usuario>Reporte realizado por: ").append(usuario).append("\n\n");
        reporte.append("Información del reporte:\n");
        reporte.append(contenido);
        return reporte.toString();
    }
}