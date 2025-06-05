--indice sobre titulo
DROP INDEX IF EXISTS titulo;
CREATE INDEX titulo ON video(titulo);

-- Vista de películas con todos sus datos
DROP VIEW IF EXISTS vista_peliculas;
CREATE VIEW vista_peliculas AS
SELECT
    v.idVideo AS video_id,
    v.titulo,
    v.sinopsis,
    v.imagen,
    v.descarga,
    v.duracion,
    p.director,
    p.valoracion
FROM video v
JOIN pelicula p ON v.idVideo = p.idPelicula;

-- Vista de series con todos sus datos, incluyendo temporadas y capítulos
DROP VIEW IF EXISTS vista_series;
CREATE VIEW vista_series AS
SELECT
    v.id AS video_id,
    v.titulo AS serie_titulo,
    v.sinopsis,
    v.imagen,
    v.descarga,
    v.duracion AS duracion_media_serie,
    t.numeroTemporada AS temporada_id,
    t.nombre AS nombre_temporada,
    c.numeroCapitulo AS capitulo_id,
    c.nombre AS nombre_capitulo,
    c.duracion AS duracion_capitulo
FROM video v
JOIN serie s ON v.id = s.id
JOIN temporada t ON s.id = t.serie_id
JOIN capitulo c ON t.id = c.temporada_id;

