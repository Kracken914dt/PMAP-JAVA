# PMAP - Gestión de Materias

Repositorio académico con dos aplicaciones Java tradicionales que comparten la misma base de datos MySQL `pmap` y la tabla `materias`.

## Aplicaciones

- PMAP-Desktop: Java 17, Swing, JDBC, Maven.
- PMAP-Web: Java 17, JSP, Servlets, JDBC, Apache Tomcat 10, Maven.

## Estructura

- [sql/pmap.sql](sql/pmap.sql): script de base de datos e inserciones de ejemplo.
- [PMAP-Desktop/README.md](PMAP-Desktop/README.md): guía del proyecto de escritorio.
- [PMAP-Web/README.md](PMAP-Web/README.md): guía del proyecto web.

## Base de datos

Importa primero el script [sql/pmap.sql](sql/pmap.sql).

## Observación técnica

Tomcat 10 usa el namespace Jakarta. Por eso el proyecto web se entrega con dependencias `jakarta.servlet` y JSTL Jakarta compatibles con esa plataforma.

