package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.model.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

public class EstudianteFrame extends JFrame {

    private final EstudianteDAO estudianteDAO;
    private final DefaultTableModel modeloTabla;
    private final JTable tablaEstudiantes;

    private final JLabel lblDetalleNombre;
    private final JLabel lblDetalleRut;
    private final JLabel lblDetalleCurso;
    private final JLabel lblDetalleCorreo;

    public EstudianteFrame() {
        estudianteDAO = new EstudianteDAO();

        setTitle("Biblioteca Escolar - Gestión de estudiantes");
        setSize(950, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel(
                "Gestión de estudiantes",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(31, 78, 121));

        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10)
        );

        JLabel subtitulo = new JLabel(
                "Seleccione un estudiante en la tabla para ver su información detallada.",
                SwingConstants.CENTER
        );

        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        subtitulo.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 12, 10)
        );

        JPanel panelEncabezado = new JPanel(new BorderLayout());

        panelEncabezado.add(titulo, BorderLayout.CENTER);
        panelEncabezado.add(subtitulo, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre",
                        "RUT",
                        "Curso",
                        "Correo"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTabla);

        tablaEstudiantes.setAutoCreateRowSorter(true);

        tablaEstudiantes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaEstudiantes.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        tablaEstudiantes.setRowHeight(25);

        JScrollPane scrollTabla = new JScrollPane(tablaEstudiantes);

        JLabel tituloDetalle = new JLabel(
                "Información del estudiante seleccionado"
        );

        tituloDetalle.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloDetalle.setForeground(new Color(31, 78, 121));

        lblDetalleNombre = crearEtiquetaDetalle();
        lblDetalleRut = crearEtiquetaDetalle();
        lblDetalleCurso = crearEtiquetaDetalle();
        lblDetalleCorreo = crearEtiquetaDetalle();

        limpiarDetalle();

        JPanel panelDatosDetalle = new JPanel(
                new GridLayout(4, 1, 0, 7)
        );

        panelDatosDetalle.add(lblDetalleNombre);
        panelDatosDetalle.add(lblDetalleRut);
        panelDatosDetalle.add(lblDetalleCurso);
        panelDatosDetalle.add(lblDetalleCorreo);

        JPanel panelDetalle = new JPanel(new BorderLayout(0, 12));

        panelDetalle.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Detalle del estudiante"
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                20,
                                15,
                                20
                        )
                )
        );

        panelDetalle.add(tituloDetalle, BorderLayout.NORTH);
        panelDetalle.add(panelDatosDetalle, BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(
                new BorderLayout(10, 10)
        );

        panelCentro.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 20)
        );

        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        panelCentro.add(panelDetalle, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 12)
        );

        JButton btnAgregar = new JButton("Agregar estudiante");

        JButton btnEditar = new JButton(
                "Editar seleccionado"
        );

        JButton btnEliminar = new JButton(
                "Eliminar seleccionado"
        );

        JButton btnActualizar = new JButton(
                "Actualizar listado"
        );

        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        tablaEstudiantes.getSelectionModel()
                .addListSelectionListener(evento -> {
                    if (!evento.getValueIsAdjusting()) {
                        mostrarDetalleSeleccionado();
                    }
                });

        btnAgregar.addActionListener(
                evento -> agregarEstudiante()
        );

        btnEditar.addActionListener(
                evento -> editarEstudiante()
        );

        btnEliminar.addActionListener(
                evento -> eliminarEstudiante()
        );

        btnActualizar.addActionListener(
                evento -> cargarEstudiantes()
        );

        btnCerrar.addActionListener(
                evento -> dispose()
        );

        cargarEstudiantes();
    }

    private JLabel crearEtiquetaDetalle() {
        JLabel etiqueta = new JLabel();

        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 16));

        return etiqueta;
    }

    private void limpiarDetalle() {
        lblDetalleNombre.setText("Nombre: Seleccione un estudiante");
        lblDetalleRut.setText("RUT: -");
        lblDetalleCurso.setText("Curso: -");
        lblDetalleCorreo.setText("Correo: -");
    }

    private void mostrarDetalleSeleccionado() {
        int filaSeleccionada =
                tablaEstudiantes.getSelectedRow();

        if (filaSeleccionada == -1) {
            limpiarDetalle();
            return;
        }

        int filaModelo =
                tablaEstudiantes.convertRowIndexToModel(
                        filaSeleccionada
                );

        String nombre = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 1)
        );

        String rut = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 2)
        );

        String curso = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 3)
        );

        String correo = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 4)
        );

        lblDetalleNombre.setText("Nombre: " + nombre);
        lblDetalleRut.setText("RUT: " + rut);
        lblDetalleCurso.setText("Curso: " + curso);
        lblDetalleCorreo.setText("Correo: " + correo);
    }

    private void cargarEstudiantes() {
        modeloTabla.setRowCount(0);
        limpiarDetalle();

        try {
            List<Estudiante> estudiantes =
                    estudianteDAO.listarTodos();

            for (Estudiante estudiante : estudiantes) {
                modeloTabla.addRow(new Object[]{
                        estudiante.getId(),
                        estudiante.getNombre(),
                        estudiante.getRut(),
                        estudiante.getCurso(),
                        estudiante.getCorreo()
                });
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar los estudiantes.",
                    error
            );
        }
    }

    private void agregarEstudiante() {
        EstudianteFormularioDialog formulario =
                new EstudianteFormularioDialog(this, null);

        formulario.setVisible(true);

        if (!formulario.isGuardado()) {
            return;
        }

        try {
            boolean creado = estudianteDAO.crear(
                    formulario.getEstudiante()
            );

            if (creado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Estudiante agregado correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarEstudiantes();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo agregar el estudiante.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible agregar el estudiante.\n"
                            + "Verifique que el RUT no esté repetido.",
                    error
            );
        }
    }

    private void editarEstudiante() {
        int filaSeleccionada =
                tablaEstudiantes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante de la tabla para editar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tablaEstudiantes.convertRowIndexToModel(
                        filaSeleccionada
                );

        int idEstudiante = (int) modeloTabla.getValueAt(
                filaModelo,
                0
        );

        try {
            Estudiante estudiante =
                    estudianteDAO.buscarPorId(idEstudiante);

            if (estudiante == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "El estudiante ya no existe en la base de datos.",
                        "Estudiante no encontrado",
                        JOptionPane.WARNING_MESSAGE
                );

                cargarEstudiantes();
                return;
            }

            EstudianteFormularioDialog formulario =
                    new EstudianteFormularioDialog(
                            this,
                            estudiante
                    );

            formulario.setVisible(true);

            if (!formulario.isGuardado()) {
                return;
            }

            boolean actualizado = estudianteDAO.actualizar(
                    formulario.getEstudiante()
            );

            if (actualizado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Estudiante actualizado correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarEstudiantes();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar el estudiante.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible editar el estudiante.\n"
                            + "Verifique que el RUT no esté repetido.",
                    error
            );
        }
    }

    private void eliminarEstudiante() {
        int filaSeleccionada =
                tablaEstudiantes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante de la tabla para eliminar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tablaEstudiantes.convertRowIndexToModel(
                        filaSeleccionada
                );

        int idEstudiante = (int) modeloTabla.getValueAt(
                filaModelo,
                0
        );

        String nombreEstudiante = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 1)
        );

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar al estudiante:\n"
                        + nombreEstudiante
                        + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean eliminado = estudianteDAO.eliminar(
                    idEstudiante
            );

            if (eliminado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Estudiante eliminado correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarEstudiantes();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar el estudiante.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible eliminar el estudiante.\n"
                            + "Puede tener préstamos asociados.",
                    error
            );
        }
    }

    private void mostrarError(String mensaje, SQLException error) {
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
