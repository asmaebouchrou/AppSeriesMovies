package ejercicio.DAOs;

import ejercicio.modelos.Capitulo;
import ejercicio.modelos.Serie;
import ejercicio.modelos.Temporada;

import java.sql.SQLException;
import java.util.List;

public interface SerieDAO {
    void crearSerie (Serie serie);
    boolean borrarSerie(String id);
    List<Serie> obtenerTodasSeries();
    Serie obtenerSeriePorId(String id);
    void crearTemporada(Temporada temporada);
    boolean crearCapitulo(Capitulo capitulo);
    boolean actualizarSerie(Serie serie);
    }
