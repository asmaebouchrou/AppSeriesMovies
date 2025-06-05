    package ejercicio.controllers;

    import ejercicio.DAOs.SerieDAO;
    import ejercicio.DAOs.SerieDAOImpl;
    import ejercicio.modelos.Capitulo;
    import ejercicio.modelos.Serie;
    import ejercicio.modelos.Temporada;
    import ejercicio.util.AlertUtils;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Insets;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.VBox;
    import javafx.util.Pair;

    import java.util.List;
    import java.util.Optional;

    public class SerieController {

        @FXML
        private Button botonAñadirTemporada;

        @FXML
        private Button botonEditarSerie;

        @FXML
        private Button botonEliminarSerie;
        @FXML private TableView<Serie> tablaSeries;
        @FXML private Accordion acordionTemporadas;
        @FXML private TitledPane panelTemporadas;
        @FXML
        private TableColumn<Serie, String> columnaTitulo;
        @FXML
        private TableColumn<Serie, String> columnaSinopsis;
        @FXML
        private TableColumn<Serie, Float> columnaDuracion;
        @FXML
        private TableColumn<Serie, String> columnaImagen;
        @FXML
        private TableColumn<Serie, String> columnaDescarga;
        @FXML
        private TableColumn<Serie, Integer> columnaNumeroTemporadas;

        private SerieDAO serieDAO = new SerieDAOImpl();
        private Serie serieSeleccionada;

        @FXML private TableColumn<Serie, String> columnaGenero;

        @FXML
        public void initialize() {
            // Configurar las columnas
            columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            columnaSinopsis.setCellValueFactory(new PropertyValueFactory<>("sinopsis"));
            columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
            columnaImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
            columnaDescarga.setCellValueFactory(new PropertyValueFactory<>("descarga"));
            columnaNumeroTemporadas.setCellValueFactory(new PropertyValueFactory<>("numTemporadas"));

            // Configurar el ancho preferido de las columnas
            columnaTitulo.setPrefWidth(200);
            columnaSinopsis.setPrefWidth(300);
            columnaDuracion.setPrefWidth(100);
            columnaNumeroTemporadas.setPrefWidth(100);
            columnaImagen.setPrefWidth(120);
            columnaDescarga.setPrefWidth(120);

            // Cargar los datos
            cargarSeries();

            // Configurar la selección de la tabla
            tablaSeries.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            serieSeleccionada = newSelection;
                            cargarTemporadas(newSelection);
                        }
                    });

            // Deshabilitar botones si no hay selección
            botonAñadirTemporada.disableProperty().bind(
                    tablaSeries.getSelectionModel().selectedItemProperty().isNull()
            );
            botonEditarSerie.disableProperty().bind(
                    tablaSeries.getSelectionModel().selectedItemProperty().isNull()
            );
            botonEliminarSerie.disableProperty().bind(
                    tablaSeries.getSelectionModel().selectedItemProperty().isNull()
            );
        }
        private void cargarSeries() {
            try {
                // Limpiar la tabla antes de cargar nuevos datos
                tablaSeries.getItems().clear();

                // Obtener todas las series desde la base de datos
                List<Serie> series = serieDAO.obtenerTodasSeries();

                // Verificar si hay datos
                if (series.isEmpty()) {
                    AlertUtils.mostrarInfo("No hay series disponibles en la base de datos");
                    return;
                }

                // Configurar las celdas para mostrar los datos formateados
                columnaDuracion.setCellFactory(column -> new TableCell<Serie, Float>() {
                    @Override
                    protected void updateItem(Float duracion, boolean empty) {
                        super.updateItem(duracion, empty);
                        if (empty || duracion == null) {
                            setText(null);
                        } else {
                            setText(String.format("%.1f min", duracion));
                        }
                    }
                });

                // Añadir las series a la tabla
                tablaSeries.getItems().addAll(series);

                // Ordenar por título por defecto
                columnaTitulo.setSortType(TableColumn.SortType.ASCENDING);
                tablaSeries.getSortOrder().add(columnaTitulo);
                tablaSeries.sort();

            } catch (Exception e) {
                AlertUtils.mostrarError("Error al cargar las series: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void cargarTemporadas(Serie serie) {
            acordionTemporadas.getPanes().clear();

            serie.getTemporadas().forEach(temporada -> {
                TitledPane paneTemporada = new TitledPane();
                paneTemporada.setText("Temporada " + temporada.getNumeroTemporada() + ": " + temporada.getNombre());

                VBox contenido = new VBox(5);

                // Lista de capítulos
                ListView<Pair<String, Float>> listaCapitulos = new ListView<>();
                temporada.getCapitulos().forEach(capitulo ->
                        listaCapitulos.getItems().add(
                                new Pair<>(capitulo.getNombreCapitulo(), capitulo.getDuracionCapitulo())));

                // Botón para añadir capítulo
                Button btnAddCapitulo = new Button("Añadir Capítulo");
                btnAddCapitulo.setOnAction(e -> mostrarFormularioCapitulo(temporada));

                contenido.getChildren().addAll(listaCapitulos, btnAddCapitulo);
                paneTemporada.setContent(contenido);
                acordionTemporadas.getPanes().add(paneTemporada);
            });
        }

        @FXML
        private void volverAPrincipal() {
            try {
                AnchorPane root = FXMLLoader.load(getClass().getResource("/vistas/Principal.fxml"));
                tablaSeries.getScene().setRoot(root);
            } catch (Exception e) {
                AlertUtils.mostrarError("Error al cargar la vista principal");
            }
        }

        @FXML
        private void mostrarFormularioCrearSerie() {
            Dialog<Serie> dialog = new Dialog<>();
            dialog.setTitle("Nueva Serie");
            dialog.setHeaderText("Introduce los datos de la nueva serie");

            ButtonType crearButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField titulo = new TextField();
            TextField genero = new TextField();
            TextArea sinopsis = new TextArea();
            TextField imagen = new TextField();
            TextField descarga = new TextField();
            Spinner<Double> duracion = new Spinner<>(0.1, 300.0, 45.0, 0.5);

            // Configurar tamaño de los campos
            sinopsis.setPrefRowCount(3);
            sinopsis.setWrapText(true);

            // Añadir campos al grid
            grid.add(new Label("Título:"), 0, 0);
            grid.add(titulo, 1, 0);
            grid.add(new Label("Género:"), 0, 1);
            grid.add(genero, 1, 1);
            grid.add(new Label("Sinopsis:"), 0, 2);
            grid.add(sinopsis, 1, 2);
            grid.add(new Label("Imagen (URL o ruta):"), 0, 3);
            grid.add(imagen, 1, 3);
            grid.add(new Label("Descarga (URL o ruta):"), 0, 4);
            grid.add(descarga, 1, 4);
            grid.add(new Label("Duración (min):"), 0, 5);
            grid.add(duracion, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == crearButtonType) {
                    return new Serie(
                            java.util.UUID.randomUUID().toString(), // idVideo generado automáticamente
                            titulo.getText(),
                            sinopsis.getText(),
                            imagen.getText(),         // ruta de imagen
                            descarga.getText(),       // ruta de descarga
                            duracion.getValue().floatValue() // duración
                    );
                }
                return null;
            });

            Optional<Serie> result = dialog.showAndWait();

            result.ifPresent(serie -> {
                serieDAO.crearSerie(serie);
                cargarSeries();
                AlertUtils.mostrarInfo("Serie creada exitosamente");
            });
        }


        @FXML
        private void mostrarFormularioEditarSerie() {
            if (serieSeleccionada == null) return;

            Dialog<Serie> dialog = new Dialog<>();
            dialog.setTitle("Editar Serie");
            dialog.setHeaderText("Modifica los datos de la serie");

            ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField titulo = new TextField(serieSeleccionada.getTitulo());
            TextField sinopsis = new TextField(serieSeleccionada.getSinopsis());
            TextField imagen = new TextField(serieSeleccionada.getImagen());
            TextField descarga = new TextField(serieSeleccionada.getDescarga());
            TextField duracion = new TextField(String.valueOf(serieSeleccionada.getDuracion()));


            grid.add(new Label("Título:"), 0, 0);
            grid.add(titulo, 1, 0);

            grid.add(new Label("Sinopsis:"), 0, 1);
            grid.add(sinopsis, 1, 1);

            grid.add(new Label("Imagen (URL o ruta):"), 0, 2);
            grid.add(imagen, 1, 2);

            grid.add(new Label("Descarga (URL o ruta):"), 0, 3);
            grid.add(descarga, 1, 3);

            grid.add(new Label("Duración (minutos):"), 0, 4);
            grid.add(duracion, 1, 4);


            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == guardarButtonType) {
                    serieSeleccionada.setTitulo(titulo.getText());
                    serieSeleccionada.setSinopsis(sinopsis.getText());
                    serieSeleccionada.setImagen(imagen.getText());
                    serieSeleccionada.setDescarga(descarga.getText());
                    serieSeleccionada.setDuracion(Float.parseFloat(duracion.getText()));
                    return serieSeleccionada;
                }
                return null;

            });

            Optional<Serie> result = dialog.showAndWait();

            result.ifPresent(serie -> {
                boolean exito = serieDAO.actualizarSerie(serie);
                if (exito) {
                    cargarSeries();
                    AlertUtils.mostrarInfo("Serie actualizada correctamente");
                } else {
                    AlertUtils.mostrarError("No se pudo actualizar la serie");
                }
            });
        }
        @FXML
        private void eliminarSerie() {
            Serie seleccionada = tablaSeries.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                boolean confirmar = AlertUtils.mostrarConfirmacion(
                        "¿Eliminar serie?",
                        "Esta acción borrará la serie y todas sus temporadas y capítulos. ¿Continuar?");

                if (confirmar) {
                    boolean exito = serieDAO.borrarSerie(seleccionada.getIdVideo());
                    if (exito) {
                        cargarSeries();
                        AlertUtils.mostrarInfo("Serie eliminada correctamente");
                    } else {
                        AlertUtils.mostrarError("No se pudo eliminar la serie");
                    }
                }
            }
        }

        @FXML
        private void mostrarFormularioTemporada() {
            if (serieSeleccionada == null) return;

            Dialog<Temporada> dialog = new Dialog<>();
            dialog.setTitle("Nueva Temporada");
            dialog.setHeaderText("Añadir temporada a: " + serieSeleccionada.getTitulo());

            // Configurar botones
            ButtonType crearButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

            // Crear formulario
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField numero = new TextField();
            TextField nombre = new TextField();

            grid.add(new Label("Número:"), 0, 0);
            grid.add(numero, 1, 0);
            grid.add(new Label("Nombre:"), 0, 1);
            grid.add(nombre, 1, 1);

            dialog.getDialogPane().setContent(grid);

            // Convertir resultado
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == crearButtonType) {
                    try {
                        return new Temporada(
                                serieSeleccionada.getIdVideo(),
                                nombre.getText(),
                                Integer.parseInt(numero.getText())
                        );
                    } catch (NumberFormatException e) {
                        AlertUtils.mostrarError("El número de temporada debe ser un valor entero");
                        return null;
                    }
                }
                return null;
            });

            // Procesar resultado
            Optional<Temporada> result = dialog.showAndWait();
            result.ifPresent(temporada -> {
                serieDAO.crearTemporada(temporada);
                cargarTemporadas(serieSeleccionada);
                AlertUtils.mostrarInfo("Temporada creada exitosamente");
            });
        }

        @FXML
        private void mostrarFormularioCapitulo(Temporada temporada) {
            Dialog<Pair<String, Float>> dialog = new Dialog<>();
            dialog.setTitle("Nuevo Capítulo");
            dialog.setHeaderText("Añadir capítulo a Temporada " + temporada.getNumeroTemporada());

            ButtonType crearButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nombre = new TextField();
            Spinner<Double> duracion = new Spinner<>(0.1, 300.0, 45.0, 0.5);

            grid.add(new Label("Nombre:"), 0, 0);
            grid.add(nombre, 1, 0);
            grid.add(new Label("Duración (min):"), 0, 1);
            grid.add(duracion, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == crearButtonType) {
                    return new Pair<>(nombre.getText(), duracion.getValue().floatValue());
                }
                return null;
            });

            Optional<Pair<String, Float>> result = dialog.showAndWait();
            result.ifPresent(capituloData -> {
                Capitulo capitulo = new Capitulo(
                        temporada.getNumeroTemporada(),            // Asumiendo que esto es el ID de la temporada
                        serieSeleccionada.getIdVideo(),            // ID de la serie
                        capituloData.getKey(),                     // Nombre del capítulo
                        capituloData.getValue()                    // Duración
                );

                boolean exito = serieDAO.crearCapitulo(capitulo);
                if (exito) {
                    AlertUtils.mostrarInfo("Capítulo creado exitosamente");
                    cargarTemporadas(serieSeleccionada);
                } else {
                    AlertUtils.mostrarError("No se pudo crear el capítulo");
                }
            });

        }

    }