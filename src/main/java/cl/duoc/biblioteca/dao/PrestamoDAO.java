package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.model.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public boolean crear(Prestamo prestamo) throws SQLException {
        String sql = """
                INSERT INTO prestamos (
                    id_estudiante,
                    id_libro,
                    fecha_prestamo,
                    fecha_devolucion,
                    devuelto
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, prestamo.getIdEstudiante());
            statement.setInt(2, prestamo.getIdLibro());

            statement.setDate(
                    3,
                    Date.valueOf(prestamo.getFechaPrestamo())
            );

            statement.setDate(
                    4,
                    Date.valueOf(prestamo.getFechaDevolucion())
            );

            statement.setBoolean(5, prestamo.isDevuelto());

            return statement.executeUpdate() > 0;
        }
    }

    public List<Prestamo> listarTodos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    id_estudiante,
                    id_libro,
                    fecha_prestamo,
                    fecha_devolucion,
                    devuelto
                FROM prestamos
                ORDER BY devuelto, fecha_devolucion
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (
                PreparedStatement statement = conexion.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            while (resultado.next()) {
                prestamos.add(
                        crearPrestamoDesdeResultado(resultado)
                );
            }
        }

        return prestamos;
    }

    public Prestamo buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT
                    id,
                    id_estudiante,
                    id_libro,
                    fecha_prestamo,
                    fecha_devolucion,
                    devuelto
                FROM prestamos
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return crearPrestamoDesdeResultado(resultado);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Prestamo prestamo) throws SQLException {
        String sql = """
                UPDATE prestamos
                SET id_estudiante = ?,
                    id_libro = ?,
                    fecha_prestamo = ?,
                    fecha_devolucion = ?,
                    devuelto = ?
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, prestamo.getIdEstudiante());
            statement.setInt(2, prestamo.getIdLibro());

            statement.setDate(
                    3,
                    Date.valueOf(prestamo.getFechaPrestamo())
            );

            statement.setDate(
                    4,
                    Date.valueOf(prestamo.getFechaDevolucion())
            );

            statement.setBoolean(5, prestamo.isDevuelto());
            statement.setInt(6, prestamo.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean registrarDevolucion(int idPrestamo)
            throws SQLException {

        String sql = """
                UPDATE prestamos
                SET devuelto = TRUE
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, idPrestamo);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = """
                DELETE FROM prestamos
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    private Prestamo crearPrestamoDesdeResultado(
            ResultSet resultado
    ) throws SQLException {
        return new Prestamo(
                resultado.getInt("id"),
                resultado.getInt("id_estudiante"),
                resultado.getInt("id_libro"),
                resultado.getDate(
                        "fecha_prestamo"
                ).toLocalDate(),
                resultado.getDate(
                        "fecha_devolucion"
                ).toLocalDate(),
                resultado.getBoolean("devuelto")
        );
    }
}
