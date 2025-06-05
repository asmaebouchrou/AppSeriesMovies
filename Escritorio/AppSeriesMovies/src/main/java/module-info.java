open module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    exports org.example.appseriesmovies;
    exports ejercicio.controllers;
    exports ejercicio.modelos;
}
