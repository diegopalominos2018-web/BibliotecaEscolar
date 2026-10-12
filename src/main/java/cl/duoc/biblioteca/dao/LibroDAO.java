package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public boolean crear(Libro libro) throws SQLException {
        String sql = """
                INSERT INTO libros (
                    titulo,
                    autor,
                    isbn,
                    editorial,
                    stock,
                    id_categoria
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getIdCategoria());

            return statement.executeUpdate() > 0;
        }
    }

    public List<Libro> listarTodos() throws SQLException {
        List<Libro> libros = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    titulo,
                    autor,
                    isbn,
                    editorial,
                    stock,
                    id_categoria
                FROM libros
                ORDER BY titulo
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (
                PreparedStatement statement = conexion.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            while (resultado.next()) {
                Libro libro = mapearLibro(resultado);

                libros.add(libro);
            }
        }

        return libros;
    }

    public Libro buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT
                    id,
                    titulo,
                    autor,
                    isbn,
                    editorial,
                    stock,
                    id_categoria
                FROM libros
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return mapearLibro(resultado);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Libro libro) throws SQLException {
        String sql = """
                UPDATE libros
                SET titulo = ?,
                    autor = ?,
                    isbn = ?,
                    editorial = ?,
                    stock = ?,
                    id_categoria = ?
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getIdCategoria());
            statement.setInt(7, libro.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = """
                DELETE FROM libros
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

    private Libro mapearLibro(ResultSet resultado)
            throws SQLException {

        return new Libro(
                resultado.getInt("id"),
                resultado.getString("titulo"),
                resultado.getString("autor"),
                resultado.getString("isbn"),
                resultado.getString("editorial"),
                resultado.getInt("stock"),
                resultado.getInt("id_categoria")
        );
    }
}