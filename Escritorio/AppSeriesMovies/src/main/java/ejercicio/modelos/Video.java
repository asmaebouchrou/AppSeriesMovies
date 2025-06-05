    package ejercicio.modelos;

    import ejercicio.Helper;

    /*idVideo TEXT  PRIMARY KEY,
        titulo TEXT NOT NULL,
        sinopsis TEXT,
        imagen TEXT,
        descarga TEXT,
        duracion REAL*/
    public class Video {
        private final String idVideo;
        private String titulo;
        private String sinopsis;
        private String imagen;
        private String descarga;
        private float duracion;
        //select
        public Video(String idVideo, String titulo, String sinopsis, String imagen, String descarga, float duracion) {
            this.idVideo = idVideo;
            this.titulo = titulo;
            this.sinopsis = sinopsis;
            this.imagen = imagen;
            this.descarga = descarga;
            this.duracion = duracion;
        }
        //insert
        public Video(String titulo, String sinopsis,
                     String imagen, String descarga, float duracion) {
            this.idVideo = Helper.generarUUID();
            this.titulo = titulo;
            this.sinopsis = sinopsis;
            this.imagen = imagen;
            this.descarga = descarga;
            this.duracion = duracion;
        }

        public String getIdVideo() {
            return idVideo;
        }

        /*public void setIdVideo(String idVideo) {
            this.idVideo = idVideo;
        }*/

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getSinopsis() {
            return sinopsis;
        }

        public void setSinopsis(String sinopsis) {
            this.sinopsis = sinopsis;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }

        public String getDescarga() {
            return descarga;
        }

        public void setDescarga(String descarga) {
            this.descarga = descarga;
        }

        public float getDuracion() {
            return duracion;
        }

        public void setDuracion(float duracion) {
            this.duracion = duracion;
        }

    }
