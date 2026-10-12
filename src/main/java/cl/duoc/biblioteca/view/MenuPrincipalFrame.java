package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.EstilosUI.EstilosUI;
import cl.duoc.biblioteca.model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class MenuPrincipalFrame extends JFrame {

    private final Usuario usuario;

    public MenuPrincipalFrame(Usuario usuario) {
        this.usuario = usuario;

        configurarVentana();
        crearContenido();
    }

    private void configurarVentana() {
        setTitle("Biblioteca Escolar - Menú principal");
        setSize(720, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void crearContenido() {
        JPanel panelPrincipal = new JPanel(
                new BorderLayout(0, 15)
        );

        panelPrincipal.setBackground(
                EstilosUI.FONDO_VENTANA
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(30, 55, 30, 55)
        );

        JLabel lblTitulo = new JLabel(
                "Biblioteca Escolar",
                SwingConstants.CENTER
        );

        EstilosUI.aplicarTitulo(lblTitulo);

        JLabel lblBienvenida = new JLabel(
                "Bienvenido(a), "
                        + usuario.getNombre()
                        + "  |  Rol: "
                        + usuario.getRol(),
                SwingConstants.CENTER
        );

        EstilosUI.aplicarSubtitulo(lblBienvenida);

        JPanel panelEncabezado = new JPanel(
                new GridLayout(2, 1, 0, 8)
        );

        panelEncabezado.setOpaque(false);

        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(lblBienvenida);

        JButton btnEstudiantes = new JButton(
                "Gestionar estudiantes"
        );

        JButton btnLibros = new JButton(
                "Gestionar libros"
        );

        JButton btnCategorias = new JButton(
                "Gestionar categorías"
        );

        JButton btnPrestamos = new JButton(
                "Gestionar préstamos"
        );

        JButton btnReportes = new JButton(
                "Ver reportes"
        );

        JButton btnCerrarSesion = new JButton(
                "Cerrar sesión"
        );

        Dimension tamanoBoton = new Dimension(310, 48);

        btnEstudiantes.setPreferredSize(tamanoBoton);
        btnLibros.setPreferredSize(tamanoBoton);
        btnCategorias.setPreferredSize(tamanoBoton);
        btnPrestamos.setPreferredSize(tamanoBoton);
        btnReportes.setPreferredSize(tamanoBoton);
        btnCerrarSesion.setPreferredSize(tamanoBoton);

        EstilosUI.aplicarBotonPrincipal(btnEstudiantes);
        EstilosUI.aplicarBotonPrincipal(btnLibros);
        EstilosUI.aplicarBotonPrincipal(btnCategorias);

        EstilosUI.aplicarBotonSecundario(btnPrestamos);
        EstilosUI.aplicarBotonSecundario(btnReportes);

        EstilosUI.aplicarBotonPeligro(btnCerrarSesion);

        JPanel panelBotones = new JPanel(
                new GridLayout(6, 1, 0, 14)
        );

        panelBotones.setBackground(
                EstilosUI.FONDO_PANEL
        );

        panelBotones.setBorder(
                EstilosUI.crearBordePanel()
        );

        if (usuario.esBibliotecario()) {
            panelBotones.add(btnEstudiantes);
            panelBotones.add(btnLibros);
            panelBotones.add(btnCategorias);
        }

        panelBotones.add(btnPrestamos);
        panelBotones.add(btnReportes);
        panelBotones.add(btnCerrarSesion);

        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        add(panelPrincipal);

        btnEstudiantes.addActionListener(evento -> {
            EstudianteFrame ventanaEstudiantes =
                    new EstudianteFrame();

            ventanaEstudiantes.setVisible(true);
        });

        btnLibros.addActionListener(evento -> {
            LibroFrame ventanaLibros = new LibroFrame();

            ventanaLibros.setVisible(true);
        });

        btnCategorias.addActionListener(evento -> {
            CategoriaFrame ventanaCategorias =
                    new CategoriaFrame();

            ventanaCategorias.setVisible(true);
        });

        btnPrestamos.addActionListener(evento -> {
            PrestamoFrame ventanaPrestamos =
                    new PrestamoFrame();

            ventanaPrestamos.setVisible(true);
        });

        btnReportes.addActionListener(evento -> {
            ReporteFrame ventanaReportes =
                    new ReporteFrame();

            ventanaReportes.setVisible(true);
        });

        btnCerrarSesion.addActionListener(evento -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea cerrar la sesión actual?",
                    "Cerrar sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                LoginFrame ventanaLogin = new LoginFrame();

                ventanaLogin.setVisible(true);

                dispose();
            }
        });
    }
}
