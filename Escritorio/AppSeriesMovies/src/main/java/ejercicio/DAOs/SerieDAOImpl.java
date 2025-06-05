package ejercicio.DAOs;

import ejercicio.Conexion;
import ejercicio.modelos.Capitulo;
import ejercicio.modelos.Serie;
import ejercicio.modelos.Temporada;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SerieDAOImpl implements SerieDAO{
    private Connection conexion;
    public SerieDAOImpl() {
        conexion = Conexion.getInstance().getConnection();
    }
    @Override
    public void crearSerie(Serie serie) {
        String sql1 = "INSERT INTO video VALUES (?, ?, ?, ?, ?, ?);";
        String sql2 = "INSERT INTO serie VALUES (?)";
        PreparedStatement statement = null;
        int rows1 = 0, rows2 = 0;
        //preparamos transacción
        try {
            conexion.setAutoCommit(false);
            //sentencias
            statement = conexion.prepareStatement(sql1);
            statement.setString(1, serie.getIdVideo());
            statement.setString(2, serie.getTitulo());
            statement.setString(3, serie.getSinopsis());
            statement.setString(4, serie.getImagen());
            statement.setString(5, serie.getDescarga());
            statement.setFloat (6, serie.getDuracion());
            rows1 = statement.executeUpdate();
            statement = conexion.prepareStatement(sql2);
            statement.setString(1, serie.getIdVideo());
            rows2 = statement.executeUpdate();
            conexion.commit();
        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } finally {
                System.err.println("Error en la insercción de una serie");
            }
            //throw new RuntimeException(e);

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public boolean borrarSerie(String id) {
        String sql = " delete FROM  serie WHERE idSerie = ?;";
        int rows = 0;
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, id);
            rows = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rows == 1;
    }
    @Override
    public boolean actualizarSerie(Serie serie) {
        String sql = "UPDATE video SET titulo = ?, sinopsis = ?, imagen = ?, descarga = ?, duracion = ? WHERE idVideo = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, serie.getTitulo());
            statement.setString(2, serie.getSinopsis());
            statement.setString(3, serie.getImagen());
            statement.setString(4, serie.getDescarga());
            statement.setFloat(5, serie.getDuracion());
            statement.setString(6, serie.getIdVideo());

            int filasActualizadas = statement.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la serie: " + e.getMessage());
            return false;
        }
    }



    @Override
    public List<Serie> obtenerTodasSeries() {
        List<Serie> series = new ArrayList<>();
        String sql = "SELECT v.idVideo, v.titulo, v.sinopsis, v.imagen, v.descarga, v.duracion, " +
                "COUNT(t.numeroTemporada) as numTemporadas " +
                "FROM video v " +
                "JOIN serie s ON v.idVideo = s.idSerie " +
                "LEFT JOIN temporada t ON s.idSerie = t.serie_id " +
                "GROUP BY v.idVideo, v.titulo, v.sinopsis, v.imagen, v.descarga, v.duracion";

        try (Statement statement = conexion.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String idVideo = resultSet.getString("idVideo");
                String titulo = resultSet.getString("titulo");
                String sinopsis = resultSet.getString("sinopsis");
                String imagen = resultSet.getString("imagen");
                String descarga = resultSet.getString("descarga");
                float duracion = resultSet.getFloat("duracion");
                int numTemporadas = resultSet.getInt("numTemporadas");

                Serie serie = new Serie(idVideo, titulo, sinopsis, imagen, descarga, duracion);
                serie.setNumTemporadas(numTemporadas);
                series.add(serie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las series: " + e.getMessage(), e);
        }
        return series;
    }

    @Override
    public Serie obtenerSeriePorId(String id) {
        String sql = "SELECT * FROM video where idVideo = ?;";
        Serie serie = null;
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
               // String idSerie  = resultSet.getString("idVideo");
                String titulo   = resultSet.getString("titulo");
                String sinopsis = resultSet.getString("sinopsis");
                String imagen   = resultSet.getString("imagen");
                String descarga = resultSet.getString("descarga");
                float  duracion = resultSet.getFloat("duracion");
                serie = new Serie(id, titulo, sinopsis, imagen, descarga, duracion);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return serie;
    }

    @Override
    public void crearTemporada(Temporada temporada) {
        try {
            if (existeTemporada(temporada.getNumeroTemporada(), temporada.getIdSerie())) {
                throw new SQLException("La temporada ya existe para esta serie.");
            }

            String sqlCheckSerie = "SELECT COUNT(*) FROM serie WHERE idSerie = ?";
            String sqlInsertTemporada = "INSERT INTO temporada (numeroTemporada, serie_id, nombreTemporada) VALUES (?, ?, ?)";

            try (PreparedStatement checkStmt = conexion.prepareStatement(sqlCheckSerie);
                 PreparedStatement insertStmt = conexion.prepareStatement(sqlInsertTemporada)) {

                conexion.setAutoCommit(false);

                // 1. Validar que la serie existe
                checkStmt.setString(1, temporada.getIdSerie());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new SQLException("La serie con ID " + temporada.getIdSerie() + " no existe");
                }

                // 2. Insertar temporada
                insertStmt.setInt(1, temporada.getNumeroTemporada());
                insertStmt.setString(2, temporada.getIdSerie());
                insertStmt.setString(3, temporada.getNombre());
                insertStmt.executeUpdate();

                conexion.commit();
                System.out.println("Temporada creada exitosamente");

            } catch (SQLException e) {
                conexion.rollback();
                throw new RuntimeException("Error al crear temporada: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en la operación crearTemporada: " + e.getMessage(), e);
        }
    }


    @Override
    public boolean crearCapitulo(Capitulo capitulo) {
        String sql = "INSERT INTO capitulo (temporada_id, serie_id, nombre, duracion) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, capitulo.getTemporadaId());
            stmt.setString(2, capitulo.getSerieId());
            stmt.setString(3, capitulo.getNombreCapitulo());
            stmt.setFloat(4, capitulo.getDuracionCapitulo());

            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean existeTemporada(int numeroTemporada, String idSerie) {
        String sql = "SELECT COUNT(*) FROM temporada WHERE numeroTemporada = ? AND serie_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, numeroTemporada);
            stmt.setString(2, idSerie);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar existencia de temporada: " + e.getMessage(), e);
        }
        return false;
    }


}
