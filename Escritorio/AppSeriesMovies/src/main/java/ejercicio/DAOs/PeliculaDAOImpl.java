package ejercicio.DAOs;

import ejercicio.Conexion;
import ejercicio.Helper;
import ejercicio.excepciones.ValoracionException;
import ejercicio.modelos.Pelicula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAOImpl implements PeliuculaDAO {
    private Connection conn = Conexion.getInstance().getConnection();

    @Override
    public void insertarPelicula(Pelicula pelicula) {
        String sqlVideo = "INSERT INTO video VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPelicula = "INSERT INTO pelicula VALUES (?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            // 1. Insertar en video
            try (PreparedStatement stmtVideo = conn.prepareStatement(sqlVideo)) {
                stmtVideo.setString(1, pelicula.getIdVideo());
                stmtVideo.setString(2, pelicula.getTitulo());
                stmtVideo.setString(3, pelicula.getSinopsis());
                stmtVideo.setString(4, pelicula.getImagen());
                stmtVideo.setString(5, pelicula.getDescarga());
                stmtVideo.setFloat(6, pelicula.getDuracion());
                stmtVideo.executeUpdate();
            }

            // 2. Insertar en pelicula
            try (PreparedStatement stmtPelicula = conn.prepareStatement(sqlPelicula)) {
                stmtPelicula.setString(1, pelicula.getIdVideo());
                stmtPelicula.setString(2, pelicula.getDirector());
                stmtPelicula.setInt(3, pelicula.getValoracion());
                stmtPelicula.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                throw new RuntimeException("Error en transacción: " + e.getMessage(), e);
            } catch (SQLException ex) {
                throw new RuntimeException("Error en rollback: " + ex.getMessage(), ex);
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar auto-commit: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean borrarPeliculaPorId(String id) {
        String sql = "delete FROM video WHERE idVideo = ?;";
        int rows = 0;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            rows = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rows == 1;
    }

    @Override
    public Pelicula obtenerPeliculaPorId(String id) {
        String sql = "select * from vista_peliculas WHERE video_id = ?;";
        Pelicula pelicula = null;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
             statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String titulo      = resultSet.getString(2);
                String sinopsis    = resultSet.getString(3);
                String imagen      = resultSet.getString(4);
                String descarga    = resultSet.getString(5);
                float  duracion    = resultSet.getFloat(6);
                String director    = resultSet.getString(7);
                int    valoracion  = resultSet.getInt(8);
                pelicula = new Pelicula(id, titulo, sinopsis, imagen, descarga, duracion,
                        director, valoracion);

            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pelicula;
    }

    @Override
    public List<Pelicula> obtenerTodasPeliculas() {
        String sql = "select * from vista_peliculas;";
        List<Pelicula> peliculas = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String id          = resultSet.getString(1);
                String titulo      = resultSet.getString(2);
                String sinopsis    = resultSet.getString(3);
                String imagen      = resultSet.getString(4);
                String descarga    = resultSet.getString(5);
                float  duracion    = resultSet.getFloat(6);
                String director    = resultSet.getString(7);
                int    valoracion  = resultSet.getInt(8);
                Pelicula pelicula = new Pelicula(id, titulo, sinopsis, imagen, descarga, duracion,
                        director, valoracion);
                peliculas.add(pelicula);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return peliculas;
    }

    @Override
    public List<Pelicula> obtenerPeliculasSegunValoracion(int valoracion) {
        String sql = " select * from vista_peliculas where valoracion = ?;";
        List<Pelicula> peliculas = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, valoracion);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id          = resultSet.getString(1);
                String titulo      = resultSet.getString(2);
                String sinopsis    = resultSet.getString(3);
                String imagen      = resultSet.getString(4);
                String descarga    = resultSet.getString(5);
                float  duracion    = resultSet.getFloat(6);
                String director    = resultSet.getString(7);
               // int    valoracion  = resultSet.getInt(8);
                Pelicula pelicula = new Pelicula(id, titulo, sinopsis, imagen, descarga, duracion,
                        director, valoracion);
                peliculas.add(pelicula);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return peliculas;
    }

    @Override
    public List<Pelicula> obtenerTodasPeliculasOrdenadasPorValoracion() {
        String sql = "select * from vista_peliculas ORDER BY valoracion DESC;";
        List<Pelicula> peliculas = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String id          = resultSet.getString(1);
                String titulo      = resultSet.getString(2);
                String sinopsis    = resultSet.getString(3);
                String imagen      = resultSet.getString(4);
                String descarga    = resultSet.getString(5);
                float  duracion    = resultSet.getFloat(6);
                String director    = resultSet.getString(7);
                int    valoracion  = resultSet.getInt(8);
                Pelicula pelicula = new Pelicula(id, titulo, sinopsis, imagen, descarga, duracion,
                        director, valoracion);
                peliculas.add(pelicula);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return peliculas;
    }

    @Override
    public Pelicula actualizarValoracionPelicula(String idPelicula, int nuevaValoracion ) {
        String sql = " UPDATE pelicula SET valoracion = ? WHERE idPelicula = ?;";
        int rows = 0;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, nuevaValoracion);
            statement.setString(2, idPelicula);
            rows = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (rows == 0)
            return null;
        return obtenerPeliculaPorId(idPelicula);
    }


    public static void main(String[] args) throws ValoracionException {
        Pelicula newPelicula = new Pelicula( "Capitan America", "Superheroes",
                "/home/imagenes/cAmerica.png","/home/descarga/cAmerica.avi",
                200, "John Carpenter", 4);
        PeliuculaDAO dao = new PeliculaDAOImpl();
        dao.insertarPelicula(newPelicula);
        //boolean exito = dao.borrarPeliculaPorId("10");
        //System.out.printf("Borrada película? %B%n", exito);
       // Pelicula pelicula = dao.obtenerPeliculaPorId("11");
        //System.out.println(pelicula);
       // List<Pelicula> peliculas = dao.obtenerTodasPeliculas();
        //peliculas.forEach(System.out::println);
        List<Pelicula> peliculas = dao.obtenerPeliculasSegunValoracion(3);
        peliculas.forEach(System.out::println);
    }
}
