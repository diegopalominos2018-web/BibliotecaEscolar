package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DatabaseConnection;
import cl.duoc.biblioteca.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticar(String correo, String contrasena)
            throws SQLException {

        String sql = """
                SELECT id, nombre, rut, correo, `contraseña`, rol
                FROM usuarios
                WHERE correo = ?
                  AND `contraseña` = ?
                """;

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, correo);
            statement.setString(2, contrasena);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    return new Usuario(
                            resultado.getInt("id"),
                            resultado.getString("nombre"),
                            resultado.getString("rut"),
                            resultado.getString("correo"),
                            resultado.getString("contraseña"),
                            resultado.getString("rol")
                    );
                }
            }
        }

        return null;
    }
}
