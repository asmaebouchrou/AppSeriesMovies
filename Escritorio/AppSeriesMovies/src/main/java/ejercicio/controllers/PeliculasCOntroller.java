package ejercicio.controllers;

import ejercicio.DAOs.PeliculaDAOImpl;
import ejercicio.DAOs.PeliuculaDAO;
import ejercicio.excepciones.ValoracionException;
import ejercicio.modelos.Pelicula;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Optional;

public class PeliculasCOntroller {
    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    private PeliuculaDAO peliculaDAO = new PeliculaDAOImpl();

    @FXML
    private void initialize() {
        cargarPeliculas();
        editarButton.disableProperty().bind(
                tablaPeliculas.getSelectionModel().selectedItemProperty().isNull()
        );
        eliminarButton.disableProperty().bind(
                tablaPeliculas.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    private void cargarPeliculas() {
        tablaPeliculas.setItems(FXCollections.observableArrayList(peliculaDAO.obtenerTodasPeliculas()));
    }

    @FXML
    private void mostrarFormularioCrear() {
        // Crear diálogo
        Dialog<Pelicula> dialog = new Dialog<>();
        dialog.setTitle("Nueva Película");
        dialog.setHeaderText("Introduce los datos de la nueva película");

        // Configurar botones
        ButtonType crearButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titulo = new TextField();
        TextField director = new TextField();
        TextArea sinopsis = new TextArea();
        TextField imagen = new TextField();
        TextField descarga = new TextField();
        Spinner<Double> duracion = new Spinner<>(0.0, 1000.0, 120.0, 0.5);
        Spinner<Integer> valoracion = new Spinner<>(1, 5, 3);

        grid.add(new Label("Título:"), 0, 0);
        grid.add(titulo, 1, 0);
        grid.add(new Label("Director:"), 0, 1);
        grid.add(director, 1, 1);
        grid.add(new Label("Sinopsis:"), 0, 2);
        grid.add(sinopsis, 1, 2);
        grid.add(new Label("Duración (min):"), 0, 3);
        grid.add(duracion, 1, 3);
        grid.add(new Label("Valoración (1-5):"), 0, 4);
        grid.add(valoracion, 1, 4);
        grid.add(new Label("Ruta Imagen:"), 0, 5);
        grid.add(imagen, 1, 5);
        grid.add(new Label("Ruta Descarga:"), 0, 6);
        grid.add(descarga, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Convertir resultado a objeto Pelicula cuando se presiona Crear
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == crearButtonType) {
                try {
                    return new Pelicula(
                            titulo.getText(),
                            sinopsis.getText(),
                            imagen.getText(),
                            descarga.getText(),
                            duracion.getValue().floatValue(),
                            director.getText(),
                            valoracion.getValue()
                    );
                } catch (ValoracionException e) {
                    mostrarError("Valoración inválida: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        // Mostrar diálogo y procesar resultado
        Optional<Pelicula> result = dialog.showAndWait();

        result.ifPresent(pelicula -> {
            peliculaDAO.insertarPelicula(pelicula);
            cargarPeliculas();
            mostrarMensaje("Película creada exitosamente");
        });
    }

    @FXML
    private void mostrarFormularioEditar() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Por favor selecciona una película");
            return;
        }

        // Crear diálogo
        Dialog<Pelicula> dialog = new Dialog<>();
        dialog.setTitle("Editar Película");
        dialog.setHeaderText("Edita los datos de la película");

        // Configurar botones
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        // Crear formulario con valores actuales
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titulo = new TextField(seleccionada.getTitulo());
        TextField director = new TextField(seleccionada.getDirector());
        TextArea sinopsis = new TextArea(seleccionada.getSinopsis());
        TextField imagen = new TextField(seleccionada.getImagen());
        TextField descarga = new TextField(seleccionada.getDescarga());
        Spinner<Double> duracion = new Spinner<>(0.0, 1000.0, seleccionada.getDuracion(), 0.5);
        Spinner<Integer> valoracion = new Spinner<>(1.0, 5, seleccionada.getValoracion());

        // Mismo layout que en crear
        grid.add(new Label("Título:"), 0, 0);
        grid.add(titulo, 1, 0);
        grid.add(new Label("Director:"), 0, 1);
        grid.add(director, 1, 1);
        grid.add(new Label("Sinopsis:"), 0, 2);
        grid.add(sinopsis, 1, 2);
        grid.add(new Label("Duración (min):"), 0, 3);
        grid.add(duracion, 1, 3);
        grid.add(new Label("Valoración (1-5):"), 0, 4);
        grid.add(valoracion, 1, 4);
        grid.add(new Label("Ruta Imagen:"), 0, 5);
        grid.add(imagen, 1, 5);
        grid.add(new Label("Ruta Descarga:"), 0, 6);
        grid.add(descarga, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Convertir resultado a objeto Pelicula cuando se presiona Guardar
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                try {
                    Pelicula editada = new Pelicula(
                            seleccionada.getIdVideo(), // Mantener mismo ID
                            titulo.getText(),
                            sinopsis.getText(),
                            imagen.getText(),
                            descarga.getText(),
                            duracion.getValue().floatValue(),
                            director.getText(),
                            valoracion.getValue()
                    );
                    return editada;
                } catch ( Exception e ) {
                    mostrarError("Valoración inválida: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        // Mostrar diálogo y procesar resultado
        Optional<Pelicula> result = dialog.showAndWait();

        result.ifPresent(peliculaEditada -> {
            // Actualizar valoración (o todos los campos según tu implementación)
            peliculaDAO.actualizarValoracionPelicula(
                    peliculaEditada.getIdVideo(),
                    peliculaEditada.getValoracion()
            );
            cargarPeliculas();
            mostrarMensaje("Película actualizada exitosamente");
        });
    }

    @FXML
    private void eliminarPelicula() {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            boolean exito = peliculaDAO.borrarPeliculaPorId(seleccionada.getIdVideo());
            if (exito) {
                cargarPeliculas();
                mostrarMensaje("Película eliminada correctamente");
            } else {
                mostrarError("No se pudo eliminar la película");
            }
        }
    }
    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        InputStream iconStream = getClass().getResourceAsStream("/imagenes/mou.jpeg");
//        if (iconStream != null) {
//            stage.getIcons().add(new Image(iconStream));
//        } else {
//            System.err.println("No se encontró la imagen del icono: /imagenes/mou.jpeg");
//        }

        try {
            alert.getDialogPane().getStylesheets().add(
                    getClass().getResource("/styles/modern.css").toExternalForm()
            );
            alert.getDialogPane().getStyleClass().add("dialog-pane");
        } catch (NullPointerException e) {
            System.err.println("No se encontró el archivo de estilo: /styles/modern.css");
        }

        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

//        try {
//            InputStream iconStream = getClass().getResourceAsStream("/imagenes/mou.jpeg");
//            if (iconStream != null) {
//                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//                stage.getIcons().add(new Image(iconStream));
//            } else {
//                System.err.println("No se pudo cargar el icono: /imagenes/mou.jpeg");
//            }
//        } catch (Exception e) {
//            System.err.println("Error al cargar el icono: " + e.getMessage());
//        }

        alert.showAndWait();
    }
}