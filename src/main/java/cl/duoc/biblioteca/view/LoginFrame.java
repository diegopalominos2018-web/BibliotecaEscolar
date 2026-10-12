package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.dao.UsuarioDAO;
import cl.duoc.biblioteca.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private final JTextField txtCorreo;
    private final JPasswordField txtContrasena;

    public LoginFrame() {
        setTitle("Biblioteca Escolar - Inicio de sesión");
        setSize(450, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel lblTitulo = new JLabel(
                "Biblioteca Escolar",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(31, 78, 121));

        lblTitulo.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 10, 10)
        );

        JLabel lblSubtitulo = new JLabel(
                "Ingrese sus credenciales para continuar",
                SwingConstants.CENTER
        );

        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel panelEncabezado = new JPanel(new BorderLayout());

        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblSubtitulo, BorderLayout.SOUTH);

        txtCorreo = new JTextField(20);
        txtContrasena = new JPasswordField(20);

        JPanel panelFormulario = new JPanel(new GridBagLayout());

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(25, 45, 15, 45)
        );

        GridBagConstraints restricciones = new GridBagConstraints();

        restricciones.insets = new Insets(8, 8, 8, 8);
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.weightx = 0;

        panelFormulario.add(
                new JLabel("Correo:"),
                restricciones
        );

        restricciones.gridx = 1;
        restricciones.gridy = 0;
        restricciones.weightx = 1.0;

        panelFormulario.add(txtCorreo, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.weightx = 0;

        panelFormulario.add(
                new JLabel("Contraseña:"),
                restricciones
        );

        restricciones.gridx = 1;
        restricciones.gridy = 1;
        restricciones.weightx = 1.0;

        panelFormulario.add(txtContrasena, restricciones);

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnSalir = new JButton("Salir");

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 10)
        );

        panelBotones.add(btnIngresar);
        panelBotones.add(btnSalir);

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnIngresar.addActionListener(
                evento -> iniciarSesion()
        );

        btnSalir.addActionListener(
                evento -> System.exit(0)
        );

        getRootPane().setDefaultButton(btnIngresar);
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();

        String contrasena = new String(
                txtContrasena.getPassword()
        );

        if (correo.isBlank() || contrasena.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el correo y la contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            Usuario usuario = usuarioDAO.autenticar(
                    correo,
                    contrasena
            );

            if (usuario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Correo o contraseña incorrectos.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE
                );

                txtContrasena.setText("");
                txtContrasena.requestFocus();

                return;
            }

            MenuPrincipalFrame menuPrincipal =
                    new MenuPrincipalFrame(usuario);

            menuPrincipal.setVisible(true);

            dispose();

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible iniciar sesión.\n\n"
                            + "Detalle técnico: "
                            + error.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
