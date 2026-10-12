package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public List<Object[]> obtenerLibrosMasPrestados()
            throws SQLException {

        List<Object[]> filas = new ArrayList<>();

        String sql = """
                SELECT
                    l.id,
                    l.titulo,
                    l.autor,
                    COUNT(p.id) AS cantidad_prestamos
                FROM libros l
                LEFT JOIN prestamos p
                    ON l.id = p.id_libro
                GROUP BY
                    l.id,
                    l.titulo,
                    l.autor
                ORDER BY cantidad_prestamos DESC, l.titulo
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (
                PreparedStatement statement = conexion.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            while (resultado.next()) {
                filas.add(new Object[]{
                        resultado.getInt("id"),
                        resultado.getString("titulo"),
                        resultado.getString("autor"),
                        resultado.getInt("cantidad_prestamos")
                });
            }
        }

        return filas;
    }

    public List<Object[]> obtenerHistorialPorEstudiante(
            int idEstudiante
    ) throws SQLException {

        List<Object[]> filas = new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    l.titulo,
                    l.autor,
                    p.fecha_prestamo,
                    p.fecha_devolucion,
                    p.devuelto
                FROM prestamos p
                INNER JOIN libros l
                    ON p.id_libro = l.id
                WHERE p.id_estudiante = ?
                ORDER BY p.fecha_prestamo DESC
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, idEstudiante);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    filas.add(new Object[]{
                            resultado.getInt("id"),
                            resultado.getString("titulo"),
                            resultado.getString("autor"),
                            resultado.getDate(
                                    "fecha_prestamo"
                            ).toLocalDate(),
                            resultado.getDate(
                                    "fecha_devolucion"
                            ).toLocalDate(),
                            resultado.getBoolean("devuelto")
                    });
                }
            }
        }

        return filas;
    }

    public List<Object[]> obtenerLibrosPrestados()
            throws SQLException {

        List<Object[]> filas = new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    e.nombre AS estudiante,
                    e.curso,
                    l.titulo,
                    l.autor,
                    p.fecha_prestamo,
                    p.fecha_devolucion
                FROM prestamos p
                INNER JOIN estudiantes e
                    ON p.id_estudiante = e.id
                INNER JOIN libros l
                    ON p.id_libro = l.id
                WHERE p.devuelto = FALSE
                ORDER BY p.fecha_devolucion
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (
                PreparedStatement statement = conexion.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            while (resultado.next()) {
                filas.add(new Object[]{
                        resultado.getInt("id"),
                        resultado.getString("estudiante"),
                        resultado.getString("curso"),
                        resultado.getString("titulo"),
                        resultado.getString("autor"),
                        resultado.getDate(
                                "fecha_prestamo"
                        ).toLocalDate(),
                        resultado.getDate(
                                "fecha_devolucion"
                        ).toLocalDate()
                });
            }
        }

        return filas;
    }
}