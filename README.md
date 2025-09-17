# AppSeriesMovies

## Descripción

**AppSeriesMovies** es una aplicación de escritorio desarrollada en Java utilizando JavaFX, que permite gestionar y visualizar información sobre películas y series. El usuario puede agregar, visualizar, editar y eliminar tanto películas como series, incluyendo temporadas y capítulos para estas últimas. Interfaz orientada al manejo fácil de contenidos multimedia.

## Características principales

- **Gestión de películas:** Alta, baja, modificación y listado de películas, incluyendo detalles como título, director, sinopsis, imagen, descarga, duración y valoración.
- **Gestión de series:** Registro y administración de series, temporadas y capítulos, con soporte para visualizar y editar información relevante.
- **Interfaz gráfica moderna:** Basada en JavaFX, con estilos personalizados mediante CSS para una experiencia visual atractiva.
- **Persistencia:** Utiliza SQLite para almacenar y recuperar los datos de películas, series, temporadas y capítulos, gestionando transacciones y relaciones entre tablas.
- **Arquitectura modular:** Separación de lógica en controladores, modelos y DAOs, siguiendo buenas prácticas de desarrollo Java.
- **Validaciones y manejo de errores:** Incluye validación de datos y notificaciones visuales para errores o acciones exitosas.

## Tecnologías utilizadas

- **Java 17+**
- **JavaFX** (Controles y FXML)
- **SQLite** (mediante dependencia org.xerial.sqlitejdbc)
- **CSS** para la personalización visual de la interfaz

## Estructura del proyecto

```
Escritorio/AppSeriesMovies/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── ejercicio/
│   │   │   │   ├── DAOs/           # Implementaciones de acceso a datos (Películas, Series)
│   │   │   │   ├── controllers/    # Controladores JavaFX
│   │   │   │   ├── modelos/        # Clases modelo: Película, Serie, Temporada, Capítulo
│   │   │   │   └── Conexion.java   # Singleton de conexión a SQLite
│   │   │   └── org/example/appseriesmovies/MainApp.java # Main JavaFX
│   │   ├── resources/
│   │   │   ├── views/              # Archivos FXML de las vistas
│   │   │   ├── images/             # Imágenes del proyecto
│   │   │   └── styles/modern.css   # Estilos CSS
├── README.md
```

## Ejecución

1. Clona el repositorio:
    ```bash
    git clone https://github.com/asmaebouchrou/AppSeriesMovies.git
    ```
2. Abre el proyecto en tu IDE Java compatible (por ejemplo, IntelliJ o Eclipse).
3. Asegúrate de tener configuradas las dependencias de JavaFX y SQLite.
4. Ejecuta la clase `org.example.appseriesmovies.MainApp` para iniciar la aplicación.
