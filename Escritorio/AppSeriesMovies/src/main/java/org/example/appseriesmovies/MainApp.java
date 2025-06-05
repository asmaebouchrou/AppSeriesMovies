package org.example.appseriesmovies;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Principal.fxml"));
        Parent root = loader.load();  // Aquí se carga el FXML y se asigna a root
        Scene scene = new Scene(root, 1200, 1000);
        scene.getStylesheets().add(getClass().getResource("/styles/modern.css").toExternalForm());

        primaryStage.setTitle("Gestor Multimedia");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}