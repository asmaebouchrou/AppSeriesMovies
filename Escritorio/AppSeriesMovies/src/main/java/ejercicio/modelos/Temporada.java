package ejercicio.modelos;

import java.util.ArrayList;
import java.util.List;

/*numeroTemporada INTEGER PRIMARY KEY AUTOINCREMENT,
    serie_id INTEGER NOT NULL,
    nombre TEXT,*/
public class Temporada {
    private int numeroTemporada;
    private final String idSerie;
    private String nombre;
    private List<Capitulo> capitulos = new ArrayList<>();

    public Temporada(String idSerie, String nombre, int numeroTemporada) {
        this.idSerie = idSerie;
        this.nombre = nombre;
        this.numeroTemporada = numeroTemporada;
    }

    public Temporada(String idSerie, String nombre) {
        this.idSerie = idSerie;
        this.nombre = nombre;
    }

    public int getNumeroTemporada() {
        return numeroTemporada;
    }

    public String getIdSerie() {
        return idSerie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //añadimos capítulos
    public boolean addCapitulo (Capitulo capitulo) {
        return capitulos.add(capitulo);
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }
}







