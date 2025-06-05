package ejercicio.modelos;

import ejercicio.excepciones.ValoracionException;

/*idPelicula TEXT PRIMARY KEY,  -- también es clave foránea hacia video
    director TEXT,
    valoracion INTEGER CHECK (valoracion BETWEEN 1 AND 5),*/
public class Pelicula extends Video{
    //private String idPelicula;
    private String director;
    private int valoracion;

    public Pelicula(String idVideo, String titulo, String sinopsis, String imagen, String descarga, float duracion,
                    String director, int valoracion) {
        super(idVideo, titulo, sinopsis, imagen, descarga, duracion);
        this.director = director;
        this.valoracion = valoracion;
    }

    public Pelicula(String titulo, String sinopsis, String imagen, String descarga, float duracion,
                    String director, int valoracion) throws ValoracionException {
        super( titulo, sinopsis, imagen, descarga, duracion);
        if (valoracion > 0 && valoracion < 6) {
           // this.idPelicula = id;
            this.director = director;
            this.valoracion = valoracion;
        } else
            throw new ValoracionException("La valoración de la película debes estar comprendida entre 1 y 5");
    }

    /*public String getIdPelicula() {
        return idPelicula;
    }*/

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%.0f,%s,%d", getIdVideo(), getTitulo(), getSinopsis(),
                getImagen(), getDescarga(), getDuracion(),getDirector(), getValoracion() );
    }
}
