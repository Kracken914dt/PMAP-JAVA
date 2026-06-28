package com.pmap.web.dao;

import com.pmap.web.config.Conexion;
import com.pmap.web.model.Materia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    private static final String SQL_INSERTAR = "INSERT INTO materias (nombre, descripcion, categoria, estado) VALUES (?, ?, ?, ?)";
    private static final String SQL_LISTAR = "SELECT id, nombre, descripcion, categoria, estado FROM materias ORDER BY id";
    private static final String SQL_BUSCAR_POR_ID = "SELECT id, nombre, descripcion, categoria, estado FROM materias WHERE id = ?";
    private static final String SQL_ACTUALIZAR = "UPDATE materias SET nombre = ?, descripcion = ?, categoria = ?, estado = ? WHERE id = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM materias WHERE id = ?";

    public int insertar(Materia materia) throws SQLException {
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, materia.getNombre());
            sentencia.setString(2, materia.getDescripcion());
            sentencia.setString(3, materia.getCategoria());
            sentencia.setString(4, materia.getEstado());

            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
            }
        }
        return -1;
    }

    public List<Materia> listar() throws SQLException {
        List<Materia> materias = new ArrayList<>();

        try (Connection conexion = Conexion.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_LISTAR);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                materias.add(mapearMateria(resultado));
            }
        }

        return materias;
    }

    public Materia buscarPorId(int id) throws SQLException {
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_ID)) {

            sentencia.setInt(1, id);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearMateria(resultado);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Materia materia) throws SQLException {
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {

            sentencia.setString(1, materia.getNombre());
            sentencia.setString(2, materia.getDescripcion());
            sentencia.setString(3, materia.getCategoria());
            sentencia.setString(4, materia.getEstado());
            sentencia.setInt(5, materia.getId());

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {

            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        }
    }

    private Materia mapearMateria(ResultSet resultado) throws SQLException {
        Materia materia = new Materia();
        materia.setId(resultado.getInt("id"));
        materia.setNombre(resultado.getString("nombre"));
        materia.setDescripcion(resultado.getString("descripcion"));
        materia.setCategoria(resultado.getString("categoria"));
        materia.setEstado(resultado.getString("estado"));
        return materia;
    }
}
