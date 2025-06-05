package ejercicio.DAOs;

import ejercicio.modelos.Pelicula;

import java.util.List;

public interface PeliuculaDAO {
    void insertarPelicula (Pelicula pelicula);
    boolean borrarPeliculaPorId(String id);
    Pelicula obtenerPeliculaPorId(String id);
    List<Pelicula> obtenerTodasPeliculas();
    List<Pelicula> obtenerPeliculasSegunValoracion(int valoracion);
    List<Pelicula> obtenerTodasPeliculasOrdenadasPorValoracion();
    Pelicula actualizarValoracionPelicula(String idPelicula, int nuevaValoracion);
}
