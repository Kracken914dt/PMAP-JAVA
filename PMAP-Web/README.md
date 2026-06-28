# PMAP Web

Aplicación web del módulo Gestión de Materias.

## Tecnologías

- Java 17
- JSP
- Servlets
- JDBC puro
- MySQL
- Apache Tomcat 10
- Maven

## Ejecución

En mi entorno, este proyecto lo ejecute con WildFly. El WAR generado por Maven se publica en WildFly y desde allí se accede a la aplicación desde el navegador, aunque lo puede hacer con tomcat.

## Punto de entrada

- [index.jsp](src/main/webapp/index.jsp)
- Servlet: [com.pmap.web.servlet.MateriaServlet](src/main/java/com/pmap/web/servlet/MateriaServlet.java)

## Funcionalidades

- Crear materias.
- Listar materias en tabla HTML.
- Buscar materias.
- Editar materias.
- Eliminar materias.

## Nota sobre Tomcat 10

Tomcat 10 usa Jakarta Servlet. Por eso el proyecto incluye dependencias `jakarta.servlet` y JSTL Jakarta.
