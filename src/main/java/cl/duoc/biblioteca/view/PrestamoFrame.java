package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.dao.PrestamoDAO;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.model.Libro;
import cl.duoc.biblioteca.model.Prestamo;
import cl.duoc.biblioteca.service.PrestamoService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrestamoFrame extends JFrame {

    private final EstudianteDAO estudianteDAO;
    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;
    private final PrestamoService prestamoService;

    private final JComboBox<Estudiante> cmbEstudiante;
    private final JComboBox<Libro> cmbLibro;

    private final DefaultTableModel modeloTabla;
    private final JTable tablaPrestamos;

    private final JLabel lblInformacionLibro;
    private final JButton btnRegistrarPrestamo;
    private final JButton btnRegistrarDevolucion;

    public PrestamoFrame() {
        estudianteDAO = new EstudianteDAO();
        libroDAO = new LibroDAO();
        prestamoDAO = new PrestamoDAO();
        prestamoService = new PrestamoService();

        setTitle("Biblioteca Escolar - Préstamos y devoluciones");
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel(
                "Gestión de préstamos y devoluciones",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(31, 78, 121));

        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 8, 10)
        );

        JLabel subtitulo = new JLabel(
                "Seleccione un estudiante y un libro disponible para registrar un préstamo.",
                SwingConstants.CENTER
        );

        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        subtitulo.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 10, 10)
        );

        JPanel panelEncabezado = new JPanel(new BorderLayout());

        panelEncabezado.add(titulo, BorderLayout.CENTER);
        panelEncabezado.add(subtitulo, BorderLayout.SOUTH);

        cmbEstudiante = new JComboBox<>();
        cmbLibro = new JComboBox<>();

        lblInformacionLibro = new JLabel(
                "Seleccione un libro para consultar su disponibilidad."
        );

        lblInformacionLibro.setFont(
                new Font("SansSerif", Font.ITALIC, 13)
        );

        JPanel panelFormulario = new JPanel(new GridBagLayout());

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Registrar nuevo préstamo"
                )
        );

        GridBagConstraints restricciones = new GridBagConstraints();

        restricciones.insets = new Insets(6, 8, 6, 8);
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.weightx = 0;

        panelFormulario.add(
                new JLabel("Estudiante:"),
                restricciones
        );

        restricciones.gridx = 1;
        restricciones.gridy = 0;
        restricciones.weightx = 1.0;

        panelFormulario.add(cmbEstudiante, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 1;
        restricciones.weightx = 0;

        panelFormulario.add(
                new JLabel("Libro:"),
                restricciones
        );

        restricciones.gridx = 1;
        restricciones.gridy = 1;
        restricciones.weightx = 1.0;

        panelFormulario.add(cmbLibro, restricciones);

        restricciones.gridx = 1;
        restricciones.gridy = 2;
        restricciones.weightx = 1.0;

        panelFormulario.add(lblInformacionLibro, restricciones);

        btnRegistrarPrestamo = new JButton(
                "Registrar préstamo"
        );

        btnRegistrarDevolucion = new JButton(
                "Registrar devolución seleccionada"
        );

        JButton btnActualizar = new JButton(
                "Actualizar información"
        );

        JButton btnCerrar = new JButton("Cerrar");

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        panelBotones.add(btnRegistrarPrestamo);
        panelBotones.add(btnRegistrarDevolucion);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha vencimiento",
                        "Estado"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPrestamos = new JTable(modeloTabla);

        tablaPrestamos.setAutoCreateRowSorter(true);

        tablaPrestamos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaPrestamos.setRowHeight(24);

        tablaPrestamos.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        JScrollPane scrollTabla = new JScrollPane(tablaPrestamos);

        JPanel panelCentro = new JPanel(
                new BorderLayout(10, 10)
        );

        panelCentro.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 20)
        );

        panelCentro.add(panelFormulario, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cmbLibro.addActionListener(
                evento -> mostrarInformacionLibro()
        );

        btnRegistrarPrestamo.addActionListener(
                evento -> registrarPrestamoEnSegundoPlano()
        );

        btnRegistrarDevolucion.addActionListener(
                evento -> registrarDevolucionEnSegundoPlano()
        );

        btnActualizar.addActionListener(
                evento -> cargarDatos()
        );

        btnCerrar.addActionListener(
                evento -> dispose()
        );

        cargarDatos();
    }

    private void cargarDatos() {
        cargarEstudiantes();
        cargarLibrosDisponibles();
        cargarPrestamos();
    }

    private void cargarEstudiantes() {
        cmbEstudiante.removeAllItems();

        try {
            List<Estudiante> estudiantes =
                    estudianteDAO.listarTodos();

            for (Estudiante estudiante : estudiantes) {
                cmbEstudiante.addItem(estudiante);
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar los estudiantes.",
                    error
            );
        }
    }

    private void cargarLibrosDisponibles() {
        cmbLibro.removeAllItems();

        try {
            List<Libro> libros = libroDAO.listarTodos();

            for (Libro libro : libros) {
                if (libro.getStock() > 0) {
                    cmbLibro.addItem(libro);
                }
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar los libros.",
                    error
            );
        }

        mostrarInformacionLibro();
    }

    private void cargarPrestamos() {
        modeloTabla.setRowCount(0);

        try {
            List<Prestamo> prestamos =
                    prestamoDAO.listarTodos();

            for (Prestamo prestamo : prestamos) {
                modeloTabla.addRow(new Object[]{
                        prestamo.getId(),
                        obtenerNombreEstudiante(
                                prestamo.getIdEstudiante()
                        ),
                        obtenerTituloLibro(
                                prestamo.getIdLibro()
                        ),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucion(),
                        obtenerEstado(prestamo)
                });
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar los préstamos.",
                    error
            );
        }
    }

    private String obtenerNombreEstudiante(int idEstudiante) {
        try {
            Estudiante estudiante =
                    estudianteDAO.buscarPorId(idEstudiante);

            if (estudiante == null) {
                return "Estudiante no encontrado";
            }

            return estudiante.getNombre();

        } catch (SQLException error) {
            return "Error al cargar estudiante";
        }
    }

    private String obtenerTituloLibro(int idLibro) {
        try {
            Libro libro = libroDAO.buscarPorId(idLibro);

            if (libro == null) {
                return "Libro no encontrado";
            }

            return libro.getTitulo();

        } catch (SQLException error) {
            return "Error al cargar libro";
        }
    }

    private String obtenerEstado(Prestamo prestamo) {
        if (prestamo.isDevuelto()) {
            return "Devuelto";
        }

        if (
                prestamo.getFechaDevolucion() != null
                        && LocalDate.now().isAfter(
                        prestamo.getFechaDevolucion()
                )
        ) {
            return "Atrasado";
        }

        return "Prestado";
    }

    private void mostrarInformacionLibro() {
        Libro libro = (Libro) cmbLibro.getSelectedItem();

        if (libro == null) {
            lblInformacionLibro.setText(
                    "No hay libros disponibles para préstamo."
            );

            return;
        }

        lblInformacionLibro.setText(
                "Stock disponible: "
                        + libro.getStock()
                        + " | Autor: "
                        + libro.getAutor()
        );
    }

    private void registrarPrestamoEnSegundoPlano() {
        Estudiante estudiante =
                (Estudiante) cmbEstudiante.getSelectedItem();

        Libro libro = (Libro) cmbLibro.getSelectedItem();

        if (estudiante == null || libro == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante y un libro disponible.",
                    "Datos requeridos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        btnRegistrarPrestamo.setEnabled(false);

        new Thread(() -> {
            try {
                boolean registrado =
                        prestamoService.registrarPrestamo(
                                estudiante.getId(),
                                libro.getId()
                        );

                SwingUtilities.invokeLater(() -> {
                    if (registrado) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Préstamo registrado correctamente.\n"
                                        + "Fecha de vencimiento: "
                                        + LocalDate.now().plusDays(7),
                                "Operación exitosa",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "No fue posible registrar el préstamo.\n"
                                        + "El libro no tiene stock disponible.",
                                "Sin stock",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }

                    cargarDatos();

                    btnRegistrarPrestamo.setEnabled(true);
                });

            } catch (SQLException | IllegalArgumentException error) {
                SwingUtilities.invokeLater(() -> {
                    mostrarMensajeError(
                            "No fue posible registrar el préstamo.",
                            error
                    );

                    btnRegistrarPrestamo.setEnabled(true);
                });
            }
        }).start();
    }

    private void registrarDevolucionEnSegundoPlano() {
        int filaSeleccionada =
                tablaPrestamos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un préstamo de la tabla para registrar la devolución.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tablaPrestamos.convertRowIndexToModel(
                        filaSeleccionada
                );

        int idPrestamo = (int) modeloTabla.getValueAt(
                filaModelo,
                0
        );

        String estado = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 5)
        );

        if (estado.equals("Devuelto")) {
            JOptionPane.showMessageDialog(
                    this,
                    "El préstamo seleccionado ya fue devuelto.",
                    "Operación no realizada",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea registrar la devolución del préstamo seleccionado?",
                "Confirmar devolución",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        btnRegistrarDevolucion.setEnabled(false);

        new Thread(() -> {
            try {
                boolean registrada =
                        prestamoService.registrarDevolucion(
                                idPrestamo
                        );

                SwingUtilities.invokeLater(() -> {
                    if (registrada) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Devolución registrada correctamente.",
                                "Operación exitosa",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "No fue posible registrar la devolución.",
                                "Operación no realizada",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }

                    cargarDatos();

                    btnRegistrarDevolucion.setEnabled(true);
                });

            } catch (SQLException | IllegalArgumentException error) {
                SwingUtilities.invokeLater(() -> {
                    mostrarMensajeError(
                            "No fue posible registrar la devolución.",
                            error
                    );

                    btnRegistrarDevolucion.setEnabled(true);
                });
            }
        }).start();
    }

    private void mostrarError(
            String mensaje,
            SQLException error
    ) {
        mostrarMensajeError(mensaje, error);
    }

    private void mostrarMensajeError(
            String mensaje,
            Exception error
    ) {
        JOptionPane.showMessageDialog(
                this,
                mensaje
                        + "\n\nDetalle técnico: "
                        + error.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

