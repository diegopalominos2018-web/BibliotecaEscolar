package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.dao.ReporteDAO;
import cl.duoc.biblioteca.model.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReporteFrame extends JFrame {

    private final ReporteDAO reporteDAO;
    private final EstudianteDAO estudianteDAO;

    private final DefaultTableModel modeloLibrosMasPrestados;
    private final DefaultTableModel modeloHistorialEstudiante;
    private final DefaultTableModel modeloLibrosPrestados;

    private final JTable tablaLibrosMasPrestados;
    private final JTable tablaHistorialEstudiante;
    private final JTable tablaLibrosPrestados;

    private final JComboBox<Estudiante> cmbEstudiante;

    public ReporteFrame() {
        reporteDAO = new ReporteDAO();
        estudianteDAO = new EstudianteDAO();

        setTitle("Biblioteca Escolar - Reportes");
        setSize(1050, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel(
                "Reportes de Biblioteca Escolar",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(31, 78, 121));

        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 12, 10)
        );

        modeloLibrosMasPrestados = crearModeloNoEditable(
                new Object[]{
                        "ID",
                        "Título",
                        "Autor",
                        "Cantidad de préstamos"
                }
        );

        tablaLibrosMasPrestados = new JTable(
                modeloLibrosMasPrestados
        );

        configurarTabla(tablaLibrosMasPrestados);

        JPanel panelMasPrestados = new JPanel(
                new BorderLayout(10, 10)
        );

        panelMasPrestados.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel lblMasPrestados = new JLabel(
                "Ranking de libros según cantidad de préstamos"
        );

        lblMasPrestados.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );

        JButton btnActualizarMasPrestados = new JButton(
                "Actualizar ranking"
        );

        JPanel panelTituloMasPrestados = new JPanel(
                new BorderLayout()
        );

        panelTituloMasPrestados.add(
                lblMasPrestados,
                BorderLayout.WEST
        );

        panelTituloMasPrestados.add(
                btnActualizarMasPrestados,
                BorderLayout.EAST
        );

        panelMasPrestados.add(
                panelTituloMasPrestados,
                BorderLayout.NORTH
        );

        panelMasPrestados.add(
                new JScrollPane(tablaLibrosMasPrestados),
                BorderLayout.CENTER
        );

        modeloHistorialEstudiante = crearModeloNoEditable(
                new Object[]{
                        "ID préstamo",
                        "Título",
                        "Autor",
                        "Fecha préstamo",
                        "Fecha vencimiento",
                        "Estado"
                }
        );

        tablaHistorialEstudiante = new JTable(
                modeloHistorialEstudiante
        );

        configurarTabla(tablaHistorialEstudiante);

        cmbEstudiante = new JComboBox<>();

        JButton btnBuscarHistorial = new JButton(
                "Ver historial"
        );

        JPanel panelSeleccionEstudiante = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        panelSeleccionEstudiante.add(
                new JLabel("Estudiante:")
        );

        cmbEstudiante.setPrototypeDisplayValue(
                new Estudiante(
                        0,
                        "Nombre de estudiante de ejemplo",
                        "11111111-1",
                        "4to Medio A",
                        "correo@ejemplo.cl"
                )
        );

        panelSeleccionEstudiante.add(cmbEstudiante);
        panelSeleccionEstudiante.add(btnBuscarHistorial);

        JPanel panelHistorial = new JPanel(
                new BorderLayout(10, 10)
        );

        panelHistorial.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        panelHistorial.add(
                panelSeleccionEstudiante,
                BorderLayout.NORTH
        );

        panelHistorial.add(
                new JScrollPane(tablaHistorialEstudiante),
                BorderLayout.CENTER
        );

        modeloLibrosPrestados = crearModeloNoEditable(
                new Object[]{
                        "ID préstamo",
                        "Estudiante",
                        "Curso",
                        "Libro",
                        "Autor",
                        "Fecha préstamo",
                        "Fecha vencimiento",
                        "Estado"
                }
        );

        tablaLibrosPrestados = new JTable(
                modeloLibrosPrestados
        );

        configurarTabla(tablaLibrosPrestados);

        JPanel panelPrestados = new JPanel(
                new BorderLayout(10, 10)
        );

        panelPrestados.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel lblPrestados = new JLabel(
                "Libros que actualmente se encuentran prestados"
        );

        lblPrestados.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );

        JButton btnActualizarPrestados = new JButton(
                "Actualizar listado"
        );

        JPanel panelTituloPrestados = new JPanel(
                new BorderLayout()
        );

        panelTituloPrestados.add(
                lblPrestados,
                BorderLayout.WEST
        );

        panelTituloPrestados.add(
                btnActualizarPrestados,
                BorderLayout.EAST
        );

        panelPrestados.add(
                panelTituloPrestados,
                BorderLayout.NORTH
        );

        panelPrestados.add(
                new JScrollPane(tablaLibrosPrestados),
                BorderLayout.CENTER
        );

        JTabbedPane pestanas = new JTabbedPane();

        pestanas.addTab(
                "Libros más prestados",
                panelMasPrestados
        );

        pestanas.addTab(
                "Historial por estudiante",
                panelHistorial
        );

        pestanas.addTab(
                "Libros actualmente prestados",
                panelPrestados
        );

        JButton btnCerrar = new JButton("Cerrar");

        JPanel panelInferior = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        panelInferior.add(btnCerrar);

        add(titulo, BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnActualizarMasPrestados.addActionListener(
                evento -> cargarLibrosMasPrestados()
        );

        btnBuscarHistorial.addActionListener(
                evento -> cargarHistorialEstudiante()
        );

        btnActualizarPrestados.addActionListener(
                evento -> cargarLibrosPrestados()
        );

        btnCerrar.addActionListener(
                evento -> dispose()
        );

        cargarEstudiantes();
        cargarLibrosMasPrestados();
        cargarLibrosPrestados();
    }

    private DefaultTableModel crearModeloNoEditable(
            Object[] columnas
    ) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void configurarTabla(JTable tabla) {
        tabla.setAutoCreateRowSorter(true);
        tabla.setRowHeight(24);

        tabla.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
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

    private void cargarLibrosMasPrestados() {
        modeloLibrosMasPrestados.setRowCount(0);

        try {
            List<Object[]> filas =
                    reporteDAO.obtenerLibrosMasPrestados();

            for (Object[] fila : filas) {
                modeloLibrosMasPrestados.addRow(fila);
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar el ranking de libros.",
                    error
            );
        }
    }

    private void cargarHistorialEstudiante() {
        Estudiante estudiante =
                (Estudiante) cmbEstudiante.getSelectedItem();

        if (estudiante == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        modeloHistorialEstudiante.setRowCount(0);

        try {
            List<Object[]> filas =
                    reporteDAO.obtenerHistorialPorEstudiante(
                            estudiante.getId()
                    );

            for (Object[] fila : filas) {
                boolean devuelto = (boolean) fila[5];

                String estado = obtenerEstadoHistorial(
                        fila[4],
                        devuelto
                );

                modeloHistorialEstudiante.addRow(
                        new Object[]{
                                fila[0],
                                fila[1],
                                fila[2],
                                fila[3],
                                fila[4],
                                estado
                        }
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar el historial del estudiante.",
                    error
            );
        }
    }

    private void cargarLibrosPrestados() {
        modeloLibrosPrestados.setRowCount(0);

        try {
            List<Object[]> filas =
                    reporteDAO.obtenerLibrosPrestados();

            for (Object[] fila : filas) {
                String estado = obtenerEstadoPrestamoActual(
                        fila[6]
                );

                modeloLibrosPrestados.addRow(
                        new Object[]{
                                fila[0],
                                fila[1],
                                fila[2],
                                fila[3],
                                fila[4],
                                fila[5],
                                fila[6],
                                estado
                        }
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar los libros prestados.",
                    error
            );
        }
    }

    private String obtenerEstadoHistorial(
            Object fechaDevolucion,
            boolean devuelto
    ) {
        if (devuelto) {
            return "Devuelto";
        }

        LocalDate vencimiento =
                (LocalDate) fechaDevolucion;

        if (LocalDate.now().isAfter(vencimiento)) {
            return "Atrasado";
        }

        return "Prestado";
    }

    private String obtenerEstadoPrestamoActual(
            Object fechaDevolucion
    ) {
        LocalDate vencimiento =
                (LocalDate) fechaDevolucion;

        if (LocalDate.now().isAfter(vencimiento)) {
            return "Atrasado";
        }

        return "Prestado";
    }

    private void mostrarError(
            String mensaje,
            SQLException error
    ) {
        JOptionPane.showMessageDialog(
                this,
                mensaje
                        + "\n\nDetalle técnico: "
                        + error.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
