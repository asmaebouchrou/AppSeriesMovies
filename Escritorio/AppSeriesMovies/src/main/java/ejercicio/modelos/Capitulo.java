                                                                                                        package ejercicio.modelos;

                                                                                                        import java.lang.ref.PhantomReference;

                                                                                                        /*numeroCapitulo INTEGER PRIMARY KEY AUTOINCREMENT,
                                                                                                            temporada_id INTEGER NOT NULL,
                                                                                                            nombre TEXT NOT NULL,
                                                                                                            duracion REAL,*/
                                                                                                        public class Capitulo {
                                                                                                            private int numeroCapitulo;
                                                                                                            private final int temporadaId;
                                                                                                            private final String serieId;  // Nuevo campo
                                                                                                            private String nombreCapitulo;
                                                                                                            private float duracionCapitulo;

                                                                                                            // Constructor actualizado
                                                                                                            public Capitulo(int temporadaId, String serieId, String nombreCapitulo, float duracionCapitulo) {
                                                                                                                this.temporadaId = temporadaId;
                                                                                                                this.serieId = serieId;
                                                                                                                this.nombreCapitulo = nombreCapitulo;
                                                                                                                this.duracionCapitulo = duracionCapitulo;
                                                                                                            }

                                                                                                            // Getter para serieId
                                                                                                            public String getSerieId() {
                                                                                                                return serieId;
                                                                                                            }

                                                                                                            public int getNumeroCapitulo() {
                                                                                                                return numeroCapitulo;
                                                                                                            }

                                                                                                            public void setNumeroCapitulo(int numeroCapitulo) {
                                                                                                                this.numeroCapitulo = numeroCapitulo;
                                                                                                            }

                                                                                                            public int getTemporadaId() {
                                                                                                                return temporadaId;
                                                                                                            }

                                                                                                            public String getNombreCapitulo() {
                                                                                                                return nombreCapitulo;
                                                                                                            }

                                                                                                            public void setNombreCapitulo(String nombreCapitulo) {
                                                                                                                this.nombreCapitulo = nombreCapitulo;
                                                                                                            }

                                                                                                            public float getDuracionCapitulo() {
                                                                                                                return duracionCapitulo;
                                                                                                            }

                                                                                                            public void setDuracionCapitulo(float duracionCapitulo) {
                                                                                                                this.duracionCapitulo = duracionCapitulo;
                                                                                                            }
                                                                                                        }
