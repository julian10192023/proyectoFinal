package co.edu.uniquindio.full_marketplace.viewcontroller;

import co.edu.uniquindio.full_marketplace.model.*;
import co.edu.uniquindio.full_marketplace.singleton.ServicioMarketPlace;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class VendedorTabController {
    @FXML private ListView<ProductoConLikes> lstProductos;
    @FXML private ListView<Vendedor> lstContactos;
    @FXML private TextArea txtMuro;
    @FXML private Button btnPublicar;
    @FXML private Button btnAgregarContacto;
    @FXML private Button btnComentar;
    @FXML private Button btnMeGusta;
    private TabPane tabPane;

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    private Vendedor vendedor;
    private ServicioMarketPlace servicio = ServicioMarketPlace.obtenerInstancia();

    // Clase auxiliar para mostrar productos con likes
    private static class ProductoConLikes {
        private final Producto producto;
        private final long likes;

        public ProductoConLikes(Producto producto, long likes) {
            this.producto = producto;
            this.likes = likes;
        }

        public Producto getProducto() {
            return producto;
        }

        public long getLikes() {
            return likes;
        }

        @Override
        public String toString() {
            return String.format("%s - $%.2f (❤ %d)",
                    producto.getNombre(),
                    producto.getPrecio(),
                    likes);
        }
    }

    @FXML
    public void initialize() {
        if (btnMeGusta != null) {
            btnMeGusta.setDisable(true);
        }

        if (lstProductos != null) {
            lstProductos.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    mostrarLikesProducto(lstProductos.getSelectionModel().getSelectedItem());
                }
            });
        }

        if (lstContactos != null) {
            lstContactos.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    confirmarEliminarContacto(lstContactos.getSelectionModel().getSelectedItem());
                }
            });
        }

        if (btnComentar != null) {
            btnComentar.setDisable(true);

            // Habilitar/deshabilitar según selección de producto
            lstProductos.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> btnComentar.setDisable(newValue == null)
            );
        }

        // Solo inicializar los elementos que existen en el FXML
        if (lstProductos != null) {
            lstProductos.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        if (btnMeGusta != null) {
                            btnMeGusta.setDisable(newValue == null);

                            if (newValue != null) {
                                Usuario usuarioActual = servicio.getUsuarioActual();
                                if (usuarioActual instanceof Vendedor) {
                                    Vendedor vendedorActual = (Vendedor) usuarioActual;
                                    boolean yaLeDioMeGusta = vendedorActual.getProductosLiked()
                                            .contains(newValue.producto);

                                    if (yaLeDioMeGusta) {
                                        btnMeGusta.setText("Me gusta ❤");
                                        btnMeGusta.setStyle("-fx-background-color: #ff4081;");
                                    } else {
                                        btnMeGusta.setText("Me gusta ♡");
                                        btnMeGusta.setStyle("-fx-background-color: #2196f3;");
                                    }
                                }
                            }
                        }
                    }
            );
        }

        if (lstContactos != null) {
            lstContactos.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Vendedor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNombre() + " " + item.getApellidos());
                    }
                }
            });
        }
    }

    @FXML
    void onComentar(ActionEvent event) {
        ProductoConLikes seleccion = lstProductos.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un producto para comentar");
            return;
        }

        mostrarDialogoComentario(seleccion.getProducto());
    }

    private void mostrarDialogoComentario(Producto producto) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Comentario");
        dialog.setHeaderText("Escriba su comentario para: " + producto.getNombre());

        TextArea txtComentario = new TextArea();
        txtComentario.setPromptText("Escriba su comentario aquí...");
        txtComentario.setPrefRowCount(3);

        dialog.getDialogPane().setContent(txtComentario);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return txtComentario.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(comentario -> {
            try {
                Usuario usuarioActual = servicio.getUsuarioActual();
                if (usuarioActual instanceof Vendedor) {
                    servicio.publicarComentario((Vendedor)usuarioActual, producto, comentario);
                    actualizarVista();
                    mostrarMensaje("Comentario agregado exitosamente");
                }
            } catch (IllegalStateException e) {
                mostrarError(e.getMessage());
            }
        });
    }

    private void mostrarLikesProducto(ProductoConLikes productoConLikes) {
        if (productoConLikes == null) return;

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Me gusta");
        dialog.setHeaderText("Usuarios que dieron Me gusta a: " + productoConLikes.getProducto().getNombre());

        ListView<String> listView = new ListView<>();
        ObservableList<String> likes = FXCollections.observableArrayList();

        // Recolectar usuarios que dieron like
        for (Usuario usuario : servicio.getMarketplace().getUsuarios()) {
            if (usuario.getProductosLiked().contains(productoConLikes.getProducto())) {
                likes.add(usuario.getNombre() + " " + usuario.getApellidos());
            }
        }

        listView.setItems(likes);
        dialog.getDialogPane().setContent(listView);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void confirmarEliminarContacto(Vendedor contacto) {
        if (contacto == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Contacto");
        alert.setHeaderText("¿Está seguro de eliminar el contacto " + contacto.getNombre() + "?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Usuario usuarioActual = servicio.getUsuarioActual();
            if (usuarioActual instanceof Vendedor) {
                Vendedor vendedorActual = (Vendedor) usuarioActual;

                // Eliminar la relación bidireccional
                vendedorActual.getVendedoresAliados().remove(contacto);
                contacto.getVendedoresAliados().remove(vendedorActual);

                // Eliminar la tab del contacto
                if (tabPane != null) {
                    Tab tabToRemove = tabPane.getTabs().stream()
                            .filter(tab -> tab.getId() != null &&
                                    tab.getId().equals("tab-" + contacto.getUsername()))
                            .findFirst()
                            .orElse(null);

                    if (tabToRemove != null) {
                        tabPane.getTabs().remove(tabToRemove);
                    }
                }

                actualizarVista();
                mostrarMensaje("Contacto eliminado exitosamente");
            }
        }
    }

    @FXML
    void onDarMeGusta(ActionEvent event) {
        ProductoConLikes seleccion = lstProductos.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un producto para dar/quitar Me gusta");
            return;
        }

        Usuario usuarioActual = servicio.getUsuarioActual();
        if (usuarioActual == null || !(usuarioActual instanceof Vendedor)) {
            mostrarError("Solo los vendedores pueden dar Me gusta a productos");
            return;
        }

        try {
            Vendedor vendedorActual = (Vendedor) usuarioActual;
            boolean yaLeDioMeGusta = vendedorActual.getProductosLiked().contains(seleccion.producto);

            if (yaLeDioMeGusta) {
                servicio.quitarMeGusta(vendedorActual, seleccion.producto);
            } else {
                servicio.darMeGusta(vendedorActual, seleccion.producto);
            }

            actualizarVista();

            // Actualizar el estado visual del botón
            yaLeDioMeGusta = vendedorActual.getProductosLiked().contains(seleccion.producto);
            if (yaLeDioMeGusta) {
                btnMeGusta.setText("Me gusta ❤");
                btnMeGusta.setStyle("-fx-background-color: #ff4081;");
            } else {
                btnMeGusta.setText("Me gusta ♡");
                btnMeGusta.setStyle("-fx-background-color: #2196f3;");
            }

            mostrarMensaje(yaLeDioMeGusta ?
                    "Me gusta registrado correctamente" :
                    "Me gusta eliminado correctamente");
        } catch (Exception e) {
            mostrarError("Error al modificar Me gusta: " + e.getMessage());
        }
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
        actualizarVista();

        Usuario usuarioActual = servicio.getUsuarioActual();
        if (usuarioActual instanceof Vendedor) {
            if (btnPublicar != null) {
                boolean esTabPropio = vendedor.getUsername().equals(usuarioActual.getUsername());
                btnPublicar.setDisable(!esTabPropio);
                if (esTabPropio) {
                    btnPublicar.setStyle("-fx-background-color: #2196f3;");
                } else {
                    btnPublicar.setStyle("-fx-background-color: #9e9e9e;");
                }
            }

            // Habilitar el botón de Me gusta solo si hay producto seleccionado
            if (btnMeGusta != null) {
                btnMeGusta.setDisable(lstProductos.getSelectionModel().getSelectedItem() == null);
            }
        }
    }

    private void actualizarVista() {
        if (vendedor == null) return;

        // Actualizar productos
        if (lstProductos != null) {
            List<ProductoConLikes> productosConLikes = vendedor.getProductos().stream()
                    .map(p -> new ProductoConLikes(p, contarLikes(p)))
                    .collect(Collectors.toList());
            lstProductos.setItems(FXCollections.observableArrayList(productosConLikes));
        }

        // Actualizar contactos
        if (lstContactos != null) {
            lstContactos.setItems(FXCollections.observableArrayList(vendedor.getVendedoresAliados()));
        }

        // Actualizar muro
        if (txtMuro != null) {
            StringBuilder muroText = new StringBuilder();
            for (Muro muro : vendedor.getMuros()) {
                muroText.append("Mensaje: ").append(muro.getMensaje().getContenido()).append("\n\n");
                for (Publicacion pub : muro.getPublicaciones()) {
                    muroText.append("Producto: ").append(pub.getProducto().getNombre())
                            .append(" (").append(pub.getFechaPublicacion()).append(")\n")
                            .append("Precio: $").append(pub.getProducto().getPrecio()).append("\n");

                    for (Comentario com : pub.getComentarios()) {
                        muroText.append("  - ").append(com.getAutor().getNombre())
                                .append(": ").append(com.getContenido()).append("\n");
                    }
                    muroText.append("\n");
                }
            }
            txtMuro.setText(muroText.toString());
        }
    }

    private long contarLikes(Producto producto) {
        return servicio.getMarketplace().getUsuarios().stream()
                .filter(u -> u.getProductosLiked().contains(producto))
                .count();
    }

    private void actualizarMuro() {
        StringBuilder muroText = new StringBuilder();
        for (Muro muro : vendedor.getMuros()) {
            muroText.append("Mensaje: ").append(muro.getMensaje().getContenido()).append("\n\n");
            for (Publicacion pub : muro.getPublicaciones()) {
                muroText.append("Producto: ").append(pub.getProducto().getNombre())
                        .append(" (").append(pub.getFechaPublicacion()).append(")\n");

                for (Comentario com : pub.getComentarios()) {
                    muroText.append("  - ").append(com.getAutor().getNombre())
                            .append(": ").append(com.getContenido()).append("\n");
                }
                muroText.append("\n");
            }
        }
        txtMuro.setText(muroText.toString());
    }

    @FXML
    void onPublicarProducto(ActionEvent event) {
        Usuario usuarioActual = servicio.getUsuarioActual();

        // Imprimir información de depuración
        System.out.println("Usuario actual: " + (usuarioActual != null ? usuarioActual.getUsername() : "null"));
        System.out.println("Vendedor del tab: " + (vendedor != null ? vendedor.getUsername() : "null"));

        // Verificar si es el mismo usuario comparando por username
        if (usuarioActual == null || !(usuarioActual instanceof Vendedor)) {
            mostrarError("Solo los vendedores pueden publicar productos");
            return;
        }

        Vendedor vendedorActual = (Vendedor) usuarioActual;
        if (!vendedorActual.getUsername().equals(vendedor.getUsername())) {
            mostrarError("Solo puedes publicar productos en tu propio tab");
            return;
        }

        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Producto");
        dialog.setHeaderText("Ingrese los datos del producto");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField();
        TextField txtCategoria = new TextField();
        TextField txtPrecio = new TextField();

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Categoría:"), 0, 1);
        grid.add(txtCategoria, 1, 1);
        grid.add(new Label("Precio:"), 0, 2);
        grid.add(txtPrecio, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (!validarCampos(txtNombre, txtCategoria, txtPrecio)) {
                ae.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    Producto nuevoProducto = new Producto(
                            txtNombre.getText().trim(),
                            "default.jpg",
                            txtCategoria.getText().trim(),
                            precio,
                            vendedorActual
                    );
                    return nuevoProducto;
                } catch (NumberFormatException e) {
                    mostrarError("El precio debe ser un número válido");
                    return null;
                }
            }
            return null;
        });

        Optional<Producto> result = dialog.showAndWait();
        result.ifPresent(producto -> {
            vendedor.getProductos().add(producto);

            // Crear una nueva publicación para el producto
            Publicacion publicacion = new Publicacion(producto);

            // Añadir la publicación al muro del vendedor
            if (!vendedor.getMuros().isEmpty()) {
                vendedor.getMuros().get(0).getPublicaciones().add(publicacion);
            } else {
                Mensaje mensaje = new Mensaje("Nuevo producto publicado", vendedor);
                Muro muro = new Muro(mensaje);
                muro.getPublicaciones().add(publicacion);
                vendedor.getMuros().add(muro);
            }

            // Añadir la publicación a la lista global de publicaciones
            servicio.getMarketplace().getPublicaciones().add(publicacion);

            actualizarVista();
            mostrarMensaje("Producto publicado exitosamente");
        });
    }

    private boolean validarCampos(TextField txtNombre, TextField txtCategoria, TextField txtPrecio) {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre del producto es requerido");
            return false;
        }
        if (txtCategoria.getText().trim().isEmpty()) {
            mostrarError("La categoría es requerida");
            return false;
        }
        try {
            Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("El precio debe ser un número válido");
            return false;
        }
        return true;
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void darMeGusta(Producto producto) {
        servicio.darMeGusta(vendedor, producto);
        actualizarVista();
    }

    @FXML
    void onAgregarContacto(ActionEvent event) {
        Dialog<Vendedor> dialog = new Dialog<>();
        dialog.setTitle("Agregar Contacto");
        dialog.setHeaderText("Seleccione un vendedor para agregar como contacto");

        Usuario usuarioActual = servicio.getUsuarioActual();
        if (!(usuarioActual instanceof Vendedor)) return;

        Vendedor vendedorActual = (Vendedor) usuarioActual;

        // Filtrar vendedores disponibles
        List<Vendedor> vendedoresDisponibles = servicio.getMarketplace().getUsuarios().stream()
                .filter(u -> u instanceof Vendedor)
                .map(u -> (Vendedor)u)
                .filter(v -> !v.equals(vendedorActual) && !vendedorActual.getVendedoresAliados().contains(v))
                .collect(Collectors.toList());

        ComboBox<Vendedor> cboVendedores = new ComboBox<>(
                FXCollections.observableArrayList(vendedoresDisponibles)
        );

        cboVendedores.setConverter(new StringConverter<Vendedor>() {
            @Override
            public String toString(Vendedor vendedor) {
                if (vendedor == null) {
                    return "";
                }
                return vendedor.getNombre() + " - " + vendedor.getApellidos();
            }

            @Override
            public Vendedor fromString(String string) {
                // Este método es necesario pero no se usa en este caso
                return null;
            }
        });

        dialog.getDialogPane().setContent(cboVendedores);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return cboVendedores.getValue();
            }
            return null;
        });

        Optional<Vendedor> result = dialog.showAndWait();
        result.ifPresent(nuevoContacto -> {
            servicio.agregarAliadoVendedor(vendedorActual, nuevoContacto);
            actualizarVista();

            // Añadir nueva tab para el contacto
            try {
                ((MainViewController) tabPane.getUserData()).cargarTabVendedor(nuevoContacto);
                mostrarMensaje("Contacto agregado exitosamente");
            } catch (IOException e) {
                mostrarError("Error al crear la pestaña del nuevo contacto");
            }
        });
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}