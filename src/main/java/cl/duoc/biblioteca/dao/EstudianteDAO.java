package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.model.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public boolean crear(Estudiante estudiante) throws SQLException {
        String sql = """
                INSERT INTO estudiantes (
                    nombre,
                    rut,
                    curso,
                    correo
                )
                VALUES (?, ?, ?, ?)
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());

            return statement.executeUpdate() > 0;
        }
    }

    public List<Estudiante> listarTodos() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = """
                SELECT id, nombre, rut, curso, correo
                FROM estudiantes
                ORDER BY nombre
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (
                PreparedStatement statement = conexion.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            while (resultado.next()) {
                Estudiante estudiante = mapearEstudiante(resultado);
                estudiantes.add(estudiante);
            }
        }

        return estudiantes;
    }

    public Estudiante buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT id, nombre, rut, curso, correo
                FROM estudiantes
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return mapearEstudiante(resultado);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Estudiante estudiante) throws SQLException {
        String sql = """
                UPDATE estudiantes
                SET nombre = ?,
                    rut = ?,
                    curso = ?,
                    correo = ?
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());
            statement.setInt(5, estudiante.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    private Estudiante mapearEstudiante(ResultSet resultado)
            throws SQLException {

        return new Estudiante(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("rut"),
                resultado.getString("curso"),
                resultado.getString("correo")
        );
    }
}