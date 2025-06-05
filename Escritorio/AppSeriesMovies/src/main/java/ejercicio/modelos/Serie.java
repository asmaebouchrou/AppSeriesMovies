    package ejercicio.modelos;

    import java.util.ArrayList;
    import java.util.List;

    public class Serie extends Video{

        //private String idSerie;
        private List<Temporada> temporadas = new ArrayList<>();
        private transient int numTemporadas; // Usamos transient para evitar problemas de serialización
        // Método para obtener el número de temporadas
        public int getNumTemporadas() {
            return this.numTemporadas;
        }
        // Método para establecer el número de temporadas
        public void setNumTemporadas(int numTemporadas) {
            this.numTemporadas = numTemporadas;
        }

        public Serie(String titulo, String sinopsis, String imagen, String descarga, float duracion) {
            super(titulo, sinopsis, imagen, descarga, duracion);
            //this.idSerie = id;
        }
        public Serie(String idSerie, String titulo, String sinopsis, String imagen, String descarga, float duracion) {
            super(idSerie, titulo, sinopsis, imagen, descarga, duracion);
            //this.idSerie = id;
        }

       /* public String getIdSerie() {
            return idSerie;
        }*/
        //añadimos temporadas
        public boolean addTemporada (Temporada temporada) {
            return temporadas.add(temporada);
        }

        public List<Temporada> getTemporadas() {
            return temporadas;
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%s,%.0f", getIdVideo(), getTitulo(), getSinopsis(),
                    getImagen(), getDescarga(), getDuracion());
        }
        public int getNumeroTemporadas() {
            return temporadas.size();
        }
    }
