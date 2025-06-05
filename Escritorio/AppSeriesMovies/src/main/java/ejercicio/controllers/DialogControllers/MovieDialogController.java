package ejercicio.controllers.DialogControllers;

import ejercicio.excepciones.ValoracionException;
import ejercicio.modelos.Pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MovieDialogController {

    @FXML private TextField tituloField;
    @FXML private TextField directorField;
    @FXML private TextArea sinopsisArea;
    @FXML private ChoiceBox<Integer> valoracionBox;
    @FXML private TextField duracionField;

    private Pelicula pelicula;

    @FXML
    public void initialize() {
        valoracionBox.getItems().addAll(1, 2, 3, 4, 5);
        valoracionBox.setValue(3);
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        if (pelicula != null) {
            tituloField.setText(pelicula.getTitulo());
            directorField.setText(pelicula.getDirector());
            sinopsisArea.setText(pelicula.getSinopsis());
            valoracionBox.setValue(pelicula.getValoracion());
            duracionField.setText(String.valueOf(pelicula.getDuracion()));
        }
    }

    public Pelicula getPelicula() throws ValoracionException {
        if (pelicula == null) {
            pelicula = new Pelicula(
                    tituloField.getText(),
                    sinopsisArea.getText(),
                    "", "", // Imagen y descarga (implementar según necesidad)
                    Float.parseFloat(duracionField.getText()),
                    directorField.getText(),
                    valoracionBox.getValue()
            );
        } else {
            pelicula.setTitulo(tituloField.getText());
            pelicula.setDirector(directorField.getText());
            pelicula.setSinopsis(sinopsisArea.getText());
            pelicula.setValoracion(valoracionBox.getValue());
            pelicula.setDuracion(Float.parseFloat(duracionField.getText()));
        }
        return pelicula;
    }
}