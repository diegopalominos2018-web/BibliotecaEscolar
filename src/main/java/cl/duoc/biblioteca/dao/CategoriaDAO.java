package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean crear(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, categoria.getNombre());

            return statement.executeUpdate() > 0;
        }
    }

    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();

        String sql = """
                SELECT id, nombre
                FROM categorias
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
                Categoria categoria = new Categoria(
                        resultado.getInt("id"),
                        resultado.getString("nombre")
                );

                categorias.add(categoria);
            }
        }

        return categorias;
    }

    public Categoria buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT id, nombre
                FROM categorias
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return new Categoria(
                            resultado.getInt("id"),
                            resultado.getString("nombre")
                    );
                }
            }
        }

        return null;
    }

    public boolean actualizar(Categoria categoria) throws SQLException {
        String sql = """
                UPDATE categorias
                SET nombre = ?
                WHERE id = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, categoria.getNombre());
            statement.setInt(2, categoria.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id = ?";

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }
}
