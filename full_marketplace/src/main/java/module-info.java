module co.edu.uniquindio.full_marketplace {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    // Abre los paquetes que contienen los controladores para FXML
    opens co.edu.uniquindio.full_marketplace to javafx.fxml;
    opens co.edu.uniquindio.full_marketplace.viewcontroller to javafx.fxml;

    // Exporta los paquetes principales
    exports co.edu.uniquindio.full_marketplace;
    exports co.edu.uniquindio.full_marketplace.viewcontroller;
    exports co.edu.uniquindio.full_marketplace.model;
}