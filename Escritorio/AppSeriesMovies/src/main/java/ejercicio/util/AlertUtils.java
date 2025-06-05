package ejercicio.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtils {

    // Muestra un mensaje de error
    public static void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Muestra un mensaje informativo
    public static void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Muestra un diálogo de confirmación y devuelve true si el usuario acepta
    public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
