    package ejercicio.controllers;

    import ejercicio.DAOs.PeliculaDAOImpl;
    import ejercicio.DAOs.PeliuculaDAO;
    import ejercicio.DAOs.SerieDAO;
    import ejercicio.DAOs.SerieDAOImpl;
    import ejercicio.modelos.Pelicula;
    import ejercicio.modelos.Serie;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Pos;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.FlowPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;

    import java.io.InputStream;
    import java.util.List;

    public class PrincipalController {
        @FXML private VBox panelDetalles;
        @FXML private Label detalleTitulo;
        @FXML private Label detalleDirector;
        @FXML private Label detalleDuracion;
        @FXML private Label detalleValoracion;
        @FXML private Label detalleSinopsis;
        @FXML private Label detalleDescarga;
        @FXML private VBox panelDetallesSerie;
        @FXML private Label detalleTituloSerie;
        @FXML private Label detalleCreadorSerie;
        @FXML private Label detalleTemporadasSerie;
        @FXML private Label detalleValoracionSerie;
        @FXML private Label detalleSinopsisSerie;



        @FXML private FlowPane peliculasContainer;
        @FXML private FlowPane seriesContainer;


        private PeliuculaDAO peliculaDAO = new PeliculaDAOImpl();
        private SerieDAO serieDAO = new SerieDAOImpl();

        @FXML
        public void initialize() {
            cargarPeliculas();
            cargarSeries();

        }

        private void cargarPeliculas() {
            peliculasContainer.getChildren().clear();
            List<Pelicula> peliculas = peliculaDAO.obtenerTodasPeliculas();
            for (Pelicula pelicula : peliculas) {
                VBox tarjeta = crearTarjeta(pelicula); // le pasamos la Pelicula entera
                peliculasContainer.getChildren().add(tarjeta);
            }
        }
        private void mostrarDetalles(Pelicula pelicula) {
            detalleTitulo.setText("Título: " + pelicula.getTitulo());
            detalleDirector.setText("Director: " + pelicula.getDirector());
            detalleDuracion.setText("Duración: " + pelicula.getDuracion() + " min");
            detalleValoracion.setText("Valoración: " + pelicula.getValoracion());
            detalleSinopsis.setText("Sinopsis: " + pelicula.getSinopsis());
            detalleDescarga.setText("Descarga: " + pelicula.getDescarga());

            panelDetalles.setVisible(true);
            panelDetalles.setManaged(true);
        }
        private void mostrarDetallesSerie(Serie serie) {
            detalleTitulo.setText("Título: " + serie.getTitulo());
            detalleDirector.setText("Temporadas: " + serie.getNumeroTemporadas());
            detalleDuracion.setText("Duración: " + serie.getDuracion() + " min");
            detalleSinopsis.setText("Sinopsis: " + serie.getSinopsis());
            detalleDescarga.setText("Descarga: " + serie.getDescarga());

            panelDetalles.setVisible(true);
            panelDetalles.setManaged(true);
        }

        private void ocultarDetallesSerie() {
            panelDetallesSerie.setVisible(false);
            panelDetallesSerie.setManaged(false);
        }

        private void ocultarDetalles() {
            panelDetalles.setVisible(false);
            panelDetalles.setManaged(false);
        }
        private VBox crearTarjeta(Serie serie) {
            String titulo = serie.getTitulo();
            String imagenUrl = serie.getImagen();
            String rutaImagen = imagenUrl.startsWith("images/") ? "/" + imagenUrl : "/images/" + imagenUrl;

            InputStream is = getClass().getResourceAsStream(rutaImagen);
            Image imagenObj = (is == null)
                    ? new Image(getClass().getResourceAsStream("/images/icon.png"))
                    : new Image(is);

            ImageView imagen = new ImageView(imagenObj);
            imagen.setFitWidth(200);
            imagen.setFitHeight(250);
            imagen.setPreserveRatio(false);

            Label tituloLabel = new Label(titulo);
            tituloLabel.getStyleClass().add("titulo-tarjeta");

            VBox tarjeta = new VBox(imagen, tituloLabel);
            tarjeta.setAlignment(Pos.CENTER);
            tarjeta.setSpacing(8);
            tarjeta.getStyleClass().add("tarjeta");

            tarjeta.setOnMouseClicked(e -> mostrarDetallesSerie(serie));

            return tarjeta;
        }

        private void cargarSeries() {
            seriesContainer.getChildren().clear();  // Limpia el contenido para evitar duplicados
            List<Serie> series = serieDAO.obtenerTodasSeries();
            for (Serie serie : series) {
                VBox tarjeta = crearTarjeta(serie);
                seriesContainer.getChildren().add(tarjeta);
            }
        }



        /*
        * InputStream is = getClass().getResourceAsStream("/images/" + nombreImagen);
    if (is == null) {
        System.err.println("No se encontró la imagen: " + nombreImagen);
        // Puedes cargar una imagen por defecto o devolver null
        return null;
    }
    Image img = new Image(is);*/

        private VBox crearTarjeta(Pelicula pelicula) {
            String titulo = pelicula.getTitulo();
            String imagenUrl = pelicula.getImagen();
            String rutaImagen;

            if (imagenUrl.startsWith("images/")) {
                rutaImagen = "/" + imagenUrl;
            } else {
                rutaImagen = "/images/" + imagenUrl;
            }

            InputStream is = getClass().getResourceAsStream(rutaImagen);
            Image imagenObj;

            if (is == null) {
                System.out.println("No se encontró la imagen: " + rutaImagen);
                imagenObj = new Image(getClass().getResourceAsStream("/images/icon.png"));
            } else {
                imagenObj = new Image(is);
            }

            ImageView imagen = new ImageView(imagenObj);
            imagen.setFitWidth(200);
            imagen.setFitHeight(250);
            imagen.setPreserveRatio(false);

            Label tituloLabel = new Label(titulo);
            tituloLabel.getStyleClass().add("titulo-tarjeta");

            VBox tarjeta = new VBox(imagen, tituloLabel);
            tarjeta.setAlignment(Pos.CENTER);
            tarjeta.setSpacing(8);
            tarjeta.getStyleClass().add("tarjeta");

            //  Evento para mostrar detalles al hacer clic
            tarjeta.setOnMouseClicked(event -> mostrarDetalles(pelicula));

            return tarjeta;
        }



        //Luego vuelvo aqui
        @FXML
        private void irAPeliculas() {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/Pelicula.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1200, 1000));
                stage.setMinWidth(1500);
                stage.setMinHeight(900);
                stage.setTitle("Películas");
                stage.show();
            } catch (Exception e) {
                mostrarError("No se pudo cargar la vista de películas");
                e.printStackTrace();
            }
        }

        @FXML
        private void irASeries() {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/SerieView.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1200, 1000));
                stage.setMinWidth(1500);
                stage.setMinHeight(900);
                stage.setTitle("Series");
                stage.show();
            } catch (Exception e) {
                mostrarError("No se pudo cargar la vista de series");
                e.printStackTrace();
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
