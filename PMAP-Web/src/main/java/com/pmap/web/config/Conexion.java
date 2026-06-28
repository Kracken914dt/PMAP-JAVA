package com.pmap.web.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class Conexion {

    private static final String CONFIG_PROPERTY = "pmap.config";
    private static final String DEFAULT_CONFIG_PATH = "config/db.properties";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se pudo cargar el driver MySQL: " + e.getMessage());
        }
    }

    private Conexion() {
    }

    public static Connection getConnection() throws SQLException {
        Properties configuracion = cargarConfiguracion();
        String url = obtenerValor("pmap.db.url", "PMAP_DB_URL", configuracion);
        String usuario = obtenerValor("pmap.db.user", "PMAP_DB_USER", configuracion);
        String clave = obtenerValor("pmap.db.password", "PMAP_DB_PASSWORD", configuracion);
        return DriverManager.getConnection(url, usuario, clave);
    }

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        String rutaConfiguracion = System.getProperty(CONFIG_PROPERTY, DEFAULT_CONFIG_PATH);

        try {
            Path ruta = Path.of(rutaConfiguracion);
            if (Files.exists(ruta)) {
                try (InputStream entrada = Files.newInputStream(ruta)) {
                    propiedades.load(entrada);
                }
            }
        } catch (IOException ignored) {
            // Si el archivo externo no existe o no puede leerse, se usan variables de entorno o valores por defecto.
        }

        return propiedades;
    }

    private static String obtenerValor(String claveSistema, String claveEntorno, Properties propiedades) throws SQLException {
        String valor = System.getProperty(claveSistema);
        if (valor != null && !valor.isBlank()) {
            return valor;
        }

        valor = System.getenv(claveEntorno);
        if (valor != null && !valor.isBlank()) {
            return valor;
        }

        valor = propiedades.getProperty(claveSistema);
        if (valor != null && !valor.isBlank()) {
            return valor;
        }

        throw new SQLException("Falta configuración de base de datos: define " + claveSistema + " en config/db.properties o " + claveEntorno + " como variable de entorno.");
    }
}
